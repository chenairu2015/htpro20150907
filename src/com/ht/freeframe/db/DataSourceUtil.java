package com.ht.freeframe.db;

import java.util.HashMap;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;

import com.alibaba.druid.pool.DruidDataSourceFactory;

public class DataSourceUtil {
	// 多数据源？
	/** 使用配置文件dbconfig.properties构建Druid数据源. */
	public static final int DRUID_MYSQL_SOURCE = 0;

	/** The duird mysql source. */
	private static DataSource druidMysqlSource;

	/** 使用配置文件dbconfig2.properties构建Druid数据源. */
	public static final int DRUID_MYSQL_SOURCE2 = 1;

	/** The druid mysql source2. */
	private static DataSource druidMysqlSource2;

	/** 使用配置文件dbconfig.properties构建Dbcp数据源. */
	public static final int DBCP_SOURCE = 4;

	/** The dbcp source. */
	private static DataSource dbcpSource;

	public static final DataSource getDataSource(int sourceType)
			throws Exception {
		DataSource dataSource = null;
		switch (sourceType) {
		case DRUID_MYSQL_SOURCE:
			if (druidMysqlSource == null) {
				druidMysqlSource = DruidDataSourceFactory.createDataSource();
			}
			dataSource = druidMysqlSource;
			break;
		case DRUID_MYSQL_SOURCE2:
			if (druidMysqlSource2 == null) {
				// druidMysqlSource2 =
				// DruidDataSourceFactory.createDataSource();
			}
			dataSource = dbcpSource;
			break;
		case DBCP_SOURCE:
			if (dbcpSource == null) {
				dbcpSource = BasicDataSourceFactory
						.createDataSource(MySqlConfigProperty.getInstance()
								.getProperties());
			}
			dataSource = dbcpSource;
			break;
		}
		return dataSource;
	}
}
