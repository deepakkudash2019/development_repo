package com.ms.WebPlatform.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.stereotype.Component;

@Component
public class SQLServerSurveyDbConnection {
	public static Connection connObj;
	public static String JDBC_URL = "jdbc:sqlserver://localhost:1433;databaseName=msrtc_survey_dump;integratedSecurity=true";
	
	
	public static void init() {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			connObj = DriverManager.getConnection(JDBC_URL);
			System.out.println("SQL Server Db Connection Stablished. Connection Object : " + connObj);
		} catch (Exception sqlException) {
			System.out.println("SQL Server Survey Database Connection Error");
			sqlException.printStackTrace();
		}
	}
	
	public static ResultSet resultSet(String strSql) throws SQLException {
		String[] split = strSql.split(" ");
		for (String string : split) {
			if(string.equalsIgnoreCase("delete") || string.equalsIgnoreCase("insert")){
				int executeUpdate = connObj.createStatement().executeUpdate(strSql);
			}else {
				ResultSet dsms = connObj.createStatement().executeQuery(strSql);
				return dsms;
			}
		}
		return null;
	}

}
