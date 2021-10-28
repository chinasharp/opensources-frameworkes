${md_file_char} 快速启动
    1 IDE启动,VM options设置参数:
    -Djava.library.path=java_native_library
    -Dopensourceframework.server.port=8082
    -Dopensourceframework.profiles.active=dev
    -Dopensourceframework.env.logger.workdir=./logs
    -Dopensourceframework.env.logger.level=debug
    -Dopensourceframework.service.version=1.0.0
    -Dopensourceframework.env.module=${rootArtifactId}
    -Dopensourceframework.service.server.username=nacos
    -Dopensourceframework.service.server.password=nacos
    -Dopensourceframework.service.server.address=127.0.0.1:8848
    -Dopensourceframework.service.server.namespace=ba5a5579-b0d2-4470-a106-82dc86c676cc
    -Dopensourceframework.services.group=opensources-framework
    -Dopensourceframework.config.dataIds=opensourceframework-common.properties,${rootArtifactId}.properties
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
    -Dopensourceframework.env.logger.level=info -Dopensourceframework.env.module=${rootArtifactId} -Dopensourceframework.service.server.namespace=ba5a5579-b0d2-4470-a106-82dc86c676cc \
    -Dopensourceframework.service.server.username=nacos  -Dopensourceframework.service.server.password=nacos  -Dopensourceframework.service.server.address=10.53.157.115:8848 \
    -Dopensourceframework.service.version=1.0.0 -Dopensourceframework.config.dataIds=opensourceframework-common.properties,${rootArtifactId}.properties  \
    -jar ${rootArtifactId}-boot.jar


${md_file_char} module说明
    ${rootArtifactId}-api               应用(聚合)服务Api模块
    ${rootArtifactId}-biz               应用(聚合)服务实现模块
    ${rootArtifactId}-boot              应用(聚合)服务启动模块

${md_file_char} 工程总体结构说明
    工程总体结构如下图示，通常会包含api、biz、boot等三个核心子模块：
        |-- ${rootArtifactId}
            |-- ${rootArtifactId}-api
            |-- ${rootArtifactId}-biz
            |-- ${rootArtifactId}-boot
            
        子模块职责说明：
        ◆ ${rootArtifactId}-api模块：     接口服务暴露模块，对外暴露具体的服务接口。
        ◆ ${rootArtifactId}-biz模块：     核心业务代码的模块。该模块下包含ctrl、service、feignclients三个包。
        ◆ ${rootArtifactId}-boot模块：    项目启动模块    
        
        1.${rootArtifactId}-api模块工程结构：
            1.1 包结构：
                |-- ${package}.api
                    |-- constants
                    |-- dto
                        |-- request
                           |-- XxxReqDto.java 
                        |-- response
                           |-- XxxRespDto.java
                    |-- exception
                [xxx] 为具体的中心名称。
                多个子业务,需要增加子业务包
                |-- ${package}.api
                    |-- constants
                        |-- aaaa 子业务名
                            |-- AaaaConstants.java
                            |-- AaaaEnum.java
                        |-- bbbb 子业务名
                            |-- ......    
                    |-- dto
                        |-- request
                            |-- aaaa 子业务名
                                |-- AaaaReqDto.java
                                |-- ....
                            |-- bbbb
                                |-- BbbbReqDto.java
                                |-- ....
                        |-- response
                            |-- aaaa
                                |-- AaaaRespDto.java
                                |-- ....
                            |-- bbbb
                                |-- BbbbRespDto.java
                                |-- ....
                    |-- exception
                    |-- query (查询类api)
                        |-- aaaa 子业务名
                            |-- IAaaaQueryApi.java
                            |-- IXxxxQueryApi.java
                        |-- bbbb 子业务名
                            |-- IBbbbQueryApi.java
                            |-- ....
                    |-- op  (操作类api,比如:保存、修改、删除等)    
                        |-- aaaa 子业务名
                            |-- IAaaaApi.java
                            |-- ...
                        |-- bbbb 子业务名
                            |-- IBbbbApi.java
                            |-- ...
        
        2.${rootArtifactId}-biz模块工程结构：
            2.1 包结构：
                |-- ${package}.xxx.biz                 
                    |-- ctrl     SpringMVC action
                        |-- XxxController.java
                    |-- service	
                        |-- impl
                            |-- XxxServiceImpl.java  集合center的服务,完成业务
                        |-- IXxxService.java 
                    |-- feignclients
                        |-- callback   处理FeignService服务异常
                            |-- XxxFeignServiceFactory.java
                        |-- feignservice
                            |--  XxxFeignService.java
                        |-- FeignClientConfig.java   配置FeignClientService配置类      
                    |-- mq (可选)
                        |-- constants 常量
                        |-- handler
                        |-- processor
                    |-- cache （可选）
                        |-- XxxCache  某业务数据的缓存统一管理  供 service/apiimpl包的实现类调用 不允许在service/apiimpl/dao中直接调用基类的方法进行缓存操作                     	
                多个子业务时:
                |-- ${package}.xxx.biz                 
                    |-- ctrl     SpringMVC action
                        |-- aaaa
                            |-- XxxController.java
                            |-- .....
                        |-- bbbb
                            |-- ....
                    |-- service	
                        |-- aaaa
                            |-- impl
                                |-- XxxServiceImpl.java  聚合各业务center的服务,完成业务
                                |-- ...........
                            |-- IXxxService.java 
                            |-- I.......
                        |-- bbbb
                            |-- impl
                                |-- XxxServiceImpl.java  聚合各业务center的服务,完成业务
                                |-- ...........
                            |-- IXxxService.java 
                            |-- I.......
                    |-- feignclients
                        |-- callback   处理FeignService服务异常
                            |-- aaaa
                                |-- XxxxFeignServiceFactory.java
                                |-- ...
                            |-- bbbb
                                |-- .....
                        |-- feignservice
                            |-- aaaa
                                |--  XxxFeignService.java
                                |-- ....
                            |-- bbbb
                                |-- ....
                        |-- FeignClientConfig.java   配置FeignClientService配置类      
                    |-- mq (可选)
                        |-- constants  常量
                        |-- handler    消息处理 逻辑
                        |-- processor  消息订阅 逻辑
                        |-- registryvo 消息配置vo
                        |-- sender     消息发送
                    |-- cache （可选）
                        |-- XxxCache  某业务数据的缓存统一管理  供 service/apiimpl包的实现类调用 不允许在service/apiimpl/dao中直接调用基类的方法进行缓存操作                     	
 
                ◆ ctrl：   该包用于处理前端请求
                ◆ service：该包用于存放业务逻辑的类代码。
                ◆ feignclients：    该包用于存放FeignClientService服务消费者。
                ◆ mq：     mq消息处理。
                ◆ cache：  业务缓存处理.  
        3.${rootArtifactId}-boot模块工程结构：
            3.1 包结构：
                |-- ${package}.boot
                    |-- RootContextConfig.java
                    |-- CacheConfig.java
                    |-- XxxConfig.java
                |-- ApplicationBoot.java			
                
                ◆ config：各种配置 
                ◆ ${package}.boot.XxxApplicationBoot   中心启动类

