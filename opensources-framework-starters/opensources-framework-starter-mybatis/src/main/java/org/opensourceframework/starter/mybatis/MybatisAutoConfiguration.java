package org.opensourceframework.starter.mybatis;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInterceptor;
import com.google.common.collect.Lists;
import org.opensourceframework.base.constants.CommonCanstant;
import org.opensourceframework.component.dao.helper.DruidDataSourceHelper;
import org.opensourceframework.component.dao.interceptor.ShardingInterceptor;
import org.opensourceframework.component.dao.transaction.DistributedTransactionManager;
import org.opensourceframework.starter.mybatis.condition.InitDataSourceCondition;
import org.opensourceframework.starter.mybatis.condition.InitSessionFactoryCondition;
import org.opensourceframework.starter.mybatis.config.DataSourceProperties;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

/**
 * DataSource / SeesionFactory / interceptor
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@EnableTransactionManagement
@EnableConfigurationProperties({DataSourceProperties.class , MybatisProperties.class})
public class MybatisAutoConfiguration {
	public static final String TRANSACTION_MANAGER = "dataSourceTransactionManager";
	private static final Logger logger = LoggerFactory.getLogger(MybatisAutoConfiguration.class);
	@Autowired
	private DataSourceProperties dataSourceProperties;

	@Autowired
	private MybatisProperties mybatisProperties;

	@Value("${druid.stat.mergeSql:true}")
	private boolean mergeSql;

	@Bean(destroyMethod = "close" , name="dataSource")
	@Conditional(InitDataSourceCondition.class)
	public DataSource dataSource() throws SQLException {
		return getDataSource();
	}

	@RefreshScope
	protected DataSource getDataSource() throws SQLException {
		logger.info("dataSource init registryvo:{}", JSON.toJSONString(dataSourceProperties));
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUrl(getDataSourceProperties().getJdbcUrl());
		dataSource.setUsername(getDataSourceProperties().getJdbcUserName());
		dataSource.setPassword(getDataSourceProperties().getJdbcUserPassword());
		dataSource.setInitialSize(getDataSourceProperties().getInitialSize());

		dataSource.setMaxActive(getDataSourceProperties().getMaxActive());

		dataSource.setMinIdle(getDataSourceProperties().getMinIdle());

		dataSource.setMaxWait(getDataSourceProperties().getMaxWait());
		dataSource.setValidationQuery(getDataSourceProperties().getValidationQuery());

		if(StringUtils.isNotBlank(getDataSourceProperties().getAutoHumpCol())){
			System.setProperty(CommonCanstant.DB_AUTO_HUMP_MYSQL_COLUMN, getDataSourceProperties().getAutoHumpCol());
		}else {
			System.setProperty(CommonCanstant.DB_AUTO_HUMP_MYSQL_COLUMN,CommonCanstant.NO_STR);
		}

		DruidDataSourceHelper.setDefaultProperty(dataSource);
        String filters = getDataSourceProperties().getFilters();
        dataSource.setFilters("mergeStat");
		if(StringUtils.isNotBlank(filters)){
            dataSource.setFilters("mergeStat," + filters);
        }
		dataSource.setConnectionInitSqls(Lists.newArrayList("set names utf8mb4;"));
        Properties p = setConnectionProperties(getDataSourceProperties().getConnectionProperties());
		if (!this.mergeSql) {
			p.setProperty("druid.stat.mergeSql", this.mergeSql + "");
		}
        dataSource.setConnectProperties(p);

		return dataSource;
	}

    /**
     * 填充连接属性
     *
     * @param connectionProperties
     */
    public Properties setConnectionProperties(String connectionProperties){
        Properties properties = new Properties();
        if(StringUtils.isBlank(connectionProperties)){
            return properties;
        }
        String[] entries = connectionProperties.split(";");
        for (int i = 0; i < entries.length; i++) {
            String entry = entries[i];
            if (entry.length() > 0) {
                int index = entry.indexOf('=');
                if (index > 0) {
                    String name = entry.substring(0, index);
                    String value = entry.substring(index + 1);
                    properties.setProperty(name, value);
                } else {
                    // no value is empty string which is how java.util.Properties works
                    properties.setProperty(entry, "");
                }
            }
        }
        return properties;
    }
	@Bean("sqlSessionFactory")
	@Conditional(InitSessionFactoryCondition.class)
	public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
		return getSqlSessionFactory(dataSource);
	}

	protected SqlSessionFactory getSqlSessionFactory(DataSource dataSource) throws Exception {
		Configuration config = new Configuration();
		config.setMapUnderscoreToCamelCase(true);
		config.setLogImpl(Slf4jImpl.class);

		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);
		sqlSessionFactoryBean.setConfiguration(config);

		//分页插件
		PageInterceptor pageInterceptor = new PageInterceptor();
		Properties properties = new Properties();
		properties.setProperty("helperDialect", "mysql");
		properties.setProperty("reasonable", "true");
		properties.setProperty("supportMethodsArguments", "true");
		properties.setProperty("returnPageInfo", "check");
		properties.setProperty("params", "count=countSql");
		pageInterceptor.setProperties(properties);

		//分表插件
		ShardingInterceptor shardingInterceptor = new ShardingInterceptor();

		Interceptor[] interceptors = new Interceptor[]{pageInterceptor , shardingInterceptor};
		sqlSessionFactoryBean.setPlugins(interceptors);

		sqlSessionFactoryBean.setMapperLocations(getMybatisProperties().resolveMapperLocations());
		if (StringUtils.isNotBlank(getMybatisProperties().getTypeAliasesPackage())) {
			sqlSessionFactoryBean.setTypeAliasesPackage(getMybatisProperties().getTypeAliasesPackage());
		}
		return sqlSessionFactoryBean.getObject();
	}

	@Bean(name = {"dataSourceTransactionManager"})
	public PlatformTransactionManager dataSourceTransactionManager(@Qualifier("dataSource") DataSource dataSource) {
		return new DistributedTransactionManager(dataSource, getDataSourceProperties().getDistributedFlag());
	}

	public DataSourceProperties getDataSourceProperties() {
		return dataSourceProperties;
	}

	public void setDataSourceProperties(DataSourceProperties dataSourceProperties) {
		this.dataSourceProperties = dataSourceProperties;
	}

	public MybatisProperties getMybatisProperties() {
		return mybatisProperties;
	}

	public void setMybatisProperties(MybatisProperties mybatisProperties) {
		this.mybatisProperties = mybatisProperties;
	}
}
