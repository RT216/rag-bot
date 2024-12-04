# 运动鞋店铺知识库问答系统

这是一个基于RAG (Retrieval-Augmented Generation) 架构的运动鞋店铺知识库问答系统。系统能够根据用户输入的问题，从知识库中检索相关信息，并生成准确的回答。

## 目录结构

```
├── config
│   └── config.json.template       # 配置文件模板
├── data
│   ├── store_knowledge.index      # Faiss 向量库存储目录
│   └── 运动鞋店铺知识库.txt          # 知识库txt文本文件
├── lec1_streamchat.py             # 普通的流式输入输出主程序
├── sql
│   ├── ai_context.sql             # 对话上下文数据库表结构
│   └── schema.sql                 # 数据库主要表结构
└── src
    ├── 01_write_to_faiss_test.py  # Faiss 向量库写入测试
    ├── rag_chat_bot.py            # RAG 问答机器人核心实现
    ├── rag_chat_api.py            # RAG 问答机器人Web API实现
    ├── text_vectorizer.py         # 文本向量化工具类及量化
    └── vectorizer_test.py         # 向量化功能测试
```

## 配置说明

在使用前，需要根据 `config.json.template` 创建 `config.json` 文件，填入相应的配置。

用户应当自行创建 `data/` 目录，并将知识库文本文件 `运动鞋店铺知识库.txt` 放入其中。

## 使用方法

1. 安装依赖：
```bash
pip install -r requirements.txt
```

2. 启动普通的流式输入输出（第一节课作业）：
```bash
python lec1_streamchat.py
```

3. 完成知识库文本向量化，并测试：
```bash
python src/text_vectorizer.py
python src/vectorizer_test.py
```

4. 启动RAG问答机器人（第二节课作业）：
```bash
python src/rag_chat_bot.py
```

5. 启动RAG问答机器人Web API（第三节课作业）：
```bash
python src/rag_chat_api.py
```

## 测试样例

### 知识库向量化功能测试
输出示例：
```
搜索查询: 如何退换货？
----------
ID: 5
相似度距离: 0.2745
文本内容: 退换货政策: 自收到商品12天内，商品未使用且原包装完好可申请退换货，需提供订单号和付款截图。

ID: 8
相似度距离: 0.3190
文本内容: 注意事项: 特价和促销商品不支持退换货，退回商品需妥善包装避免运输损坏。

ID: 6
相似度距离: 0.3234
文本内容: 退款处理: 商品返回检查无误后，退款将在11个工作日内处理完成。
```

### RAG 问答机器人
输入示例：
```
你: 如何退换货？
```

输出示例：
```
ChatGPT: 用户可以在收到商品后的12天内申请退换货。需要确保商品未使用且原包装完好。在申请退换货时，用户需要提供订单号和付款截图作为凭证。请注意，特价和促销商品是不支持退换货的，退回商品时，请妥善包装以避免运输损坏。一旦商品返回并经过检查确认无误，退款将在11个工作日内处理完成。
```

### RAG 问答机器人Web API
请求示例：
```bash
curl -X POST http://localhost:8080/chat \
  -H "Content-Type: application/json" \
  -d '{
    "message": "鞋子有哪些尺码可以选择呢",
    "historyMessages": ["历史聊天内容"]
  }'
```

响应示例：
```json
{
    "response": "根据产品信息提供的尺码选择范围，您可以在30至48的范围内选择适合您的尺码。具体而言，儿童款的尺码范围是30至36，而成人款的尺码范围是36至48。您可以根据自己的脚长来选择最适合您的尺码。",
    "status": "success"
}
```

## 开发说明

- `text_vectorizer.py`: 负责文本向量化，使用 OpenAI 的 embedding 模型
- `rag_chat_bot.py`: 实现 RAG 架构的问答逻辑
- `rag_chat_api.py`: 实现 RAG 问答机器人的Web API接口
- `sql/`: 包含数据库表结构，用于创建知识库文本数据库
- 测试文件可用于功能验证和调试

## 注意事项

- 请确保 OpenAI API Key 配置正确
- 首次运行需要建立向量库，可能需要一定时间
- 建议使用 Python 3.9 或以上版本
- Web API 默认运行在 8080 端口，可通过配置文件修改