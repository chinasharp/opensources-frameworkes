package org.opensourceframework.gateway.demo.boot.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 打印API的执行时间
 *
 * GatewayFilter 特定的Filter
 * GobalFilter 全局Filter
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2021/2/5 下午5:10
 */
@Slf4j
@Component
public class TimerFilter implements GatewayFilter , Ordered {
	/**
	 * Process the Web request and (optionally) delegate to the next {@code WebFilter}
	 * through the given {@link GatewayFilterChain}.
	 *
	 * @param exchange the current server exchange
	 * @param chain    provides a way to delegate to the next filter
	 * @return {@code Mono<Void>} to indicate when request processing is complete
	 */
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start(exchange.getRequest().getPath().toString());

		return chain.filter(exchange).then(
				Mono.fromRunnable(() -> {
					stopWatch.stop();
					log.info(stopWatch.prettyPrint());
				})
		);
	}

	/**
	 * Get the order value of this object.
	 * <p>Higher values are interpreted as lower priority. As a consequence,
	 * the object with the lowest value has the highest priority (somewhat
	 * analogous to Servlet {@code load-on-startup} values).
	 * <p>Same order values will result in arbitrary sort positions for the
	 * affected objects.
	 *
	 * @return the order value
	 * @see #HIGHEST_PRECEDENCE
	 * @see #LOWEST_PRECEDENCE
	 */
	@Override
	public int getOrder() {
		return 0;
	}
}
