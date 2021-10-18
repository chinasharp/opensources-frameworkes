springboot框架引入审计日志组件步骤:

    1.引入surk依赖
        <dependency>
            <groupId>org.opensourceframework</groupId>
            <artifactId>opensources-framework-starter-seclog</artifactId>
            <version>1.0.0-SNAPSHOT</version>
        </dependency>
        
    2.properties配置中加入mq配置（根据环境修改具体地址）：
        opensourceframework.mq.config.registryvo.nameSrvAddr=10.53.156.243:9876
        
    3.在需要记录审计日志的Controller或者Service的方法上加入注解 @SecLog，如：
        // 根据自己的需要，分别填写appName、functionName、operateName
        @SecLog(appName = "console", functionName= FunctionNameConstant.APPROVED_RESULTS, operateName = OperateNameConstant.SELECT)
        

        
spring框架引入审计日志组件步骤:

    1.引入surk依赖
        <dependency>
            <groupId>org.opensourceframework</groupId>
            <artifactId>opensources-framework-component-seclog</artifactId>
            <version>1.0.0-SNAPSHOT</version>
        </dependency>
        
    2.在spring-context.xml配置文件中注入bean：
        <bean class="SecLogConfig"/>
        
    3.properties配置中加入mq配置（根据环境修改具体地址）：
        opensourceframework.mq.config.registryvo.nameSrvAddr=10.53.156.243:9876
        
    4.在需要记录审计日志的Controller或者Service的方法上加入注解 @SecLog，如：
        // 根据自己的需要，分别填写appName、functionName、operateName
        @SecLog(appName = "console", functionName= FunctionNameConstant.APPROVED_RESULTS, operateName = OperateNameConstant.SELECT)
        
