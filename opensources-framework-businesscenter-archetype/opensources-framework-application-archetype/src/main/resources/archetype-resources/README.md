boot module说明
    
    1. application-xxx.yml文件说明,只配置基本的配置,大部分配置在nacos中
       application-dev.yml  开发环境文件
       application-test.yml 测试环境文件
       application-prod.yml 生产环境配置文件
       
       swagger 的 basepackage的配置，需修改为rest包所在的全限定路径。
       swagger 的 title属性的配置：将 xxx 修改为对应中心的中文名。
    2. CenterDataMapperConfig类：
       @MapperScan 需修改为中心对应的Mapper目录
    3. RootContextBoot类
       公共Bean、拦截器、公共处理等
    4. XxxCenterBoot类
       中心启动类
       @SpringBootApplication
       @EnableDiscoveryClient        服务注册与发现开启
       @ComponentScan({"org.opensourceframework"})  Spring扫描注解注入路径 
       
Nacos配置说明

    Data Id:${rootArtifactId}.properties
    Group:${rootArtifactId}
    配置格式:Properties
    配置内容:
    # 调用中心服务配置
    provider.demo.service.group=opensources-${bizName}-center
    provider.demo.service.version=1.0.0-SNAPSHOT
    provider.demo.service.protocol=dubbo


启动说明

    1.IDE启动,VM options设置参数:
    -Dopensourceframework.server.port=8082
    -Dspring.profiles.active=dev
    -Dopensourceframework.env.logger.workdir=/Users/yuce/work/data/logs
    -Dopensourceframework.env.logger.level=info
    -Dnacos.server.username=codeuser
    -Dnacos.server.password=codeuser3321
    -Dnacos.server.namespace=233bc25b-3177-4f78-86e7-2cc90f8bde26
    -Dnacos.server.address=aznacosof.woaizuji.com:80
    -Dnacos.config.dataIds=common.properties,${rootArtifactId}.properties
    
    说明:${rootArtifactId}.properties为本应用配置,若需公共配置,比如mongoDB连接 、MQ连接 、Redis连接等

    2.命令行启动：参数说明
    Xms/Xmx 大小通常配置为总机器内存的3/5   
    Xmn配置为Xmx大小的3/8  
    MetaspaceSize/MaxMetaspaceSize元数据空间为256m或者512m
    -XX:+HeapDumpOnOutOfMemoryError 设置当首次遭遇内存溢出时导出此时堆中相关信息
    -XX:HeapDumpPath=/tmp/heapdump.hprof 指定导出堆信息时的路径或文件名
    具体命令：
    java -server -Xms2g -Xmx2g -Xmn1g -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=256m   \
    -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp/heapdump.hprof   \
    -Dopensourceframework.server.port=8082  \ 
    -Dopensourceframework.profiles.active=dev  \
    -Dopensourceframework.env.logger.version=1.0.0    \
    -Dopensourceframework.env.logger.workdir=/Users/yuce/work/data/logs \
    -Dopensourceframework.env.logger.level=info   \
    -Dopensourceframework.env.module=${rootArtifactId}   \
    -Dopensourceframework.service.namespace=c99551cc-3877-48cb-8d80-e3261c4443d6 \
    -Dopensourceframework.service.address=10.53.157.115:8848   \
    -Dopensourceframework.config.group=${rootArtifactId}  \
    -Dopensourceframework.config.dataIds=${rootArtifactId}.properties,opensources-framework-bases.properties  \
    -jar ${rootArtifactId}-boot.jar


