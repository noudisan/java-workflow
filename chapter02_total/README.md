# IT-CLOUD

[![AUR](https://img.shields.io/aur/license/yaourt.svg)](https://github.com/Exrick/xmall/blob/master/License)
[![](https://img.shields.io/badge/Author-Exrick-orange.svg)](http://blog.exrick.cn)
[![](https://img.shields.io/badge/version-1.2-brightgreen.svg)](https://github.com/Exrick/x-boot)
[![GitHub stars](https://img.shields.io/github/stars/Exrick/x-boot.svg?style=social&label=Stars)](https://github.com/Exrick/x-boot)
[![GitHub forks](https://img.shields.io/github/forks/Exrick/x-boot.svg?style=social&label=Fork)](https://github.com/Exrick/x-boot)


### 官网
- 地址：http://yyitcloud.com
- 后端源码：https://gitee.com/simagang/it-cloud
### 在线Demo
http://yyitcloud.com
### 最新最全面在线文档
https://www.kancloud.cn/exrick/xboot/content
### 前台基于Vue+iView-Admin项目地址： [it-cloud-vue](https://gitee.com/simagang/it-cloud-vue)
### 平台简介
IT服务管理平台（IT-CLOUD）是一个整合的运维管理平台，主要包括基础服务管理和中间件管理，另外整合工作学习中用到的种种技术。基于Spring Boot, Spring Cloud的前后端分离模式，前端使用Vue+iView Admin，后台使用Spring Boot 2.x/Shiro/Oauth2/Mybatis-Plus/Redis/RabbitMQ/NETTY/Druid/Swagger/Lombok/Quartz/Websocket/Elasticsearch/Activiti 熔断/负载均衡/验证码/Hutool工具类 等等。 主要包含两大模块：基础服务，中间件管理  块权限管理  工作流 代码生成 日志记录 定时任务 第三方社交账号、短信登录


## 实现功能
- [x] 权限管理：包括用户管理，角色管理，菜单管理
- [x] 定时任务：基于Quartz实现，包括启动，暂停，恢复，立即执行等功能
- [x] SQL监控：基于Druid实现
- [x] 接口文档：基于Swagger实现
- [x] 系统日志：记录系统中重要操作
- [x] 文件上传： 基于七牛云提供的OSS
- [x] 工作流引擎：实现业务工单流转、在线流程设计器。
- [x] 工作流管理：包括模型管理，流程定义管理，我的申请，待办任务，已办任务等等，其中以请假申请流程为例
- [x] Websocket：基于Websocket，RabbitMQ实现分布式消息推送管理
- [ ] 通知管理：短信，邮件等
- [ ] 中间件管理：包括Redis，Elashticsearch，ZooKeeper，MQ等中间件的操作管理
- [ ] 监控告警：服务监控和中间件监控

## 技术选型

1、后端

* 核心框架：Spring Boot 2.1.6.RELEASE
* 后台框架：Spring Cloud Finchley.RELEASE
* 安全框架：Shiro 1.4.0
* 服务端验证：Hibernate Validator
* 工作流引擎：Activiti 6.0.0
* 任务调度：Spring Task 4.1
* 持久层：MyBatis-Plus 3.1.2
* 数据库：Mysql，Alibaba Druid 1.1.10
* 缓存框架：Redis、Jedis 3.0.1、Ehcache
* 日志管理：SLF4J 1.7、Logback
* 工具类：Apache Commons、lombok，Swagger 2.8.0，Kaptcha 0.0.9，Hutool 4.5.16（强烈推荐）
* 第三方SDK:Qiniu
* Thymeleaf：邮件模板使用

2、前端

* 核心架构：Vue + iView Admin

### 系统架构图

 _现有系统架构偏向单体应用，后续会按图中架构拆分_ 

![输入图片说明](https://images.gitee.com/uploads/images/2019/0919/103134_ddc4a375_1187700.png "springcloud 微服务 架构图.png")

### 系统功能图

![输入图片说明](https://images.gitee.com/uploads/images/2019/0919/103208_9828cad7_1187700.png "IT_CLOUD系统功能设计.png")


## 为何选择IT-CLOUD

1. 源代码完全开源，无商业限制。
2. 使用目前主流的Java开发，简单易学，学习成本低。
3. 你可以把它当做基础技术的学习项目，例如：RabbitMQ, ES, Redis, Websocket等等
4. 注释详细，通俗易懂，本着学习的目的在做项目
5. 权限控制，基础服务完美整合，可以拿来即用
6. 工作流引擎整合SpringBoot搭建请假流程，在线流程设计器，使你快速入门流程引擎开发
7. 提供在线代码生成工具，提高开发效率和质量。
8. 永不间断更新！！！

## 部署运行

1. 建议运行环境：JDK1.8+、Maven3.0+、MySql5+，IDE编码工具
2. 运行src\main\resources\db\sql\下面的数据库脚本，包含两个拆分的ddl,data文件,还有一个完整的sql文件
3. 修改src\main\resources\application-dev.yml文件中的数据库配置，Redis配置，RabbitMQ配置,七牛云配置（可选）。
4. 首先启动一个eureka服务，如果没有可以使用 https://gitee.com/simagang/it-eureka-server，直接启动类就行（为以后微服务化做准备）
5. 运行Springboot启动类 com.it.cloud.ItCloudApplication

*注意：如果使用数据库脚本中的activiti 模型有问题的话，可以打开src\main\resources\application.yml中的activiti.check-process-definitions: true*
*会使用classpath:/processes/下默认的activiti流程图*

## 账号密码

| 账号  | 密码  | 备注   |
| ----- | ----- | ------ |
| test  | test  | 员工   |
| test1 | test1 | 经理   |
| test2 | test2 | 部门长 |



### 项目截图预览
![输入图片说明](https://images.gitee.com/uploads/images/2019/0919/105702_149c4652_1187700.png "微信截图_20190919104959.png")

![输入图片说明](https://images.gitee.com/uploads/images/2019/0919/105714_23a6380a_1187700.png "微信截图_20190919105042.png")

![输入图片说明](https://images.gitee.com/uploads/images/2019/0919/105735_634a0635_1187700.png "微信截图_20190919105100.png")

![输入图片说明](https://images.gitee.com/uploads/images/2019/0919/105745_91d76301_1187700.png "微信截图_20190919105112.png")

### 后端学习分享（更新中）
1. [Springboot整合websocket，使用消息队列实现分布式WebSocket](https://blog.csdn.net/yy756127197/article/details/98094505)


### 商用授权
- 商用需联系作者授权

### 作者其他项目推荐
- [预留](https://gitee.com/simagang/it-cloud-redis)

    
### 技术交流
- QQ交流群 `590418179` 

<p align="left">
  <img src="https://images.gitee.com/uploads/images/2019/0919/111149_5aada48f_1187700.jpeg" height="250"> 
</p>
###打赏
 