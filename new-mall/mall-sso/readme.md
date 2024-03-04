# mall-SSO 单点登录

## 核心逻辑
* 用户登录与校验
  1. 判断全局票据是否存在，若存在创建并返回一张临时票据
  2. 判断全局票据是否存在，若不存在则让用户登录
* 创建用户会话、全局门票、临时票据
  1. 根据用户账号和密码获取用户信息，并将用户信息保存到redis
  2. 创建全局票据，并将其保存到cookie和redis
  3. 创建临时票据，并将其保存到redis，与全局票据不同的是临时票据是有有效期的
  4. 将临时票据携带在url中跳转返回
* 验证与销毁临时票据
  1. 校验临时票据，若校验通过则从redis中删除
  2. 从cookie中获取全局票据，根据全局票据从redis中获取用户id
  3. 根据用户id将用户信息取出并返回
* 退出登录
  1. 删除cookie信息
  2. 删除redis中的全局票据
  3. 删除redis中的用户信息

## 分布式会话的解决方法
 1. 使用redis+token的方式（相同顶级域名）
 2. 使用Spring Session将认证信息保存到redis或是数据库中（Session是不支持跨域的）
 3. SSO单点登录（不同顶级域名）