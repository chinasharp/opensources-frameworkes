## opensources-framework
    开源业务中台基础框架

### Moudle说明
    opensources-framework-businesscenter    业务中台基础结构demo
        |- opensources-framework-businesscenter-archetype   业务中台脚手架,包含application(聚合服务)和center(业务域服务)
            |- opensources-framework-application-archetype  业务中台-Application脚手架
            |- opensources-framework-center-archetype       业务中台-Center脚手架
        |- opensources-framework-businesscenter-demos       业务中台demo
            |- opensources-demo-gateway                     业务中台-网关服务 demo
            |- opensources-demo-application                 业务中台-Application Demo
            |- opensources-demo-center                      业务中台-Center Demo
            说明:
            ◆ Demo中 服务注册中心与配置中心使用Nacos
            ◆ application调用center服务归位内服务使用dubbo rpc调用
            ◆ 外部请求通过Http RestFull(外服务)调用Application
            ◆ Application与Center的工程结构查看工程内的README.md
    
    opensources-framework-dependencies              开源框架JAR版本管理
    
    opensources-framework-base                      基础类库(不包含对开源框架的封装)
        |- org.opensourceframework.base
            |- app                                  应用相关基础类,可用于对各个应用的管理权限
            |- cache                                缓存的公共接口
            |- constants                            基本的常量
            |- db                                   查询实体类封装,类似于HQL的封装
            |- dto                                  基础DTO类
            |- eo                                   基础Entity类
            |- exception                            基础异常
            |- helper                               帮助工具类
            |- id                                   主键ID生成,雪花ID生成器、UUID生成器
            |- microservice
            |- rest
            |- threads
            |- vo

    opensources-framework-commons                   公共类库(包含对开源框架的封装) 

    opensources-framework-components                公共组件封装
        |- opensource-framework-component-dao       数据库操作封装,包含基于Mybatis实现的分表分库操作
        |- opensource-framework-component-es        ES操作封装
        |- opensource-framework-component-mongodb
        |- opensource-framework-component-httpclient
        |- opensource-framework-component-kafka
        |- opensource-framework-component-mq
        |- opensource-framework-component-redis
        |- opensource-framework-component-seclog

    

    opensources-framework-starters                  各种封装后的starter
        |- opensource-framework-starter-ehcache
        |- opensource-framework-starter-encdec
        |- opensource-framework-starter-es
        |- opensource-framework-starter-fastdfs
        |- opensource-framework-starter-kafka
        |- opensource-framework-starter-mixcache
        |- opensource-framework-starter-mongodb
        |- opensource-framework-starter-mq
        |- opensource-framework-starter-mybatis
        |- opensource-framework-starter-oss
        |- opensource-framework-starter-ratelimiter
        |- opensource-framework-starter-redis
        |- opensource-framework-starter-restclient
        |- opensource-framework-starter-seclog
        |- opensource-framework-starter-sentinel
        |- opensource-framework-starter-soapclient
        |- opensource-framework-starter-springcloud-nacos
        |- opensource-framework-starter-swagger
        