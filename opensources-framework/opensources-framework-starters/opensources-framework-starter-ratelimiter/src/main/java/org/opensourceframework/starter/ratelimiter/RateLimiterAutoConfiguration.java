package org.opensourceframework.starter.ratelimiter;

import org.opensourceframework.starter.ratelimiter.helper.RateLimiterHelper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2021/2/2 下午5:56
 */
@Component
public class RateLimiterAutoConfiguration implements CommandLineRunner {
	/**
	 * Callback used to run the bean.
	 *
	 * @param args incoming main method arguments
	 * @throws Exception on error
	 */
	@Override
	public void run(String... args) throws Exception {
		RateLimiterHelper.loadLuaScript();
	}
}
