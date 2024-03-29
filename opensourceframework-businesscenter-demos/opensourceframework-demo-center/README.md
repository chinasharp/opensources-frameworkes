## 快速启动  
    IDE启动,VM options设置参数:
    #-Djava.library.path=java_native_library
    -Dopensourceframework.server.port=8081
    -Dopensourceframework.profiles.active=dev
    -Dopensourceframework.env.logger.workdir=./logs
    -Dopensourceframework.env.logger.level=debug
    -Dopensourceframework.service.version=1.0.0
    -Dopensourceframework.env.module=opensourceframework-demo-center
    -Dopensourceframework.service.server.username=nacos
    -Dopensourceframework.service.server.password=nacos
    -Dopensourceframework.service.server.address=127.0.0.1:8848
    -Dopensourceframework.service.server.namespace=ba5a5579-b0d2-4470-a106-82dc86c676cc
    -Dopensourceframework.services.group=opensources-framework-demo
    -Dopensourceframework.config.dataIds=opensourceframework-common.properties,opensourceframework-demo-center.properties
    说明: opensourceframework.service.server.namespace             nacos服务器namespace值
         opensourceframework.service.server.address               nacos服务器地址ip:port
         opensourceframework.config.dataIds                       nacos config配置名称
         opensourceframework.env.logger.workdir                   日志存放目录
         

    2 命令行启动:参数说明
    Xms/Xmx 大小通常配置为总机器内存的3/5   
    Xmn配置为Xmx大小的3/8  
    MetaspaceSize/MaxMetaspaceSize元数据空间为256m或者512m
    -XX:+HeapDumpOnOutOfMemoryError 设置当首次遭遇内存溢出时导出此时堆中相关信息
    -XX:HeapDumpPath=/tmp/heapdump.hprof 指定导出堆信息时的路径或文件名
    具体命令:
    java -server -Xms2g -Xmx2g -Xmn1g -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=256m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp/heapdump.hprof
    -Dopensourceframework.server.port=8082 -Dopensourceframework.profiles.active=dev -Dopensourceframework.env.logger.version=1.0.0 -Dopensourceframework.env.logger.workdir=/Users/yuce/work/data/logs \
    -Dopensourceframework.env.logger.level=info -Dopensourceframework.env.module=opensources-demo-center -Dnacos.service.namespace=c99551cc-3877-48cb-8d80-e3261c4443d6 \
    -Dnacos.service.address=127.0.0.1:8848 -Dnacos.service.version=1.0.0 -Dnacos.service.dataIds=opensources-demo-center.properties,opensources-framework-bases.properties  \
    -jar opensources-demo-center-boot.jar

