### opensources-framework-businesscenter    业务中台基础结构demo
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
    
   
        