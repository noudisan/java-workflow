1. 建议运行环境：JDK1.8+、Maven3.0+、MySql5+，IDE编码工具
2. 运行src\main\resources\db\sql\下面的数据库脚本，包含两个拆分的ddl,data文件,还有一个完整的sql文件
3. 修改src\main\resources\application-dev.yml文件中的数据库配置，Redis配置，RabbitMQ配置,七牛云配置（可选）。
4. 首先启动一个eureka服务，如果没有可以使用 https://gitee.com/simagang/it-eureka-server，直接启动类就行（为以后微服务化做准备）
5. 运行Springboot启动类 com.it.cloud.ItCloudApplication

*注意：如果使用数据库脚本中的activiti 模型有问题的话，可以打开src\main\resources\application.yml中的activiti.check-process-definitions: true*
*会使用classpath:/processes/下默认的activiti流程图*
