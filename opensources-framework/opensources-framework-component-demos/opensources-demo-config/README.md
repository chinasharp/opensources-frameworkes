opensources-demo-config启动说明

    1.IDE的测试类中,VM options设置参数:
    -Dopensourceframework.config.env=dev
    -Dopensourceframework.env.module=demo-config
    -Dopensourceframework.config.enabled=true
    -Dopensourceframework.config.namespaces=application,OPERATION-ONE.opensources-framework-bases
    
springboot引入surk步骤:

    1.引入surk依赖
        <dependency>
            <groupId>com.wandaph</groupId>
            <artifactId>wandaph-surk-client</artifactId>
            <version>1.0.0-SNAPSHOT</version>
        </dependency>
        
    2.配置启动参数VM options说明：
        # surk的环境变量
        -Dopensourceframework.config.env=dev
        # surk对应的appId，可不配置，默认使用opensourceframework.env.module
        -Dopensourceframework.config.app.id=demo-config
        # 是否开启surk远程配置
        -Dopensourceframework.config.enabled=true
        # surk对应的namespaces，多个可","隔开
        -Dopensourceframework.config.namespaces=application,OPERATION-ONE.opensources-framework-bases

opensourceframework新框架接入surk步骤：

    1.引入surk依赖
        <dependency>
            <groupId>com.wandaph</groupId>
            <artifactId>wandaph-surk-client</artifactId>
            <version>1.0.0-SNAPSHOT</version>
        </dependency>
        
    2.bootstrap.yml关闭nacos的config配置：
          spring.cloud.nacos.config.enabled=false
         
      bootstrap.yml删除nacos的config配置：
          nacos.config.group: ${opensourceframework.config.group}
          nacos.config.namespace: ${opensourceframework.service.namespace}
          nacos.config.sharedDataIds: ${opensourceframework.config.dataIds}
          nacos.config.refreshableDataids: ${opensourceframework.config.dataIds}
          spring.cloud.nacos.config.server-addr: ${opensourceframework.service.address}
          spring.cloud.nacos.config.group: ${nacos.config.group}
          spring.cloud.nacos.config.namespace: ${nacos.service.namespace}
          spring.cloud.nacos.config.shared-data-ids: ${nacos.config.sharedDataIds}
          spring.cloud.nacos.config.refreshable-dataids: ${nacos.config.refreshableDataids}
        
    3.删除启动参数VM options：
        -Dopensourceframework.config.group=opensources-demo-center
        -Dopensourceframework.config.dataIds=opensources-demo-center.properties,opensources-framework-bases.properties
        
    4.新增启动参数VM options说明：
        # surk的环境变量
        -Dopensourceframework.config.env=dev
        # surk对应的appId，可不配置，默认使用opensourceframework.env.module,如果appId超长，可以使用opensourceframework.config.app.id
        -Dopensourceframework.config.app.id=opensources-demo-center
        # 是否开启surk远程配置
        -Dopensourceframework.config.enabled=true
        # surk对应的namespaces，多个可","隔开
        -Dopensourceframework.config.namespaces=application,OPERATION-ONE.opensources-framework-bases
       
    5.全部启动参数VM options汇总：
        -Dserver.host.ip=127.0.0.1(改为本机ip)
        -Dopensourceframework.server.port=8081
        -Dspring.profiles.active=dev
        -Dopensourceframework.env.logger.workdir=/Users/yuce/work/data/logs
        -Dopensourceframework.env.logger.level=info
        -Dopensourceframework.env.module=opensources-demo-center
        -Dopensourceframework.service.namespace=d39820ca-674f-403e-98ea-08ed10e8ef98
        -Dopensourceframework.service.address=10.53.157.115:8848
        -Dopensourceframework.service.version=1.0.0
        -Dopensourceframework.config.env=dev
        -Dopensourceframework.config.enabled=true
        -Dopensourceframework.config.namespaces=application,OPERATION-ONE.opensources-framework-bases
