package com.ht2015.properties;

import com.ht2015.exception.HTPropertiesException;

public class DBConfigPropertiesBean {

	private String driverClassName;
	private String username;
	private String password;
	private String filters;
	private String maxActive;
	private String initialSize;
	private String maxWait;
	private String minIdle;
	private String maxIdle;
	private String timeBetweenEvictionRunsMillis;
	private String minEvictableIdleTimeMillis;
	private String validationQuery;
	private String testWhileIdle;
	private String testOnBorrow;
	private String testOnReturn;
	private String maxOpenPreparedStatements;
	private boolean removeAbandoned;
	private String removeAbandonedTimeout;
	private boolean logAbandoned;

	public String getDriverClassName() throws HTPropertiesException {
		return validateNull(driverClassName, "driverClassName");
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public String getUsername() throws HTPropertiesException {
		return validateNull(username, "username");
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() throws HTPropertiesException {
		return validateNull(password, "password");
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFilters() throws HTPropertiesException {
		return validateNull(filters, "filters");
	}

	public void setFilters(String filters) {
		this.filters = filters;
	}

	public String getMaxActive() throws HTPropertiesException {
		return validateNull(maxActive, "maxActive");
	}

	public void setMaxActive(String maxActive) {
		this.maxActive = maxActive;
	}

	public String getInitialSize() throws HTPropertiesException {
		return validateNull(initialSize, "initialSize");
	}

	public void setInitialSize(String initialSize) {
		this.initialSize = initialSize;
	}

	public String getMaxWait() throws HTPropertiesException {
		return validateNull(maxWait, "maxWait");
	}

	public void setMaxWait(String maxWait) {
		this.maxWait = maxWait;
	}

	public String getMinIdle() throws HTPropertiesException {
		return validateNull(minIdle, "minIdle");
	}

	public void setMinIdle(String minIdle) {
		this.minIdle = minIdle;
	}

	public String getMaxIdle() throws HTPropertiesException {
		return validateNull(maxIdle, "maxIdle");
	}

	public void setMaxIdle(String maxIdle) {
		this.maxIdle = maxIdle;
	}

	public String getTimeBetweenEvictionRunsMillis()
			throws HTPropertiesException {
		return validateNull(timeBetweenEvictionRunsMillis,
				"timeBetweenEvictionRunsMillis");
	}

	public void setTimeBetweenEvictionRunsMillis(
			String timeBetweenEvictionRunsMillis) {
		this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
	}

	public String getMinEvictableIdleTimeMillis() throws HTPropertiesException {
		return validateNull(minEvictableIdleTimeMillis,
				"minEvictableIdleTimeMillis");
	}

	public void setMinEvictableIdleTimeMillis(String minEvictableIdleTimeMillis) {
		this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
	}

	public String getValidationQuery() throws HTPropertiesException {
		return validateNull(validationQuery, "validationQuery");
	}

	public void setValidationQuery(String validationQuery) {
		this.validationQuery = validationQuery;
	}

	public String getTestWhileIdle() throws HTPropertiesException {
		return validateNull(testWhileIdle, "testWhileIdle");
	}

	public void setTestWhileIdle(String testWhileIdle) {
		this.testWhileIdle = testWhileIdle;
	}

	public String getTestOnBorrow() throws HTPropertiesException {
		return validateNull(testOnBorrow, "testOnBorrow");
	}

	public void setTestOnBorrow(String testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}

	public String getTestOnReturn() throws HTPropertiesException {
		return validateNull(testOnReturn, "testOnReturn");
	}

	public void setTestOnReturn(String testOnReturn) {
		this.testOnReturn = testOnReturn;
	}

	public String getMaxOpenPreparedStatements() throws HTPropertiesException {
		return validateNull(maxOpenPreparedStatements,
				"maxOpenPreparedStatements");
	}

	public void setMaxOpenPreparedStatements(String maxOpenPreparedStatements) {
		this.maxOpenPreparedStatements = maxOpenPreparedStatements;
	}

	public boolean isRemoveAbandoned() {
		return removeAbandoned;
	}

	public void setRemoveAbandoned(boolean removeAbandoned) {
		this.removeAbandoned = removeAbandoned;
	}

	public String getRemoveAbandonedTimeout() {
		return removeAbandonedTimeout;
	}

	public void setRemoveAbandonedTimeout(String removeAbandonedTimeout) {
		this.removeAbandonedTimeout = removeAbandonedTimeout;
	}

	public boolean isLogAbandoned() {
		return logAbandoned;
	}

	public void setLogAbandoned(boolean logAbandoned) {
		this.logAbandoned = logAbandoned;
	}

	private String validateNull(String string, String name)
			throws HTPropertiesException {
		if (name == null)
			throw new HTPropertiesException("配置项名称不能为空");
		if (string == null)
			throw new HTPropertiesException("获取配置文件信息失败，检查配置文件中属性："
					+ name.toUpperCase());
		return string;
	}
}
