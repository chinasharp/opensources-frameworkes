springboot项目对接方式
1.添加依赖
<dependency>
    <groupId>org.opensourceframework</groupId>
    <artifactId>opensources-framework-starter-encdec</artifactId>
</dependency>
2.surk添加修改配置
  1).jasypt.decrypt.password=Oat4jkamo$dK
  2).将需要加密的密码替换为jasypt加密后的格式，如：ENC(eqnjaD614S57iS4ut+VegQ==)，括号内的为加密后的密码，由运维提供  
3.源码扩展 基于2.0.0版本 扩展点
EncryptablePropertyResolverConfiguration.EnvCopy类修改 解决ConfigurableEnvironment复制时丢失ActiveProfiles bug

    private static class EnvCopy {
        StandardEnvironment copy;

        EnvCopy(ConfigurableEnvironment environment) {
            copy = new StandardEnvironment();
            environment.getPropertySources().forEach(ps -> {
                PropertySource<?> original = ps instanceof EncryptablePropertySource ? ((EncryptablePropertySource)ps).getDelegate() : ps;
                copy.getPropertySources().addLast(original);
            });
        }

        ConfigurableEnvironment get() {
            return copy;
        }
    }

修改为

    private static class EnvCopy {
        StandardEnvironment copy;

        EnvCopy(ConfigurableEnvironment environment) {
            copy = new StandardEnvironment();
            environment.getPropertySources().forEach(ps -> {
                PropertySource<?> original = ps instanceof EncryptablePropertySource ? ((EncryptablePropertySource)ps).getDelegate() : ps;
                copy.getPropertySources().addLast(original);
            });
            copy.setActiveProfiles(environment.getActiveProfiles());
            copy.setDefaultProfiles(environment.getDefaultProfiles());
        }

        ConfigurableEnvironment get() {
            return copy;
        }   
    }


