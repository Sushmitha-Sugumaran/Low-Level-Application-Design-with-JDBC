import java.sql.*;
import java.util.*;

public class UserDAO {
    static Scanner scan = new Scanner(System.in);

    public static String getUserName(int userId) throws Exception{
        String name = null;
        Connection con = JDBC.getConnection();
        Statement st = con.createStatement();
        String query = "SELECT * FROM User WHERE id = " + userId;
        ResultSet rs = st.executeQuery(query);
        while (rs.next()){
            name = rs.getString(2);
        }
        return name;
    }

    public boolean isValid(String phoneNumber, String mailId, String password) throws Exception{
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
            con = JDBC.getConnection();
            String query = "SELECT * FROM User WHERE phonenum =? OR email=? OR pass=?";
            pst = con.prepareStatement(query);
            pst.setString(1, phoneNumber);
            pst.setString(2,mailId);
            pst.setString(3,password);

            rs = pst.executeQuery();

            return rs.next();
    }

    public void addUser(String name, String phoneNumber, int wallet, String mailId, String password) throws Exception{
        Connection con = JDBC.getConnection();
        String query = "INSERT INTO User(uname,phonenum,wallet,email,pass) VALUES(?,?,?,?,?)";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setString(1,name);
        pst.setString(2,phoneNumber);
        pst.setInt(3,wallet);
        pst.setString(4,mailId);
        pst.setString(5,password);
        pst.executeUpdate();
        con.close();
        pst.close();
    }

    public boolean isCorrectUser(String email, String password) throws Exception{
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        con = JDBC.getConnection();
        String query = "SELECT * FROM User WHERE email=? AND pass=?";
        pst = con.prepareStatement(query);
        pst.setString(1,email);
        pst.setString(2,password);

        rs = pst.executeQuery();

        while (rs.next()){
            BOOKMYSHOWManagament.userId = rs.getInt(1);
            return true;
        }
        return false;
    }

    public void displayWallet(int userId) throws Exception {
        Connection con = JDBC.getConnection();
        Statement st = con.createStatement();
        String query = "SELECT * FROM User WHERE id = " + userId;
        ResultSet rs = st.executeQuery(query);
        while (rs.next()){
        System.out.println("TOTAL AMOUNT IN YOUR WALLET : " + rs.getInt(4));
        }
    }

    public void addMoneyInWallet(int userId) throws Exception{
        System.out.println("Enter the amount");
        int wallet = scan.nextInt();
        Connection con = JDBC.getConnection();

        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM User WHERE id = "+userId);
        while (rs.next()){
            wallet+=rs.getInt(4);
        }

        String query = "UPDATE User SET wallet = ? WHERE id= "+userId;
        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1,wallet);
        pst.executeUpdate();

        System.out.println("AMOUNT ADDED IN YOUR WALLET SUCCESSFULLY");

        con.close();
        st.close();
        pst.close();
        rs.close();
    }

    public static void updateWallet(int userId, int totalTicketPrice) throws Exception{
        int wallet = 0;
        Connection con = JDBC.getConnection();

        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM User WHERE id = "+userId);
        while (rs.next()){
            wallet=rs.getInt(4);
        }

        wallet -=totalTicketPrice;
        String query = "UPDATE User SET wallet = ? WHERE id= "+userId;
        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1,wallet);
        pst.executeUpdate();

        con.close();
        st.close();
        pst.close();
        rs.close();
    }

    public static int getWalletAmount(int userId) throws Exception{
        int wallet = 0;
        Connection con = JDBC.getConnection();
        Statement st = con.createStatement();
        String query = "SELECT * FROM User WHERE id = "+userId;
        ResultSet rs = st.executeQuery(query);
        while (rs.next()){
            wallet=rs.getInt(4);
        }
        return wallet;
    }
}