## 工程总体结构说明
    工程总体结构如下图示，通常会包含api、biz、boot等三个核心子模块:
        |-- opensourceframework-stock-center
            |-- opensourceframework-stock-center-api
            |-- opensourceframework-stock-center-biz
            |-- opensourceframework-stock-center-boot
            
        子模块职责说明:
        ◆ api模块:     接口服务暴露模块，对外暴露具体的服务接口。
        ◆ biz模块:     核心业务代码的模块。该模块下包含apiimpl、service、dao、rest(可选)三个包。
        ◆ boot模块: 项目入口，     

        1.api模块工程结构:
            1.1 包结构:
                [xxx] 为具体的中心名称
                |-- com.opensourceframework.center.xxxx.api
                    |-- constants
                        |-- XxxxConstants.java
                        |-- XxxxEnum.java
                        |-- ......    
                    |-- dto
                        |-- request
                            |-- XxxReqDto.java
                            |-- .....
                        |-- response
                            |-- XxxRespDto.java
                            |-- .....
                    |-- exception
                            |-- XxxxException.java
                            |-- ....
                    |-- query (查询类api)
                        |-- IXxxxQueryApi.java
                        |-- .....
                    |-- op  (操作类api,比如:保存、修改、删除等)    
                        |-- IXxxxApi.java
                        |-- .....
                有多个子业务时,包结构需区别子业务
                |-- com.opensourceframework.center.xxxx.api
                    |-- constants(optional)
                        |-- aaaa 子业务名
                            |-- AaaaConstants.java
                            |-- AaaaEnum.java
                        |-- bbbb 子业务名
                            |-- ......    
                    |-- dto
                        |-- request
                             |-- aaaa 子业务名
                                 |-- XxxReqDto.java 
                        |-- response
                             |--- aaaa
                                 |-- XxxRespDto.java
                    |-- exception(optional)
                    |-- query (查询类api)
                        |-- aaaa 子业务名
                             |-- IAaaaQueryApi.java
                        |-- bbbb 子业务名
                             |-- .....
                    |-- op  (操作类api,比如:保存、修改、删除等)    
                        |-- aaaa 子业务名
                             |-- IAaaaApi.java 
                        |-- bbbb 子业务名
                             |-- IBbbbApi.java    
            2.biz模块工程结构:
                2.1 包结构:
                    |-- com.opensourceframework.center.stock.biz
                        |-- apiimpl           若同一业务需同时实现RestFull和Rpc服务 可省略rest 将混合实现放在apiipml包
                            |-- query
                            |-- XxxApiImpl.java
                            子业务很多时,需要分子包,可采用如下写法
                            |--query
                                |-- aa 子业务包名
                                    |-- XxxxQueryApiImpl
                            |-- op
                                |-- aa
                                    |-- XxxApiImpl.java       
                        |-- rest（optional）
                        |-- service
                    |-- dao		
                        |-- eo
                            |-- XxxEo.java 实体类
                        |-- mapper
                            |-- XxxMapper  mybatis mapper  
                        |-- XxxDao   为简化Dao层,无需编写接口类                         
                    |-- mq (optional)
                        |-- constants  常量
                        |-- handler    消息处理
                        |-- processor  消息订阅
                        |-- registryvo 消息配置vo
                        |-- sender     消息发送
                    |-- cache (optional)
                        |-- XxxCache  某业务数据的缓存统一管理  供 service/apiimpl包的实现类调用 不允许在service/apiimpl/dao中直接调用基类的方法进行缓存操作                     	
                    子业务很多时,需要分子包,可采用如下写法 
                    |-- com.opensourceframework.center.stock.biz
                        |-- apiimpl           若同一业务需同时实现RestFull和Rpc服务 可省略rest 将混合实现放在apiipml包
                            |-- query
                                |-- aaaa
                                    |-- AaaaQueryApiImpl.java
                                |-- bbbb
                                    |-- ......    
                            |-- op
                                |-- aaaa
                                    |-- AaaaApiImpl.java 
                                |-- bbbb
                                    |-- BbbbApiImpl.java          
                        |-- service	
                            |--aaaa
                                |-- impl
                                    |-- AaaaServiceImpl.java
                                    |-- AaaaTwoServiceImpl.java
                                |-- IAaaaService.java
                                    IAaaaTwoService.java
                            |-- bbbb             
                        |-- dao		
                            |-- eo
                                |-- aaaa子业务包名
                                    |-- AaaaOneEo.java 实体类
                                    |-- AaaaTwoEo.java
                                |-- bbbb
                                    |-- ..............    
                            |-- mapper
                                |-- aaaa
                                    |-- AaaaMapper.java  mybatis mapper  
                                |-- bbbb
                                    |-- ...............    
                            |-- repository
                                |-- aaaa       
                                    |-- AaaaDao.java   为简化Dao层,无需编写接口类  
                                |-- bbbb
                                    |-- ................                          
                        |-- mq (optional)
                            |-- constants  常量
                            |-- handler    消息处理
                            |-- processor  消息订阅
                            |-- registryvo 消息配置vo
                            |-- sender     消息发送
                        |-- cache (optional)
                            |-- XxxCache  某业务数据的缓存统一管理  供 service/apiimpl包的实现类调用 不允许在service/apiimpl/dao中直接调用基类的方法进行缓存操作                     	                   

                ◆ apiimpl:该包用于存放api模块中的接口实现类，
                ◆ service:该包用于存放核心业务逻辑的类代码。
                ◆ dao:    该包用于存放核心业务数据处理类代码。
                ◆ mq:     mq消息处理。
                ◆ cache:  业务缓存处理.  
                
                |-- reources
                    |-- com.opensourceframework.center.stock.mybatis.mapper
                        |-- xxx.xml
                资源文件夹 resources (optional),存放 mybatis mapper 映射xml文件 
                存放路径和bootstrap.yml中的mybatis.mapperLocations值对应，也可配置在nacos中的dataId:opensourceframework-stock-center.properties中
                例如:
                # bootstrap.yml
                mybatis:
                    #指定mybatis xml映射文件的资源路径 多个,隔开
                    mapperLocations: classpath*:com/opensourceframework/center/stock/mybatis/mapper/*.xml
                    #指定mybatis 类型别名
                    typeAliasesPackage:
                    #指定mybatis 配置文件的资源路径
                    configLocation:
                
        3.boot模块工程结构:
            3.1 包结构:
                |-- config
                    |-- org.opensourceframework.center.demo.member.boot.config.RootContextConfig.java
                    |-- CenterDataMapperConfig.java
                    |-- CacheConfig.java
                    |-- XxxConfig.java
                |-- XxxCenterBoot.java			
                
                ◆ config:各种配置 
                ◆ com.opensourceframework.center.stock.XxxCenterBoot   中心启动类
                ◆ CenterDataMapperConfig.java
                   @MapperScan 需修改为中心对应的Mapper目录
                ◆ RootContextBoot类
                   公共Bean、拦截器、公共处理等
                ◆ XxxCenterBoot类
                   中心启动类
                   @SpringBootApplication
                   @EnableDiscoveryClient        服务注册与发现开启
                   @ComponentScan({"org.opensourceframework"})  Spring扫描注解注入路径 
            
            3.2 resources说明
                application-xxx.yml文件说明,只配置基本的配置,大部分配置在nacos中
                application-dev.yml  开发环境文件
                application-test.yml 测试环境文件
                application-prod.yml 生产环境配置文件
                   
                swagger 的 basepackage的配置，需修改为rest包所在的全限定路径。
                swagger 的 title属性的配置:将 xxx 修改为对应中心的中文名。

       
       
## Nacos Config配置
    Data Id:opensourceframework-common.properties
    Group:DEFAULT_GROUP(默认配置)
    配置格式:Properties

    # MQ连接配置
    #opensourceframework.mq.config.registryvo.nameSrvAddr=192.168.0.1:9876;192.168.0.2:9876
    opensourceframework.mq.config.registryvo.nameSrvAddr=10.53.157.118:10086
    opensourceframework.mq.config.registryvo.accessKey=
    opensourceframework.mq.config.registryvo.secretKey=
    opensourceframework.mq.config.registryvo.producerId=PID_DEMO_CENTER
    opensourceframework.mq.config.registryvo.consumeThreadNums=5
    # 是否读取数据配置的消息订阅配置 默认true
    opensourceframework.mq.topic.registryvo.enableDBConfig=false
    
    # Mongodb 配置
    #mongodb 连接的collection名称
    #opensourceframework.mongodb.registryvo.database=opensources-demo-center
    # 是否需要权限访问 默认false
    #opensourceframework.mongodb.registryvo.needAuth=true
    #opensourceframework.mongodb.registryvo.user=mq_user
    #opensourceframework.mongodb.registryvo.passWord=mq#pr0duser
    #opensourceframework.mongodb.registryvo.serverAddresses=10.0.22.73:27017
    # 数据库允许的最大并发数量
    #opensourceframework.mongodb.registryvo.connectionsPerHost=1000
    # 每个连接上可以排队等待的线程数量
    #opensourceframework.mongodb.registryvo.threadsAllowedToBlockForConnectionMultiplier=5
    # 一个线程访问数据库的时候，在成功获取到一个可用数据库连接之前的最长等待时间
    #opensourceframework.mongodb.registryvo.maxWaitTime=120000
    # 与数据库建立连接的timeout, Default  10000毫秒
    #opensourceframework.mongodb.registryvo.connectTimeout=120000
    # 数据库连接读取和写入数据的timeout,0 表示不设置超时
    #opensourceframework.mongodb.registryvo.socketTimeout=0
    #opensourceframework.mongodb.registryvo.sslEnabled=false
    
    #RestClent 连接配置
    # 连接池的最大连接数默认为50
    opensourceframework.httpclient.registryvo.maxTotal=50
    # 分配给同一个route(路由)最大的并发连接数 默认100
    opensourceframework.httpclient.registryvo.maxPerRoute=100
    # 读取数据超时(等待响应超时) 默认30秒
    opensourceframework.httpclient.registryvo.readTimeout=5000
    # 客服端发送请求到与目标url建立起连接的最大时间 默认8秒
    opensourceframework.httpclient.registryvo.connectTimeout=4000
    # 从连接池中获取可用连接超时时间 默认5秒
    opensourceframework.httpclient.registryvo.conReqTimeOut=5000
    
    #Redis 连接配置
    opensourceframework.redis.registryvo.addresses=192.168.2.92:7100
    opensourceframework.redis.registryvo.livetime=86370
    opensourceframework.redis.registryvo.authPwd=opensourceframework
    opensourceframework.redis.registryvo.maxIdle=300
    opensourceframework.redis.registryvo.maxTotal=500
    opensourceframework.redis.registryvo.workModel=single

    Data Id:opensourceframework-demo-center.properties
    Group:opensourceframework-demo-center
    配置格式:Properties
   
    # 数据库连接配置
    opensourceframework.mybatis.registryvo.validationQuery=SELECT 1
    opensourceframework.mybatis.registryvo.driverClassName=com.mysql.jdbc.Driver
    opensourceframework.mybatis.registryvo.jdbcUrl=jdbc:mysql://127.0.0.1:3306/opensources-demo-center?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true
    opensourceframework.mybatis.registryvo.maxWait=60000
    opensourceframework.mybatis.registryvo.jdbcUserName=root
    opensourceframework.mybatis.registryvo.jdbcUserPassword=opensourceframework@123456
    opensourceframework.mybatis.registryvo.maxActive=10
    opensourceframework.mybatis.registryvo.initialSize=5
    opensourceframework.mybatis.registryvo.minIdle=0
    
    # MQ消息订阅配置
    mq.demouser.subscribe.registryvo.topic=DEMO_TOPIC
    mq.demouser.subscribe.registryvo.tag=DEMO_TAG
    mq.demouser.subscribe.registryvo.consumer=GID_DEMO_CONSUMER
         
## 命名规范
    1.API接口类名命名规范:
        ◆ 接口以单词 [I] 开头，以单词 [Api] 结尾；实现类以 [Impl] 结尾；
        ◆ 查询接口、增删改接口分开，查询接口在单词 [Api] 前加 [Query] 标识该接口是个查询接口。
            
        示例:
        IDemoApi           --    接口以单词 [I] 为前缀，以单词 [Api] 结尾
        DemoApiImpl        --    实现类以 [Impl] 结尾（不用前缀“I”）
        IDemoQueryApi      --    查询接口，在 [Api] 前加 [Query]
        DemoQueryApiImpl   --    查询接口实现类以 [Impl] 结尾
        
    2.API接口方法命名规范:
        接口名称采用驼峰格式命名方式，方法名称要准确表达该方法所处理的业务逻辑。
        ◆ 接口返回 
           RestFull/RPC服务,返回对象一律为Dto对象,禁止返回Eo对象,即XxxApiImpl、XxxRest类中的方法
           中心内部service/dao接口,返回对象一律为Eo/Vo对象,即XxxServiceImpl、XxxxDao类中的方法
        ◆ 查询接口 
            示例:
            RestResponse<DemoDto> findById(Long id);    //根据ID查询仓库
            RestResponse<PageInfo<DemoDto>> findByPage(WarehouseQueryDto warehouseQueryDto);    //分页查询仓库列表  
		
		◆ 查询接口  - 以单词 [find] 为前缀。
        ◆ 新增接口  - 以单词 [add] 为前缀。
        ◆ 修改接口  - 以单词 [update] 为前缀。
        ◆ 删除接口  - 以单词 [del] 为前缀。
        ◆ 增/删接口 - 以单词 [save] 为前缀。
        
    3.接口返回类型:
        所有服务接口都返回类型都应是 RestResponse:
        示例:
        RestResponse<Void> update(DeamoDto demoDto);                           //RestResponse<Void> 表示该接口的返回类型为 void
        RestResponse<List<DeamoDto>> findByOrgId(Long orgId);                  //RestResponse<List<DeamoDto>> 表示该接口的返回类型是 DeamoDto类型的List
        
    4.代码注释:
        类注释、方法注释是生成 javaDoc 的前提条件，所有的接口、实体都必须添加规范的代码注释。
        ◆ 版权声明:	
        
        ◆ 类注释:
            示例:
            /**
             * 仓库增删改操作服务接口
             *
             * @author yuce
             * @since 1.0.0
             * 
             */
            
        ◆ 方法注释:
            示例:
            /**
             * 新增仓库数据(内容包括:仓库信息)
             *
             * @param eo 仓库实体
             * @return 新增数据的ID
             */