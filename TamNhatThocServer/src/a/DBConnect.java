package a;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
public class DBConnect {
	  
    private static final String URL = "jdbc:mysql://localhost:3306/tamnhatthoc1";
    private static final String USER = "root";
    private static final String PASSWORD = "06032003";
    
	
	public static Connection getConnect() throws SQLException{
		return DriverManager.getConnection(URL,USER,PASSWORD);
	}

}
