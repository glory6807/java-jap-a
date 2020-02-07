package project.DBconn;

import java.io.*;
import java.sql.*;
import java.util.Properties;

public class DBconn {
	private static Connection con;
	
	//	private DBconn() { } 
	
	public static Connection getConnection() {
		if(con == null) {
			String file = "src\\project\\resources\\jdbc.properties";
			try (FileInputStream fis = new FileInputStream(file)){
				Properties prop = new Properties();
				prop.load(fis);
				Class.forName(prop.getProperty("driver"));
				con = DriverManager.getConnection(prop.getProperty("url"), prop.getProperty("user"), prop.getProperty("passwd"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return con;
	}
	
	//statement 객체 
	public static void close(Statement stmt) {
		try {
			if(stmt != null)stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void close(PreparedStatement pstmt) {
		try {
			if(pstmt != null)pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void close(Statement stmt, ResultSet rs) {
		try {
			if(stmt != null)stmt.close();
			if(rs != null)rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	//Connection close
	public void close() {
		if(con != null)
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}
	
	
}
