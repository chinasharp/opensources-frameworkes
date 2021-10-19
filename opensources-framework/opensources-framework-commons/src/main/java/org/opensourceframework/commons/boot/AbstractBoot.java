package org.opensourceframework.commons.boot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * SpringBoot启动扩展类
 *
 * @author yu.ce@foxmail.com
 */
public abstract class AbstractBoot {
	private static final Logger logger = LoggerFactory.getLogger(AbstractBoot.class);

	private final Class<?> source;
	private String[] args;
	private Banner.Mode bannerMode;

	public AbstractBoot(Class<?> source, String... args){
		this.source = source;
		this.args = args;
	}

	public AbstractBoot(Class<?> source,Banner.Mode bannerMode , String... args){
		this.source = source;
		this.bannerMode = bannerMode;
		this.args = args;
	}

	public ConfigurableApplicationContext run(String... args) throws Exception {
		execute();
		ConfigurableApplicationContext applicationContext = null;
		if(args != null && args.length > 0){
			this.args = args;
		}

		try {
			SpringApplication application = new SpringApplication(this.source);
			if(bannerMode == null){
				bannerMode = Banner.Mode.CONSOLE;
			}
			application.setBannerMode(bannerMode);
			applicationContext = application.run(this.args);
		}catch (NoSuchMethodError e){
			e.printStackTrace();
			applicationContext = new SpringApplicationBuilder(source).run(this.args);
		}
		return applicationContext;
	}

	/**
	 * 可供Spring Boot启动时 增加业务逻辑和配置初始化等操作
	 *
	 * @throws Exception
	 */
	public abstract void execute() throws Exception;
}
