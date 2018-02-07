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
