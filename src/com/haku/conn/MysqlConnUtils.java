package com.haku.conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MysqlConnUtils {
	
	
	public static Connection getMysqlConnection() throws ClassNotFoundException, SQLException
	{	
		String hostName = "localhost";
	    String dbName = "dbexampleservlet";
	    String userName = "root";
	    String password = "buiquangcuong";
	    
		return getMysqlConnection(hostName,dbName,userName,password);
	}
	public static Connection getMysqlConnection(String hostName,String dbName,String username,String password ) 
			throws SQLException, ClassNotFoundException
	{
		Class.forName("com.mysql.cj.jdbc.Driver");
		 String connectionURL = "jdbc:mysql://" + hostName + ":3306/" + dbName;
		 Connection connection = DriverManager.getConnection(connectionURL,username,password);
		 return connection;
	}
}
