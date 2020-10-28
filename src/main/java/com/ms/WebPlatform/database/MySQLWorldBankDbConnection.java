package com.ms.WebPlatform.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.stereotype.Component;

@Component
public class MySQLWorldBankDbConnection {
	public static Connection connObj;
	//public static String JDBC_URL = "jdbc:mysql://localhost:3306/world_bank_app";
	
	static String JDBC_URL = "jdbc:mysql://103.197.121.84:3306/msrtc_rawdata";
			
	
	public static String USERNAME = "msrtc_read_only";
	public static String PASSWORD = "G@nG@P0in7";
	
	
	public static void init() {
		try {
			//Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			connObj = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
			System.out.println("MySQLWorldBankDbConnection Stablished. Connection Object : " + connObj);
		} catch (Exception sqlException) {
			sqlException.printStackTrace();
		}
	}
	
	public static ResultSet resultSet(String strSql) throws SQLException {
		String[] split = strSql.split(" ");
		for (String string : split) {
			if(string.equalsIgnoreCase("delete") || string.equalsIgnoreCase("insert")){
				//int executeUpdate = connObj.createStatement().executeUpdate(strSql);
			}else {
				ResultSet workbench = connObj.createStatement().executeQuery(strSql);
				System.out.println("fetching size -->> "+workbench.getFetchSize());
				return workbench;
			}
		}
		return null;
	}
	}

