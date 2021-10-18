package org.opensourceframework.component.idempotent.interceptor;

import com.alibaba.fastjson.JSON;
import org.opensourceframework.base.exception.BizException;
import org.opensourceframework.base.helper.ReflectHelper;
import org.opensourceframework.component.idempotent.annotation.IdempotentHandler;
import org.opensourceframework.component.redis.cache.service.IRedisCacheService;
import org.opensourceframework.component.redis.lock.service.ILockService;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 访问限制aop
 *
 * @author 空见
 * @since 1.0.0
 * @date  2018/12/20 3:13 PM
 */
@Aspect
@Component
public class IdempotentHandlerAspect {
    private static final Logger logger = LoggerFactory.getLogger(IdempotentHandlerAspect.class);

    private static final String IDEMPOTENT_LOCK_CACHE_KEY = "idempotentLock:";
    private static final String IDEMPOTENT_DATA_CACHE_KEY = "idempotentData:";
    private static final String KEY_SPLIT_CHAR = ":";
    private static final String METHOD_PEREMETER_DEFAULR_VALUE = "parameterDefVal";
    private static final String PEREMETER_PROPERTY_DEFAULR_VALUE = "propertyDefVal";
    private static final String PROPERTY_SPLIT_CHAR = ",";

    /**
     *
     */
    private static final Long LOCK_EX_TIME = 300L;

    @Resource
    private IRedisCacheService redisCacheService;

    @Resource
    private ILockService lockService;

    @Pointcut("@annotation(org.opensourceframework.idempotent.annotation.IdempotentHandler)")
    public void idempotentHandlerAop() {
    }

    public IdempotentHandlerAspect() {
    }

    @Around("idempotentHandlerAop()")
    public Object idempotentHandle(ProceedingJoinPoint point) throws Throwable {
        Object resObj = null;

        Signature signature = point.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();
        Method realMethod = point.getTarget().getClass().getDeclaredMethod(signature.getName(), targetMethod.getParameterTypes());
        Map<String, Object> argsMap = this.getArgsMap(methodSignature.getParameterNames(), point.getArgs());

        IdempotentHandler idempotentHandler = realMethod.getAnnotation(IdempotentHandler.class);
        String parameter = idempotentHandler.parameter();
        if(StringUtils.isBlank(parameter)){
            parameter = point.getTarget().getClass().getName().concat(KEY_SPLIT_CHAR).concat(realMethod.getName());
        }
        String properties = idempotentHandler.properties();
        int waitTimeout = idempotentHandler.waitTimeout();
        int leaseTime = idempotentHandler.leaseTime();
        int validTime = idempotentHandler.validTime();
        Class clazz = ((MethodSignature) signature).getReturnType();

        // 参数值
        Object parameterVal = null;
        // 参数为对象时的属性值
        Object propertyVal = null;
        // 幂等锁的key
        String idempotentKey = "";

        if(MapUtils.isNotEmpty(argsMap) || StringUtils.isNotBlank(parameter)){
            idempotentKey = idempotentKey.concat(parameter);
            if(MapUtils.isNotEmpty(argsMap)) {
                parameterVal = argsMap.get(parameter);
            }

            if(parameterVal == null){
                logger.info("IdempotentHandler exec doing. parameter is null , use default value:{}" , "methodParameter");
                idempotentKey = idempotentKey.concat(KEY_SPLIT_CHAR).concat(METHOD_PEREMETER_DEFAULR_VALUE);
            }else{
                if(StringUtils.isNotBlank(properties)){
                    String[] propertyArray = properties.split(PROPERTY_SPLIT_CHAR);
                    for(String property : propertyArray) {
                        propertyVal = ReflectHelper.getFieldValue(parameterVal, property);
                        if (parameterVal == null) {
                            logger.info("IdempotentHandler exec doing. property's value is null , use default value:{}", "parameterProperty");
                            propertyVal = PEREMETER_PROPERTY_DEFAULR_VALUE;
                        }
                        idempotentKey = idempotentKey.concat(KEY_SPLIT_CHAR).concat(property).concat(KEY_SPLIT_CHAR).concat(propertyVal.toString());
                    }
                }else {
                    idempotentKey = idempotentKey.concat(KEY_SPLIT_CHAR).concat(parameterVal.toString());
                }
            }

            //幂等数据缓存key
            String idempotentDataKey = IDEMPOTENT_DATA_CACHE_KEY.concat(idempotentKey);
            boolean isGainLock = false;
            try {
                isGainLock = lockService.lock(IDEMPOTENT_LOCK_CACHE_KEY, idempotentKey, waitTimeout, leaseTime, TimeUnit.SECONDS);
                if (isGainLock) {
                    //获取是否有幂等数据
                    String cahceData = redisCacheService.get(idempotentDataKey);
                    if(StringUtils.isNotBlank(cahceData)){
                        resObj = JSON.parseObject(cahceData , clazz);
                        logger.info("get data from idempottent cahce:{}", cahceData);
                    }else{
                        try {
                            resObj = point.proceed();
                        } catch (Throwable throwable) {
                            resObj = new BizException(throwable.getMessage());
                        }
                        redisCacheService.set(idempotentDataKey, resObj, validTime);
                        logger.info("get data from  point proceed. resData:{}",JSON.toJSONString(resObj));
                    }
                }else {
                    logger.warn("IdempotentHandle not cant get lock!" );
                    resObj = null;
                }
            }finally {
                if(isGainLock) {
                    lockService.unlock(IDEMPOTENT_LOCK_CACHE_KEY, idempotentKey);
                }
            }
        }else {
            resObj = point.proceed();
        }
        logger.info("IdempotentHandle result data : {}" , JSON.toJSONString(resObj));
        return resObj;
    }

    private Map<String, Object> getArgsMap(String[] paramsNames, Object[] objects) {
        Map<String, Object> resultMap = new HashMap(10);

        for (int i = 0; i < paramsNames.length; ++i) {
            resultMap.put(paramsNames[i], objects[i]);
        }

        return resultMap;
    }
}
