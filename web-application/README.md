# ai_manager
## 准备工作
1. 安装java、maven、mysql server
2. mvn clean install 
3. 初始化mysql
    * 新建数据库
    * 参考[application.yml](src%2Fmain%2Fresources%2Fapplication.yml)文件，按需修改db连接配置
4. 启动应用
    * java -jar target/ai-manager.jar
    * 启动过程中会自动执行schema.sql,新建数据表

## 项目说明
1. 目录结构
   - src/main/java
     - controller：web层控制器文件
     - biz: 业务层
       - manager:业务层服务，组装领域服务
       - dto：业务对象
     - core：领域层
       - service: 领域层服务，对核心服务能力的抽象
       - model：领域模型
     - base：基础层
       - mapper: ibatis数据表映射文件
       - entity：数据层模型
     - util：工具类（日志、错误码、异常等）
     - [AiManageApplication.java](src%2Fmain%2Fjava%2Forg%2Fuestc%2Fweglas%2FAiManageApplication.java) :应用启动文件
   - src/main/resources
     - db：sql目录
     - application.yml 配置文件
     - log4j2-spring.xml 日志配置文件，log4j2
     - mappers:ibatis数据表映射文件
     - static:静态文件，js、css、logo等
     - templates：项目自己实现的前端页面
   - src/test/java 测试用例 
