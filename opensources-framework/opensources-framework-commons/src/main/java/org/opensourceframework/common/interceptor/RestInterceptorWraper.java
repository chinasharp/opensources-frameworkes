package org.opensourceframework.common.interceptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 默认实现
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class RestInterceptorWraper implements RestInterceptor {
    protected List<String> includePaths = new ArrayList();
    protected List<String> excludePaths = new ArrayList();

    public RestInterceptorWraper() {
    }

    @Override
    public String[] includePath() {
        return this.includePaths.toArray(new String[this.includePaths.size()]);
    }

    @Override
    public String[] excludePath() {
        return this.excludePaths.toArray(new String[this.excludePaths.size()]);
    }

    @Override
    public void addIncludePaths(String... paths) {
        this.includePaths.addAll(Arrays.asList(paths));
    }

    @Override
    public void addExcludePaths(String... paths) {
        this.excludePaths.addAll(Arrays.asList(paths));
    }
}
