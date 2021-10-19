package org.opensourceframework.gateway.demo.boot.cofig;

import org.opensourceframework.gateway.demo.boot.custom.TimerFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2021/2/4 下午5:43
 */
@Configuration
public class GateWayConfig {
	@Autowired
	private TimerFilter timerFilter;

	@Bean
	public RouteLocator routeLocator(RouteLocatorBuilder builder){
		return builder.routes()
				/** rout id 和 yml文件中 id相同可正常注入 **/
				.route(predicate -> predicate.path("/java-opensources-user-application/**")
						.filters(filter -> filter.stripPrefix(1))
						.uri("lb://opensources-user-application")
						.id("opensources-user-application"))
				.route(predicate -> predicate.path("/java-opensources-user-application/**")
						// 指定时间之后生效
						//.and().after()
						// 指定 时间A -- 时间B 之间生效
						//.and().between()
						.filters(filter -> filter.stripPrefix(1)
								.filter(timerFilter))
						.uri("lb://opensources-user-center")
						.id("java-opensources-user-center"))
				.build();
	}


}