${md_file_char} Nacos配置说明
    Data Id:${rootArtifactId}.properties
    Group:${rootArtifactId}
    配置格式:Properties
    配置内容:
    # 调用中心服务配置  aaaa为调用服务的业务域名称 比如调用user服务,即为provider.user.service.group=opensourceframework-user-center
    provider.${dependency-center-name}.service.group=opensourceframework-${dependency-center-name}-center
    provider.${dependency-center-name}.service.version=1.0.0
    provider.${dependency-center-name}.service.protocol=dubbo

${md_file_char} 命名规范
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
            RestResponse<DemoDto> queryById(Long id);    //根据ID查询仓库
            RestResponse<PageInfo<DemoDto>> queryByPage(WarehouseQueryDto warehouseQueryDto);    //分页查询仓库列表  
		
		◆ 查询类操作接口  - 以单词 [query] 为前缀。
        ◆ 保存类操作接口  - 以单词 [save] 为前缀。
        ◆ 更新类操作接口  - 以单词 [update] 为前缀。
        ◆ 删除类操作接口  - 以单词 [del] 为前缀。
        ◆ 其他类操作接口  - 以表明确切含义的单词为前缀。
        
    3.接口返回类型:
        所有服务接口都返回类型都应是 RestResponse:
        示例:
        RestResponse<Void> update(DeamoDto demoDto);                           //RestResponse<Void> 表示该接口的返回类型为 void
        RestResponse<List<DeamoDto>> queryByOrgId(Long orgId);                 //RestResponse<List<DeamoDto>> 表示该接口的返回类型是 DeamoDto类型的List
        
    4.代码注释:
        类注释、方法注释是生成 javaDoc 的前提条件，所有的接口、实体都必须添加规范的代码注释。
        ◆ 版权声明:	
        
        ◆ 类注释:
            示例:
            /**
             * 仓库增删改操作服务接口
             *
             * @author yu.ce@foxmail.com
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

