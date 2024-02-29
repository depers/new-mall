
# 项目描述

  该项目是一个电商系统，是我自己学习和实践的一个项目。这个项目中实现了一个电商系统的基本功能，包括分类、商品、订单、支付、用户认证、搜索等核心模块。我通过不断扩充自己的Java技术栈，将自己学习到的知识实践到这个系统中去。这个系统一共分为六个阶段，分别是单体、集群、分布式、微服务、服务容器化和性能调优，目前我已经完成了分布式这块的开发工作。

# 技术描述

* 单体项目：通过Spring Boot + Mybatis实现了电商项目的**基本逻辑**和**支付**功能。
* 集群项目：
    * 使用**LVS+Keepalived+Nginx**实现高可用的主备负载均衡
    * 使用**Redis**实现交易的认证和相关业务逻辑。
* 分布式项目：
    * 单点登录CAS系统的实现
    * 分布式搜索引擎**Elasticsearch+Logstash**实现搜索功能
    * 分布式文件系统**FastDFS**实现文件存储
    * 实现**RabbitMQ**消息的百分百投递
    * **通过Kafka实现海量日志的收集**
    * 分布式**锁**的设计
    * 使用**MyCAT**和**Sharding-JDBC**实现分库分表
    * 分布式全局ID的实现
# 开发分支

| 序号 | 阶段    | 分支                                    |
|----|-------|---------------------------------------|
| 1  | 单体    | first-stage_singleItem                |
| 2  | 集群    | second-stage_clusterArchitecture      |
| 3  | 分布式   | third-stage_distributedArchitecture   |
| 4  | 微服务   | fourth-stage_microserviceArchitecture |
| 5  | 服务容器化 | fifth-service_containerization        |
| 6  | 性能调优  | sixth-performance_tuning              |
