from text_vectorizer import TextVectorizer
import json
import time
from openai import OpenAI
import sys
from typing import List, Dict, Any, Optional
import readline

class Message:
    """消息类，用于封装对话消息"""
    def __init__(self, role: str, content: str):
        self.role = role
        self.content = content

    def to_dict(self) -> Dict[str, str]:
        return {"role": self.role, "content": self.content}

class ChatCompletionHandler:
    """处理与OpenAI API交互的类"""
    def __init__(self, api_key: str):
        self.client = OpenAI(api_key=api_key)

    def create_completion(self, messages: List[Dict[str, str]], retries: int = 3, delay: int = 2):
        """创建聊天完成，包含重试机制"""
        for attempt in range(retries):
            try:
                return self.client.chat.completions.create(
                    model="gpt-3.5-turbo",
                    messages=messages,
                    stream=True
                )
            except Exception as e:
                if attempt == retries - 1:
                    raise e
                print(f"\n发生错误，{delay}秒后重试: {str(e)}")
                time.sleep(delay)

    def check_farewell_intent(self, text: str) -> bool:
        """检查是否有结束对话的意图"""
        try:
            response = self.client.chat.completions.create(
                model="gpt-3.5-turbo",
                messages=[
                    Message("system", "你的任务是判断用户的输入是否表达了想要结束对话的意图。如果是，请只回复'true'，如果不是，请只回复'false'。").to_dict(),
                    Message("user", f"用户说：{text}").to_dict()
                ],
                temperature=0,
                max_tokens=10
            )
            return response.choices[0].message.content.strip().lower() == 'true'
        except Exception as e:
            print(f"\n判断意图时发生错误: {str(e)}")
            return False

class ConversationManager:
    """管理对话历史的类"""
    def __init__(self):
        self.messages = [Message("system", "你是一个知识丰富的AI助手，会基于提供的上下文信息回答问题。如果上下文信息不足，会明确告知用户。")]

    def add_message(self, role: str, content: str):
        """添加新消息"""
        self.messages.append(Message(role, content))

    def clear_history(self):
        """清除对话历史"""
        system_message = self.messages[0]
        self.messages = [system_message]

    def get_messages_dict(self) -> List[Dict[str, str]]:
        """获取消息字典列表"""
        return [msg.to_dict() for msg in self.messages]

class RAGChatBot:
    """RAG增强的聊天机器人主类"""
    def __init__(self, config_path: str):
        self.config = self._load_config(config_path)
        self.completion_handler = ChatCompletionHandler(self.config['openai']['api_key'])
        self.vectorizer = TextVectorizer(config_path)
        self.vectorizer.load_index("data/store_knowledge.index")
        self.conversation = ConversationManager()

    @staticmethod
    def _load_config(config_path: str) -> Dict[str, Any]:
        """加载配置文件"""
        with open(config_path, 'r') as f:
            return json.load(f)

    def _get_relevant_context(self, query: str, k: int = 3) -> str:
        """获取相关上下文"""
        try:
            results = self.vectorizer.search_similar(query, k=k)
            return "\n".join(text for _, text, _ in results)
        except Exception as e:
            print(f"获取上下文时发生错误: {str(e)}")
            return ""

    def _generate_prompt_with_context(self, query: str, context: str) -> str:
        """生成包含上下文的prompt"""
        return f"""基于以下参考信息回答用户的问题。如果参考信息不足以回答问题，请说明无法回答或需要更多信息。

参考信息：
{context}

用户问题：{query}

回答："""

    def _process_user_input(self, user_input: str) -> Optional[str]:
        """处理用户输入，返回None表示继续对话，返回字符串表示特殊指令的响应"""
        if user_input.lower() == 'quit':
            return "再见！"
        
        if user_input.lower() == 'clear':
            self.conversation.clear_history()
            return "对话历史已清除！"
            
        if not user_input:
            return None
            
        if self.completion_handler.check_farewell_intent(user_input):
            return "感谢您的咨询，再见！"
            
        return None

    def _handle_chat_response(self, stream) -> str:
        """处理聊天响应流"""
        print("ChatGPT: ", end="", flush=True)
        full_response = ""
        for chunk in stream:
            if chunk.choices[0].delta.content is not None:
                content = chunk.choices[0].delta.content
                print(content, end="", flush=True)
                full_response += content
        print()
        return full_response

    def chat(self):
        """主聊天循环"""
        print("欢迎使用RAG增强的ChatGPT! 输入'quit'退出对话，输入'clear'清除对话历史。")
        
        while True:
            try:
                user_input = input("\n你: ").strip()
                
                # 处理特殊指令
                result = self._process_user_input(user_input)
                if result is not None:
                    print("ChatGPT:", result)
                    if result in ["再见！", "感谢您的咨询，再见！"]:
                        break
                    continue

                # 获取上下文并生成prompt
                context = self._get_relevant_context(user_input)
                prompt_with_context = self._generate_prompt_with_context(user_input, context)
                
                # 添加用户消息并获取回复
                self.conversation.add_message("user", prompt_with_context)
                stream = self.completion_handler.create_completion(self.conversation.get_messages_dict())
                
                # 处理回复
                full_response = self._handle_chat_response(stream)
                self.conversation.add_message("assistant", full_response)

            except KeyboardInterrupt:
                print("\n程序被用户中断")
                sys.exit(0)
            except Exception as e:
                print(f"\n发生错误: {str(e)}")

if __name__ == "__main__":
    rag_bot = RAGChatBot('config/config.json')
    rag_bot.chat()
