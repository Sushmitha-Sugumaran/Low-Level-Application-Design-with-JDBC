import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class BookDAO {
    public static void displayBooks() throws Exception {
        Connection con = JDBC.getConnection();
        Statement st = con.createStatement();
        String query = "SELECT * FROM Book";
        ResultSet rs = st.executeQuery(query);
        System.out.println("\t\t------------Available Books in the Library----------");
        System.out.println();
        System.out.printf("%-15s ||  %-30s||  %-20s||  %-15s||  %-15s||  %-15s|| %-15s||\n", "Book Id", "Book Title",
                "Book Author", "Book Type", "Book count", "Book stock", "book Price");
        System.out.println();
        while (rs.next()) {
            System.out.printf("%-15s ||  %-30s||  %-20s||  %-15s||  %-15s||  %-15s|| %-15s||\n",
                    rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5), rs.getInt(6), rs.getDouble(7));
        }
        System.out.println();
        rs.close();
        st.close();
        con.close();
    }

    public static void showAvailableBooks() throws Exception{
        Connection con = JDBC.getConnection();
        Statement st = con.createStatement();
        String query = "SELECT * FROM Book WHERE b_stock > 0";
        ResultSet rs = st.executeQuery(query);
        System.out.println("\t\t------------Available Books in the Library----------");
        System.out.println();
        System.out.printf("%-15s ||  %-30s||  %-20s||  %-15s||  %-15s||  %-15s|| %-15s||\n", "Book Id", "Book Title",
                "Book Author", "Book Type", "Book count", "Book stock", "book Price");
        System.out.println();
        while (rs.next()) {
            System.out.printf("%-15s ||  %-30s||  %-20s||  %-15s||  %-15s||  %-15s|| %-15s||\n",
                    rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5), rs.getInt(6), rs.getDouble(7));
        }
        System.out.println();
        rs.close();
        st.close();
        con.close();
    }

    public void addBook(String b_title, String b_auth, String b_type, int b_count, int b_stock, double b_price) throws Exception {
        Connection con = JDBC.getConnection();
        String query = "INSERT INTO Book(b_title, b_auth, b_type, b_count, b_stock, b_price) VALUES (?,?,?,?,?,?)";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setString(1, b_title);
        pst.setString(2, b_auth);
        pst.setString(3, b_type);
        pst.setInt(4, b_count);
        pst.setInt(5, b_stock);
        pst.setDouble(6, b_price);
        pst.executeUpdate();
        pst.close();
        con.close();
    }

    public void deleteBook() throws Exception {
        Connection con = JDBC.getConnection();
        Scanner scan = new Scanner(System.in);
        displayBooks();
        System.out.println("Enter Book ISBN :");
        int n = scan.nextInt();
        int flag = 0;
        String query1 = "SELECT * FROM Book WHERE b_id = " + n;
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query1);
        while (rs.next()) {
            System.out.println("Book " + rs.getString(2) + " Deleted Successfully...");
            flag++;
        }
        if (flag == 0) {
            System.out.println("for this ISBN no books are available");
        }
        String query = " DELETE FROM Book Where b_id = " + n;
        st.executeUpdate(query);
        con.close();
        st.close();
        rs.close();
    }

    public void searchBook() throws Exception {
        Connection con = JDBC.getConnection();
        Scanner scan = new Scanner(System.in);
        displayBooks();
        System.out.println("Enter Book ISBN :");
        int n = scan.nextInt();
        int flag = 0;
        String query = "SELECT * FROM Book WHERE b_id = " + n;
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);
        System.out.printf("%-15s ||  %-30s||  %-20s||  %-15s||  %-15s||  %-15s|| %-15s||\n", "Book Id", "Book Title",
                "Book Author", "Book Type", "Book count", "Book stock", "book Price");
        System.out.println();
        while (rs.next()) {
            System.out.println("Your Book is found successfully...");
            System.out.printf("%-15s ||  %-30s||  %-20s||  %-15s||  %-15s||  %-15s|| %-15s||\n",
                    rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5), rs.getInt(6), rs.getDouble(7));
            flag++;
        }
        System.out.println();
        if (flag == 0){
            System.out.println("for this ISBN no books are available");
        }
        con.close();
        st.close();
        rs.close();
    }
}