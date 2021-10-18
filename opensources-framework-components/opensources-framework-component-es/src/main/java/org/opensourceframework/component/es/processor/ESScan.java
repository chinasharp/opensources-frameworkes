package org.opensourceframework.component.es.processor;

import org.opensourceframework.component.es.annotation.ESDocument;
import org.opensourceframework.component.es.base.BaseEsEo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ES mapping类 自动加载
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@Component
public class ESScan implements ResourceLoaderAware {
	/**
	 * Spring容器注入
	 */
	private ResourceLoader resourceLoader;

	private final ResourcePatternResolver resolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
	private final MetadataReaderFactory metaReaderFactory = new CachingMetadataReaderFactory(resourceLoader);
	private static final ConcurrentHashMap<String, Set<Class<? extends BaseEsEo>>> classMap = new ConcurrentHashMap<>();

	/**
	 * set注入对象
	 */
	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	/**
	 * 扫描包下的类信息
	 *
	 * @param scanPath
	 * @return
	 * @throws IOException
	 */
	public Set<Class<? extends BaseEsEo>> scanClass(String scanPath) throws IOException {
		Set<Class<? extends BaseEsEo>> classes = classMap.get(scanPath);
		if (CollectionUtils.isNotEmpty(classes)) {
			return classes;
		} else {
			classes = new HashSet<>();
			String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX.concat(ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(scanPath)).concat("/**/*.class"));
			Resource[] resources = resolver.getResources(packageSearchPath);
			MetadataReader metadataReader = null;
			for (Resource resource : resources) {
				if (resource.isReadable()) {
					metadataReader = metaReaderFactory.getMetadataReader(resource);
					try {
						// 当类型不是抽象类或接口在添加到集合
						if (metadataReader.getClassMetadata().isConcrete()) {
							Class<?> clazz = Class.forName(metadataReader.getClassMetadata().getClassName());
							if (clazz.isAnnotationPresent(ESDocument.class)) {
								classes.add((Class<? extends BaseEsEo>) clazz);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			classMap.putIfAbsent(scanPath, classes);
		}
		return classes;
	}
}
