import java.sql.*;
public class JDBC {
    static String url = "jdbc:mysql://localhost:3306/Library";
    static String userName = "root";
    static String password = "Sushmitha@123";
    Connection con;
    public static Connection getConnection() throws Exception{
        return DriverManager.getConnection(url,userName,password);
        }
}
