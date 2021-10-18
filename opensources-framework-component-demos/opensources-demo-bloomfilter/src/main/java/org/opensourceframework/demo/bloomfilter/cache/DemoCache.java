package org.opensourceframework.demo.bloomfilter.cache;

import com.github.pagehelper.PageInfo;
import org.opensourceframework.demo.bloomfilter.dao.eo.DemoEo;
import org.opensourceframework.demo.bloomfilter.service.IDemoService;
import org.opensourceframework.component.redis.bloomFilter.service.IBloomFilterService;
import org.opensourceframework.component.redis.cache.service.IRedisCacheService;
import org.opensourceframework.component.redis.lock.service.ILockService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 初始化用户数据的布鲁过滤器
 *
 * @author yu.ce@foxmail.com
 * 
 * @since  1.0.0
 */
@Component
public class DemoCache {
    private static final Logger logger = LoggerFactory.getLogger(DemoCache.class);
    private static final String DEMO_BOOLFILTER_KEY = "boolFilter:user";

    private static final String DEMO_DATA_KEY = "user:";

    private static final String INIT_DATA_KEY = "initdata";

    @Autowired
    private IDemoService demoService;

    @Autowired
    private IRedisCacheService redisCacheService;

    @Autowired
    private IBloomFilterService bloomFilterService;

    @Autowired
    private ILockService lockService;

    public void init() throws Exception {
        Boolean isLocked = false;
        try{
            isLocked = lockService.lock(DEMO_BOOLFILTER_KEY , INIT_DATA_KEY, 0 , 300 , TimeUnit.SECONDS);
            if(isLocked){
                logger.info("\n ------------Init Demo BloomFilter Start --------------");
                DemoEo queryEo = new DemoEo();
                Integer count = demoService.count(queryEo);

                Integer pageSize = 3000;
                Integer pageCount = count % pageSize != 0 ? (count / pageSize) + 1 : count / pageSize;

                for(int pageNum = 1 ; pageNum <= pageCount ; pageNum ++) {
                    PageInfo<DemoEo> pageInfo = demoService.findPage(queryEo , pageNum , pageSize);

                    if(CollectionUtils.isNotEmpty(pageInfo.getList())){
                        pageInfo.getList().forEach(demoEo -> {
                            bloomFilterService.add(DEMO_BOOLFILTER_KEY , demoEo.getId());
                        });
                    }
                }
                logger.info("\n ------------Init Demo BloomFilter End --------------");
            }
        }catch (Exception e){
            logger.error("get redis locked is error. reason:{}" , ExceptionUtils.getStackTrace(e));
        }finally {
            if(isLocked){
                lockService.unlock(DEMO_BOOLFILTER_KEY , INIT_DATA_KEY);
            }
        }
    }

    public Boolean checkExist(Long pkId){
        if(pkId != null) {
            return bloomFilterService.exist(DEMO_BOOLFILTER_KEY, pkId);
        }else {
            return false;
        }
    }

    public void addToBloomFilter(DemoEo demoEo){
        if(demoEo != null && demoEo.getId() != null) {
            bloomFilterService.add(DEMO_BOOLFILTER_KEY, demoEo.getId().toString());
        }else {
            logger.warn("cache demoEo , data is null.");
        }
    }

    /**
     * 缓存数据1个小时  对于配置参数可永久缓存
     *
     * @param demoEo
     */
    public void addCache(DemoEo demoEo){
        if(demoEo == null){
            return;
        }
        redisCacheService.add(getCacheKey(demoEo.getId()) , demoEo , 3600);
        addToBloomFilter(demoEo);

    }

    public DemoEo getCache(Long pkId){
        if(pkId == null){
            return null;
        }
        return redisCacheService.get(getCacheKey(pkId) , DemoEo.class);
    }

    private String getCacheKey(Long pkId){
        return DEMO_DATA_KEY.concat(pkId.toString());
    }

}
