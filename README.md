# RAG 知识库问答系统

这是一个基于 RAG (Retrieval-Augmented Generation) 架构的知识库问答系统，目前以运动鞋店铺知识库为例进行实现。系统能够根据用户的问题，从知识库中检索相关信息，并结合大语言模型生成准确的回答。

## 项目背景

传统的大语言模型在回答特定领域问题时可能会产生幻觉或给出不准确的信息。通过 RAG 架构，我们可以将特定领域的知识库与大语言模型结合，确保回答的准确性和相关性。

## 项目结构

项目采用前后端分离的微服务架构，分为两个主要部分：

### 1. RAG Service (Python)
位于 `rag-service/` 目录，负责核心的 RAG 功能实现：
- 文本向量化和检索（基于 FAISS）
- 与 OpenAI API 的交互
- 流式输出支持
- RESTful API 接口

主要文件：
- `src/text_vectorizer.py`: 文本向量化核心实现
- `src/rag_chat_api.py`: Web API 实现
- `data/运动鞋店铺知识库.txt`: 示例知识库文件

### 2. Web Application (Java)
位于 `web-application/` 目录，提供 Web 界面和对话管理功能：
- 基于 Spring Boot 的后端实现
- Thymeleaf 模板引擎的前端实现
- MySQL 数据库存储对话历史
- 支持流式输出的对话交互

采用 DDD (Domain-Driven Design) 架构：
- `controller/`: Web 控制器
- `core/service/`: 核心业务逻辑
- `core/model/`: 领域模型
- `base/entity/`: 数据库实体

## 已实现功能

1. **知识库管理**
   - 文本向量化和存储
   - 相似度检索

2. **对话功能**
   - 新建对话
   - 历史对话查看
   - 实时对话（支持流式输出）
   - 对话历史保存

3. **用户界面**
   - 响应式布局
   - 对话历史展示

## 快速开始

1. **环境要求**
   - Python 3.9+
   - Java 8+
   - MySQL 8.0+
   - Maven 3.6+

2. **配置文件**
   ```bash
   # RAG Service
   cp rag-service/config/config.json.template rag-service/config/config.json
   # 编辑 config.json 添加 OpenAI API Key

   # Web Application
   # 编辑 web-application/src/main/resources/application.yml
   # 配置数据库连接信息
   ```

3. **启动服务**
   ```bash
   # 启动 RAG Service
   python rag-service/src/rag_chat_api.py

   # 启动 Web Application
   cd web-application
   mvn clean install
   java -jar target/ai-manager.jar
   ```

4. **访问系统**
   - 打开浏览器访问 `http://localhost:8081`
   - 创建新对话或查看历史对话
   - 开始提问

## 技术栈

- 后端：Python (FastAPI), Java (Spring Boot)
- 前端：Thymeleaf, Bootstrap, jQuery
- 数据库：MySQL, FAISS
- AI：OpenAI API
- 其他：Maven, Log4j2, WebFlux

## 注意事项

- 需要有效的 OpenAI API Key
- 确保 Python 和 Java 服务的端口不冲突
- 建议使用 SSD 存储 FAISS 索引以提高性能

## 未来计划

- [ ] 完善文档和注释
- [ ] 添加用户认证
- [ ] 支持多知识库切换
- [ ] 优化向量检索算法
- [ ] 添加对话导出功能
- [ ] 支持知识库在线更新

## 贡献

欢迎提交 Issue 和 Pull Request 来帮助改进项目。