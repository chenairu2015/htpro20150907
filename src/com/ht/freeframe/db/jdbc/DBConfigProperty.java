package com.ht.freeframe.db.jdbc;

import java.util.Properties;

public class DBConfigProperty {

	// 单例模式Begin
	private static DBConfigProperty instance = new DBConfigProperty();

	private DBConfigProperty() {
	}

	public static DBConfigProperty getInstance() {
		return instance;
	}
	// 单例模式End
	
}
