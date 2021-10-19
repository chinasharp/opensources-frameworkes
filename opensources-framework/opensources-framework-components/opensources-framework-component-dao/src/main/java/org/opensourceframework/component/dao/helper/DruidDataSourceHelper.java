package org.opensourceframework.component.dao.helper;

import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.collect.Lists;
import org.opensourceframework.component.dao.vo.DataSourceConfigVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

/**
 * 创建DruidDataSource帮助类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class DruidDataSourceHelper {
	private static final Logger logger = LoggerFactory.getLogger(DruidDataSourceHelper.class);

	public static DruidDataSource createDruidDataSource(DataSourceConfigVo dataSourceConfigVo ,
			String connectionProperties) throws SQLException {
		if (logger.isDebugEnabled()) {
			logger.debug("Set data source as {}", dataSourceConfigVo);
		}
		if ((null == dataSourceConfigVo) || (StringUtils.isBlank(dataSourceConfigVo.getJdbcUrl()))) {
			throw new IllegalArgumentException("数据链接字符串为空！");
		}

		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUrl(dataSourceConfigVo.getJdbcUrl());
		dataSource.setUsername(dataSourceConfigVo.getJdbcUserName());
		dataSource.setPassword(dataSourceConfigVo.getJdbcUserPassword());
		dataSource.setInitialSize(dataSourceConfigVo.getInitialSize());
		dataSource.setMaxActive(dataSourceConfigVo.getMaxActive());
		dataSource.setMinIdle(dataSourceConfigVo.getMinIdle());
		dataSource.setMaxWait(dataSourceConfigVo.getMaxWait());
		dataSource.setValidationQuery(dataSourceConfigVo.getValidationQuery());

		DruidDataSourceHelper.setDefaultProperty(dataSource);

        String filters = dataSourceConfigVo.getFilters();
        dataSource.setFilters("mergeStat");
        if(StringUtils.isNotBlank(filters)){
            dataSource.setFilters("mergeStat," + filters);
        }
		dataSource.setConnectionInitSqls(Lists.newArrayList("set names utf8mb4;"));
		if (StringUtils.isNotBlank(connectionProperties)) {
			dataSource.setConnectionProperties(connectionProperties);
		}
		dataSource.init();
		return dataSource;
	}

	public static void setDefaultProperty(DruidDataSource dataSource){
		if(dataSource == null){
			dataSource = new DruidDataSource();
		}
		dataSource.setTestOnBorrow(false);
		dataSource.setTestOnReturn(false);
		dataSource.setTestWhileIdle(true);
		dataSource.setTimeBetweenEvictionRunsMillis(60000L);
		dataSource.setMinEvictableIdleTimeMillis(25200000L);
		dataSource.setRemoveAbandoned(true);
		dataSource.setRemoveAbandonedTimeout(1800);
		dataSource.setLogAbandoned(true);
	}
}
