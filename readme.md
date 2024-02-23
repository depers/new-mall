# 商场项目二阶段的开发内容

## 工程介绍
* new-mall：商城主体项目
* new-payment：支付项目

## 本阶段内容
1. 集群架构
   * Nginx核心原理解析-反向代理/负载均衡/动静分离
   * Nginx负载均衡-策略解析/故障转移
   * 一致性哈希算法解析


2. 分布式缓存

## 涉及关键知识点
1. 事务的传播机制
2. PageHelper的工作原理

## 待完善的工作
1. 订单超时的批量需要在后续可以通过MQ或是集群批量进行优化。
2. 用户购物车的数据计划存储到Redis中。
3. 商城项目和支付项目目前尚未对接起来，这块还要看下视频，看下怎么来做。
4. 用户交易的授权控制还没有做，后续计划通过Redis来实现。
