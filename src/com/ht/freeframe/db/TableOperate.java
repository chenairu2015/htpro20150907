package com.ht.freeframe.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

public class TableOperate {
	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	private static final int COUNT = 5;

	public void query() throws SQLException {
		Connection conn = dataSource.getConnection();
		StringBuffer ddl = new StringBuffer();
		PreparedStatement stmt = conn.prepareStatement(ddl.toString());  
		
	}
}
