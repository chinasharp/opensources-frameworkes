package org.opensourceframework.component.kafka.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

/**
 * Spring工具类
 * @since 1.0.0
 * @author yuce
 * 
 */
public class KafkaSpringHelper implements ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(KafkaSpringHelper.class);

    private static ApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        logger.info("注入ApplicationContext到SpringBeanHelper:{}", applicationContext);

        if (KafkaSpringHelper.applicationContext != null) {
            logger.warn("SpringContextHolder中的ApplicationContext被覆盖, 原有ApplicationContext为:{}",KafkaSpringHelper.applicationContext);
        }
        KafkaSpringHelper.applicationContext = applicationContext;
    }

    /**
     * 取得存储在静态变量中的ApplicationContext.
     *
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        assertContextInjected();
        return applicationContext;
    }

    /**
     * <p>
     * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
     * </p>
     *
     * @param name
     *            springbean 的id
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        assertContextInjected();
        return (T) applicationContext.getBean(name);
    }

    /**
     * <p>
     * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
     * </p>
     *
     * @param clazz
     * @return
     */
    public static <T> T getBean(Class<T> clazz){
        assertContextInjected();
        Map<String, ?> beans = applicationContext.getBeansOfType(clazz);

        T bean = null;
        if (beans != null) {
            bean = (T) beans.get(beans.keySet().iterator().next());
        }
        return bean;
    }

    /**
     * 通过name,以及Clazz返回指定的Bean
     *
     * @param name
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(String name, Class<T> clazz){
        assertContextInjected();
        return applicationContext.getBean(name, clazz);
    }

    /**
     * <p>
     * 从静态变量applicationContext中取得clazz类型bean的Map集合
     * </p>
     *
     * @param clazz
     * @return
     */
    public static Map<String, ?> getBeansOfType(Class<?> clazz) {
        assertContextInjected();
        Map<String, ?> beans = applicationContext.getBeansOfType(clazz);
        return beans;
    }


    /**
     * 实现DisposableBean接口,在Context关闭时清理静态变量.
     *
     */
    public void destroy() {
        KafkaSpringHelper.clear();
    }

    /**
     * <p>
     * 清除SpringBeanHelper中的ApplicationContext为Null.
     * </p>
     */
    public static void clear() {
        logger.debug("清除SpringBeanHelper中的ApplicationContext:{}",applicationContext);
        applicationContext = null;
    }

    /**
     * <p>
     * 检查ApplicationContext不为空.为空抛出异常:IllegalStateException
     * </p>
     */
    private static void assertContextInjected() {
        if (applicationContext == null) {
            throw new IllegalStateException("applicaitonContext未注入,请在spring.xml或注解中定义SpringBeanHelper");
        }
    }

}
