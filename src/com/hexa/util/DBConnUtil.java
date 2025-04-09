package com.hexa.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.hexa.exception.DbConnectionException;

public class DBConnUtil {

	static {
		try {
			Class.forName(DBPropertyUtil.getDriver());
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	public static Connection getDbConnection() throws DbConnectionException {
		Connection conn=null;
		try {
			conn = DriverManager.getConnection(DBPropertyUtil.getDbURL(),DBPropertyUtil.getProps());
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			throw new DbConnectionException();
		}
		return conn;
	}
	
	public static void closeConnection(Connection conn) {
			try {
				if(conn != null) 
					conn.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
}

