package org.opensourceframework.starter.swagger;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * Swagger 配置类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@RefreshScope
@EnableSwagger2
@ConfigurationProperties(prefix = "opensourceframework.swagger")
@Profile({"dev", "fat", "fat2", "dev2"})
public class SwaggerConfigure {
    private static final Logger logger = LoggerFactory.getLogger(SwaggerConfigure.class);
    private Boolean enable = false;
    private String title = "Opensources-Framework API";
    private String description = "Opensources-Framework API API";
    private String basePackage = "org.opensourceframework";
    private String version = "1.0.0";
    private String baseUrl;

    public SwaggerConfigure() {
    }

    @Bean
    public Docket createRestApi() {
        ParameterBuilder aParameterBuilder = new ParameterBuilder();
        aParameterBuilder.name("Application-Key").description("应用实例Key").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        ParameterBuilder aParameterBuilder1 = new ParameterBuilder();
        aParameterBuilder1.name("Access-Token").description("访问令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        List<Parameter> aParameters = new ArrayList();
        aParameters.add(aParameterBuilder.build());
        aParameters.add(aParameterBuilder1.build());
        return (new Docket(DocumentationType.SWAGGER_2)).enable(this.enable).host(this.baseUrl).globalOperationParameters(aParameters).apiInfo(this.apiInfo()).select().apis(this.basePackage(this.basePackage)).paths(PathSelectors.any()).build();
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("Opensources-Framework产品研发中心" , null , null);
        return (new ApiInfoBuilder()).title(this.title).description(this.description).termsOfServiceUrl("http://").contact(contact).version("1.0.0").build();
    }

    public Predicate<RequestHandler> basePackage(final String basePackage) {
        return new Predicate<RequestHandler>() {
            @Override
            public boolean apply(RequestHandler input) {
                Boolean flag = (Boolean)SwaggerConfigure.declaringClass(input).transform(SwaggerConfigure.this.handlerPackage(basePackage)).or(true);
                logger.info("input:{},flag:{}" , input , flag);
                return flag;
            }
        };
    }

    private Function<Class<?>, Boolean> handlerPackage(final String basePackage) {
        return new Function<Class<?>, Boolean>() {
            @Override
            public Boolean apply(Class<?> input) {
                String[] subPackage = basePackage.split(",");
                int length = subPackage.length;

                for(int i = 0; i < length; ++i) {
                    String strPackage = subPackage[i];
                    boolean isMatch = input.getPackage().getName().startsWith(strPackage);
                    if (isMatch) {
                        return true;
                    }
                }

                return false;
            }
        };
    }

    private static Optional<? extends Class<?>> declaringClass(RequestHandler input) {
        return Optional.fromNullable(input.declaringClass());
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBasePackage() {
        return this.basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBaseUrl() {
        return this.baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }
}