工程总体结构说明

    工程总体结构如下图示，通常会包含api、biz、boot等三个核心子模块：
        |-- ${rootArtifactId}
            |-- ${rootArtifactId}-api
            |-- ${rootArtifactId}-biz
            |-- ${rootArtifactId}-boot
            
        子模块职责说明：
        ◆ api模块：     接口服务暴露模块，对外暴露具体的服务接口。
        ◆ biz模块：     核心业务代码的模块。该模块下包含ctrl、service、feignclients三个包。
        ◆ boot模块： 项目入口，     
        
        1.api模块工程结构：
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
        
        2.biz模块工程结构：
            2.1 包结构：
                |-- ${package}.xxx.biz                 
                    |-- ctrl     SpringMVC action
                        |-- XxxController.java
                    |-- service	
                        |-- impl
                            |-- XxxServiceImpl.java  集合center的服务,完成业务
                        |-- IXxxService.java 
                    |-- feignclients
                        |-- feignservice
                            |--  XxxFeignService.java
                        |-- FeignClientConfig.java   配置FeignClientService配置类      
                    |-- mq (可选)
                        |-- constants 常量
                        |-- handler
                        |-- processor
                    |-- cache （可选）
                        |-- XxxCache  某业务数据的缓存统一管理  供 service/apiimpl包的实现类调用 不允许在service/apiimpl/dao中直接调用基类的方法进行缓存操作                     	
        
                ◆ ctrl：   该包用于处理前端请求
                ◆ service：该包用于存放业务逻辑的类代码。
                ◆ feignclients：    该包用于存放FeignClientService服务消费者。
                ◆ mq：     mq消息处理。
                ◆ cache：  业务缓存处理.  
                
        3.boot模块工程结构：
            3.1 包结构：
                |-- config
                    |-- RootContextConfig.java
                    |-- CenterDataMapperConfig.java
                    |-- CacheConfig.java
                    |-- XxxConfig.java
                |-- ApplicationBoot.java			
                
                ◆ config：各种配置 
                ◆ ${package}.boot.XxxApplicationBoot   中心启动类
                
                
命名规范

    1.Service层接口类名命名规范：
        ◆ 接口以单词 [I] 开头；实现类以 [Impl] 结尾；接口以[Service]结尾
        ◆ 查询接口、增删改接口分开，查询接口在单词 [Api] 前加 [Query] 标识该接口是个查询接口。
            
        示例：
        IDemoService           --    接口以单词 [I] 为前缀，以单词 [Api] 结尾
        DemoServiceImpl        --    实现类以 [Impl] 结尾（不用前缀“I”）
        
    2.ctrl方法命名规范：
        接口名称采用驼峰格式命名方式，方法名称要准确表达该方法所处理的业务逻辑。
        ◆ 接口返回 
           Http请求方法返回类型都应是 RestResponse：
           示例：
           RestResponse<Void> update(DeamoDto demoDto);                           //RestResponse<Void> 表示该接口的返回类型为 void
           RestResponse<List<DeamoDto>> findByOrgId(Long orgId);                  //RestResponse<List<DeamoDto>> 表示该接口的返回类型是 DeamoDto类型的List
         
        ◆ 接口 和swagger  Request请求方法必须添加swagger说明
            示例：
            @GetMapping("/findById/{id}")
	        @ApiOperation(value="根据id查询Demo信息", notes="根据id查询Demo信息 id:主键id")
	        RestResponse<DemoRespDto> findById(@NotNull(message = "id不能为空") @PathVariable(name = "id") Long id);
		
		◆ 查询接口  - 以单词 [find] 为前缀。
        ◆ 新增接口  - 以单词 [add] 为前缀。
        ◆ 修改接口  - 以单词 [update] 为前缀。
        ◆ 删除接口  - 以单词 [del] 为前缀。
        ◆ 增/删接口 - 以单词 [save] 为前缀。
        
    3.代码注释：
        类注释、方法注释是生成 javaDoc 的前提条件，所有的接口、实体都必须添加规范的代码注释。
        ◆ 版权声明：	
        
        ◆ 类注释：
            示例：
            /**
             * 仓库增删改操作服务接口
             *
             * @author yuce
             * @since 1.0.0
             * 
             */
            
        ◆ 方法注释：
            示例：
            /**
             * 新增仓库数据(内容包括：仓库信息)
             *
             * @param eo 仓库实体
             * @return 新增数据的ID
             */
