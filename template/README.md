# spring-boot 模版项目

## 介绍
基于 `JHipster` 的模版项目

## sonar 代码分析   

   
先启动sonar服务
`docker-compose -f src/main/docker/sonar.yml up -d`     

开始分析    
`mvn sonar:sonar`   

浏览器打开   
`http://localhost:9000/dashboard/index/com.luolei:template` 

## Gatling 性能测试 
1. 先启动应用
2. 执行 `gatling:execute` 
3. 执行完成后会有测试报告  
4. [Gatling 参考文档 ](https://gatling.io/docs/current/general)     

### 概念  

#### 用户 
不同于其他测试工具直接通过线程数访问url，Gatling使用用户的概念，每个用户可以用自己的数据来发送请求  

#### 脚本 
就是Gatling的一系列请求是可以按照用户的访问逻辑来模拟的。   
譬如购物的流程通常为：     
1. 访问首页
2. 选择分类
3. 在分类中搜索
4. 点开商品介绍（详情）
5. 返回
6. 点开另一个商品介绍
7. 购买商品
8. 登录
9. 结账
10. 付款
11. 退出登录    

以上就是一个用户购物可能触发的逻辑
这样就可以稍微模拟正式环境可能出现的压力情况，而不仅仅是单纯的多线程发送请求来压测   

[参考文档](https://gatling.io/docs/current/general/scenario#scenario)   

#### session    
每个虚拟用户被session支持，session中是具体的数据，在请求过程中可以通过占位符使用，注入，存储数据     
[参考文档](https://gatling.io/docs/current/session/session_api/#session)    

#### 