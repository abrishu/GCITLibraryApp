package com.gcitsolutions.libraryapp.Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtils {

	public static Connection getConnection() throws SQLException{
		
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/library", "root", "root");
			conn.setAutoCommit(false);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	
}
