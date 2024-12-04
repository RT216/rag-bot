from flask import Flask, request, jsonify
from typing import List, Dict, Any, Optional
import json
from dataclasses import dataclass
from http import HTTPStatus
import logging
from text_vectorizer import TextVectorizer
from openai import OpenAI
import time

# 使用dataclass来定义请求和响应的数据结构
@dataclass
class ChatRequest:
    message: str
    history_messages: List[str]

    @classmethod
    def from_dict(cls, data: Dict[str, Any]) -> 'ChatRequest':
        return cls(
            message=data.get('message', ''),
            history_messages=data.get('historyMessages', [])
        )

@dataclass
class ChatResponse:
    response: str
    status: str
    error: Optional[str] = None

    def to_dict(self) -> Dict[str, Any]:
        result = {
            'response': self.response,
            'status': self.status
        }
        if self.error:
            result['error'] = self.error
        return result

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
                    stream=False  # Web API中使用非流式响应
                )
            except Exception as e:
                if attempt == retries - 1:
                    raise e
                logging.error(f"Error in chat completion, retrying: {str(e)}")
                time.sleep(delay)

class RAGChatService:
    """RAG聊天服务类，处理核心业务逻辑"""
    def __init__(self, config_path: str):
        self.config = self._load_config(config_path)
        self.completion_handler = ChatCompletionHandler(self.config['openai']['api_key'])
        self.vectorizer = TextVectorizer(config_path)
        self.vectorizer.load_index("data/store_knowledge.index")

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
            logging.error(f"Error getting context: {str(e)}")
            return ""

    def _generate_prompt_with_context(self, query: str, context: str) -> str:
        """生成包含上下文的prompt"""
        return f"""基于以下参考信息回答用户的问题。如果参考信息不足以回答问题，请说明无法回答或需要更多信息。

参考信息：
{context}

用户问题：{query}

回答："""

    def _prepare_messages(self, user_input: str, history_messages: List[str]) -> List[Dict[str, str]]:
        """准备消息列表"""
        messages = [Message("system", 
            "你是一个知识丰富的AI助手，会基于提供的上下文信息回答问题。如果上下文信息不足，会明确告知用户。"
        ).to_dict()]
        
        # 添加历史消息
        for msg in history_messages:
            messages.append(Message("user", msg).to_dict())
            
        # 获取上下文并添加当前问题
        context = self._get_relevant_context(user_input)
        prompt_with_context = self._generate_prompt_with_context(user_input, context)
        messages.append(Message("user", prompt_with_context).to_dict())
        
        return messages

    def process_chat(self, request: ChatRequest) -> ChatResponse:
        """处理聊天请求"""
        try:
            messages = self._prepare_messages(request.message, request.history_messages)
            completion = self.completion_handler.create_completion(messages)
            response = completion.choices[0].message.content
            return ChatResponse(response=response, status="success")
        except Exception as e:
            logging.error(f"Error processing chat: {str(e)}")
            return ChatResponse(
                response="",
                status="error",
                error=str(e)
            )

class ChatAPI:
    """Web API类，处理HTTP请求"""
    def __init__(self, config_path: str):
        self.app = Flask(__name__)
        self.chat_service = RAGChatService(config_path)
        self._setup_routes()
        
    def _setup_routes(self):
        """设置路由"""
        self.app.route('/chat', methods=['POST'])(self.chat_endpoint)

    def chat_endpoint(self):
        """处理聊天请求的端点"""
        try:
            data = request.get_json()
            chat_request = ChatRequest.from_dict(data)
            response = self.chat_service.process_chat(chat_request)
            return jsonify(response.to_dict()), HTTPStatus.OK
        except Exception as e:
            logging.error(f"API error: {str(e)}")
            return jsonify({
                'status': 'error',
                'error': str(e)
            }), HTTPStatus.INTERNAL_SERVER_ERROR

    def run(self, host: str = '0.0.0.0', port: int = 8080):
        """运行Web服务"""
        self.app.run(host=host, port=port)

def setup_logging():
    """配置日志"""
    logging.basicConfig(
        level=logging.INFO,
        format='%(asctime)s - %(name)s - %(levelname)s - %(message)s'
    )

if __name__ == "__main__":
    setup_logging()
    api = ChatAPI('config/config.json')
    api.run()