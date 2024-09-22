import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class UserDAO {
    public void displayuserList() throws Exception{
        Connection con = JDBC.getConnection();
        Statement st = con.createStatement();
        String query = "SELECT * FROM User";
        ResultSet rs = st.executeQuery(query);
        System.out.println("------------ Library User Details -------------");
        System.out.printf("  %-15s ||  %-15s||  %-15s||  %-20s||\n", "User Id", "User Name", "User Password",
                "User Library Balance");
        int count = 0;
        while (rs.next()){
            count++;
            System.out.printf("  %-15s ||  %-15s||  %-15s||  %-20s||\n", rs.getInt(1),rs.getString(2),"***",rs.getDouble(4));
        }
        if (count == 0) {
            System.out.println("No user To be shown");
        }
        con.close();
        rs.close();
        st.close();
    }

    public void createUser() throws Exception{
        Scanner scan = new Scanner(System.in);
        Connection con = JDBC.getConnection();
        String query = "INSERT INTO User (u_name,u_pass,u_amt) VALUES (?,?,?)";
        PreparedStatement pst = con.prepareStatement(query);
        System.out.print("Enter User Name:");
        String u_name = scan.next();
        System.out.print("Set User Password");
        String u_pass = scan.next();
        System.out.print("Set Initial Library Amount:");
        double u_amt = scan.nextDouble();
        pst.setString(1,u_name);
        pst.setString(2,u_pass);
        pst.setDouble(3,u_amt);
        pst.executeUpdate();
        System.out.println("Your User Id is " + u_name+","+u_pass + " Please Remember this for Login purposes");
        System.out.println("User Created Successfully...Try to log In");
        con.close();
        pst.close();
    }

    public void existUser() throws Exception {
        Scanner scan = new Scanner(System.in);
        Connection con = JDBC.getConnection();
        System.out.print("Enter User name: ");
        String u_name = scan.next();
        System.out.print("User Password: ");
        String u_pass = scan.next();

        String query = "SELECT * FROM User WHERE u_name = ? AND u_pass = ?";

        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setString(1, u_name);
        pstmt.setString(2, u_pass);

        ResultSet rs = pstmt.executeQuery();
        int flag=0;
        while (rs.next()) {
            Library.user_id=rs.getInt(1);
            flag++;
            Library.orgUser();
        }
        if (flag==0){
            System.out.println("Invalid login Credentials Try again...");
            existUser();
        }
        con.close();
        pstmt.close();
        rs.close();
    }

    public void walletCheck(int user_id) throws Exception{
        Connection con = JDBC.getConnection();
        String query = "SELECT * FROM User WHERE u_id ="+user_id;
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next()){
            System.out.println("Amount : "+rs.getInt(4));
        }
        con.close();
        st.close();
        rs.close();
    }

    public void addMoney() throws Exception{
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter amount..");
        double amt = scan.nextDouble();
        String query = "UPDATE User SET u_amt = ? WHERE u_id = ?";
        Connection con = JDBC.getConnection();
        PreparedStatement pst = con.prepareStatement(query);

        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM User Where u_id = "+Library.user_id);
        while (rs.next()){
            amt+=rs.getInt(4);
        }
        pst.setDouble(1,amt);
        pst.setInt(2,Library.user_id);
        pst.executeUpdate();

        System.out.println("AMOUNT ADDED SUCCESSFULLY...");

        con.close();
        rs.close();
        st.close();
        pst.close();
    }
}
