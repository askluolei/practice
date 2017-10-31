# Zookeeper 学习

学习zookeeper的一些理论和用法

## 环境
学习使用的都是使用 `Docker` 启动的

### 快速开始

```cmd
docker run --name some-zookeeper --restart always -d zookeeper
```

启动一个 `zookeeper` 实例，端口使用的是 `2181`, `2888`, `3888`
但是上面这个命令没有将端口保留出来，因此无法在本机访问到，如果你的应用也是以容器启动的，可以通过如下命令访问

```mcd
docker run --name some-app --link some-zookeeper:zookeeper -d application-that-uses-zookeeper
```

当然，如果只是想简单的玩一下 `zookeeper` 那么就在启动的时候加上 `-p 2181:2181`,这样就可以在本机访问了
默认的配置文件如下
```zoo.cfg
clientPort=2181
dataDir=/data
dataLogDir=/datalog
tickTime=2000
initLimit=5
syncLimit=2
maxClientCnxns=60
```


### 集群启动

使用 `Docker` 的 `Zookeeper` 镜像，搭建3个 `Zookeeper` 节点的集群。
容器管理使用 `Docker-Compose`,`docker-compose.yml` 文件如下：
```yml
version: '3.1'

services:
    zoo1:
        image: zookeeper
        restart: always
        hostname: zoo1
        ports:
            - 2181:2181
        environment:
            ZOO_MY_ID: 1
            ZOO_SERVERS: server.1=zoo1:2888:3888 server.2=zoo2:2888:3888 server.3=zoo3:2888:3888

    zoo2:
        image: zookeeper
        restart: always
        hostname: zoo2
        ports:
            - 2182:2181
        environment:
            ZOO_MY_ID: 2
            ZOO_SERVERS: server.1=zoo1:2888:3888 server.2=zoo2:2888:3888 server.3=zoo3:2888:3888

    zoo3:
        image: zookeeper
        restart: always
        hostname: zoo3
        ports:
            - 2183:2181
        environment:
            ZOO_MY_ID: 3
            ZOO_SERVERS: server.1=zoo1:2888:3888 server.2=zoo2:2888:3888 server.3=zoo3:2888:3888
```

管理命令如下：
```cmd
启动命令
docker-compose -f .\docker-compose.yml up -d

停止命令
docker-compose -f .\docker-compose.yml stop

删除停止容器
docker-compose -f .\docker-compose.yml rm

说明：
3个zookeeper组成的集群模式，对外开放2181,2182,2183端口
```

默认的配置文件如下：
```zoo.cfg
clientPort=2181
dataDir=/data
dataLogDir=/datalog
tickTime=2000
initLimit=5
syncLimit=2
maxClientCnxns=60
server.1=zoo1:2888:3888
server.2=zoo2:2888:3888
server.3=zoo3:2888:3888
```


### 自定义配置文件

想要使用自己定义的 `zoo.cfg` 配置文件，可以将本机的配置文件挂载进去
```cmd
docker run --name some-zookeeper --restart always -d -v $(pwd)/zoo.cfg:/conf/zoo.cfg zookeeper
```

其中 `$(pwd)` 配置文件在本机的路径

### 添加环境参数
```cmd
docker run -e "ZOO_INIT_LIMIT=10" --name some-zookeeper --restart always -d 31z4/zookeeper
```

使用 `docker` 的命令 `-e` 添加环境参数

支持的环境参数

|环境参数| 默认值 | 对象配置字段 | 说明 |
|-------|--------|-------| ---- |
|ZOO_TICK_TIME|2000|tickTime| 客户端和服务器端维持心跳的时间，单位为毫秒 |
|ZOO_INIT_LIMIT|5|initLimit|  |
|ZOO_SYNC_LIMIT|2|syncLimit|  |
|ZOO_MAX_CLIENT_CNXNS|60|maxClientCnxns| 最大客户端连接数。如果客户端的数量超过了指定的数量，则新连接的客户端会抛出java.io.IOException: Connection reset by peer异常 |
|ZOO_STANDALONE_ENABLED|false|standaloneEnabled|  |

集群配置需要的环境参数

* ZOO_MY_ID
取值唯一 范围为 1-255，如果启动时候 `/data` 目录有 myid 文件，则参数无效
* ZOO_SERVERS
指定 `zookeeper` 所有节点,格式为 `server.id=host:port:port`,每个配置使用空格分隔
如果 `/conf/zoo.cfg` 文件存在，则本配置无效

### 数据存储
主要是两个目录 `/data` 内存数据备份 , `/datalog` 事务日志


## 简介

### 什么是Zookeeper
`Zookeeper`是一个高性能的分布式应用协调服务的框架。Zookeeper=Zoo+keeper,中文直译是动物园的看守者。

### 为什么要使用Zookeeper
Zookeeper可以用于解决很多分布式系统遇到的问题，比如分布式锁，分布式协调，分布式消息队列等。

### 命令行调用

 **连接 `zookeeper` 服务**
```cmd
zkCli -server 127.0.0.1:2181
```
注意，要在 zookeeper 的bin目录执行

**创建节点**
```cmd
create /llnode data
```
注意，目录大小写敏感

**查看节点**
```cmd
get /llnode
```
返回数据
```data
data
cZxid = 0x100000004
ctime = Tue Oct 31 14:16:53 CST 2017
mZxid = 0x100000004
mtime = Tue Oct 31 14:16:53 CST 2017
pZxid = 0x100000004
cversion = 0
dataVersion = 0
aclVersion = 0
ephemeralOwner = 0x0
dataLength = 4
numChildren = 0
```

除了返回数据，还返回了 zookeeper 的数据结构

* cxxid：当前节点事务的xxid。
* ctime：当前节点的创建时间。
* mxxid: 当前节点最近修改的xxid。
* pZxid ： 子节点的最后版本
* cversion：这个节点的子节点变化次数，创建一个子节点会加1。
* dataVersion：数据的版本，每次修改会加一。
* aclVersion：这个节点的ACL变化次数。
* ephemeralOwner ：如果这个节点是临时节点，表示创建者的会话id。如果不是临时节点，这个值是0。
* dataLength：这个节点存放的数据长度。
* numChildren：这个节点的子节点个数。
* mtime：这个节点的最后修改时间

**修改节点**
```cmd
set /llnode data1
```

返回数据
```data
cZxid = 0x100000004
ctime = Tue Oct 31 14:16:53 CST 2017
mZxid = 0x100000005
mtime = Tue Oct 31 14:21:43 CST 2017
pZxid = 0x100000004
cversion = 0
dataVersion = 1
aclVersion = 0
ephemeralOwner = 0x0
dataLength = 5
numChildren = 0
```

可以看到 `dataVersion` 发生了变化

**删除节点**
```cmd
delete /llnode
```

**help**
通过 `help` 命令可以看到支持哪些命令
```cmd
[zk: 127.0.0.1:2181(CONNECTED) 4] help
ZooKeeper -server host:port cmd args
        stat path [watch]
        set path data [version]
        ls path [watch]
        delquota [-n|-b] path
        ls2 path [watch]
        setAcl path acl
        setquota -n|-b val path
        history
        redo cmdno
        printwatches on|off
        delete path [version]
        sync path
        listquota path
        rmr path
        get path [watch]
        create [-s] [-e] path data acl
        addauth scheme auth
        quit
        getAcl path
        close
        connect host:port
```

