package com.haku.conn;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionUtils {

	public static Connection getConnection() throws ClassNotFoundException, SQLException {
		return MysqlConnUtils.getMysqlConnection();
	}

	public static void closeQuietly(Connection connection) {
		try {
			if (connection != null)
				connection.close();
		} catch (SQLException e) {
			System.out.println("Close Connection: Error");
			e.printStackTrace();
		}
	}

	public static void rollbackQuietly(Connection connection) {
		try {
			connection.rollback();
		} catch (SQLException e) {
			System.out.println("Rollback Connection: Error");
			e.printStackTrace();
		}
	}

}
