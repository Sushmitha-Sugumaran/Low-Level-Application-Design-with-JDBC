import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class BorrowDAO {
    public void displayborrowList() throws Exception{
        Connection con = JDBC.getConnection();
        Statement st = con.createStatement();
        String query = "SELECT * FROM Borrow";
        ResultSet rs = st.executeQuery(query);
        System.out.println("\t\t------------borrows in the Library----------");
        System.out.println();
        System.out.printf("%-15s ||  %-30s||  %-20s||  %-15s||  %-15s||  %-15s|| %-15s||\n", "User ID","Book Id", "Book Title",
                "Book Author", "Book Type", "Book count","book Price");
        System.out.println();
        int flag = 0;
        while (rs.next()) {
                System.out.printf("%-15s ||  %-30s||  %-20s||  %-15s||  %-15s||  %-15s|| %-15s||\n",
                        rs.getInt(1), rs.getInt(2),rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(5), rs.getInt(6), rs.getDouble(7));
            flag++;
        }
        if (flag == 0) {
            System.out.println("No Returns so far.... ");
        }
        System.out.println();
        rs.close();
        st.close();
        con.close();
    }

    public static void displayuserBorrowList() throws Exception{
        Connection con = JDBC.getConnection();
        Statement st = con.createStatement();
        int id = Library.user_id;
        String query = "SELECT * FROM Borrow WHERE u_id = "+id;
        ResultSet rs = st.executeQuery(query);
        System.out.println("\t\t------------borrows in the Library----------");
        System.out.println();
        System.out.printf("%-15s ||  %-30s||  %-20s||  %-15s||  %-15s||  %-15s|| %-15s||\n", "Book ID","User Id", "Book Title",
                "Book Author", "Book Type", "Book count","book Price");
        System.out.println();
        int flag = 0;
        while (rs.next()) {
            System.out.printf("%-15s ||  %-30s||  %-20s||  %-15s||  %-15s||  %-15s|| %-15s||\n",
                    rs.getInt(1), rs.getInt(2),rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(5), rs.getInt(6), rs.getDouble(7));
            flag++;
        }
        if (flag == 0) {
            System.out.println("No Returns so far.... ");
        }
        System.out.println();
        rs.close();
        st.close();
        con.close();
    }

    public void borrowBook() throws Exception {
        Scanner scan = new Scanner(System.in);
        BookDAO.showAvailableBooks();
        System.out.print("Enter the number of bookings: ");
        int bo_no = scan.nextInt();

        if (bo_no > 3) {
            System.out.println("One time borrow limit is 3");
            return;
        }

        Connection con = null;
        PreparedStatement pstUser = null;
        PreparedStatement pstBook = null;
        ResultSet rsUser = null;
        ResultSet rsBook = null;

        try {
            con = JDBC.getConnection();
            pstUser = con.prepareStatement("SELECT u_amt FROM User WHERE u_id = ?");
            pstUser.setInt(1, Library.user_id);

            pstBook = con.prepareStatement("SELECT * FROM Book WHERE b_id = ?");

            for (int j = 0; j < bo_no; j++) {
                rsUser = pstUser.executeQuery();
                if (rsUser.next()) {
                    double amt = rsUser.getDouble("u_amt");
                    if (amt >= 100) {
                        System.out.print("Enter the book ISBN to place your order: ");
                        int bookId = scan.nextInt();

                        pstBook.setInt(1, bookId);
                        rsBook = pstBook.executeQuery();

                        if (rsBook.next()) {
                            int book_exit = checkBookAlreadyBorOrNot(rsBook.getInt("b_id"));
                            if (book_exit != 0) {
                                System.out.println("Book already borrowed....");
                            } else {
                                addBorrow(rsBook.getInt("b_id"));
                                System.out.println("Book borrowed successfully.");
                            }
                        } else {
                            System.out.println("Book not found.");
                        }
                    } else {
                        System.out.println("Your Available Library balance is too low.....");
                    }
                } else {
                    System.out.println("User not found.");
                }
            }
        } finally {
            // Close resources in reverse order of creation
            if (rsBook != null) rsBook.close();
            if (rsUser != null) rsUser.close();
            if (pstBook != null) pstBook.close();
            if (pstUser != null) pstUser.close();
            if (con != null) con.close();
        }
    }
    private void addBorrow(int b_id) throws Exception{
        Connection con = JDBC.getConnection();
        String query1 = "SELECT * FROM Book WHERE b_id = "+b_id;
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query1);
        String b_title = null,b_auth=null,b_type = null;
        int b_count =0,b_stock=0;
        double b_price =0.0;
        while (rs.next()) {
            b_title=rs.getString(2);
            b_auth=rs.getString(3);
            b_type=rs.getString(4);
            b_count=rs.getInt(5);
            b_stock=rs.getInt(6);
            b_price=rs.getDouble(7);
        }

        PreparedStatement pst1 = con.prepareStatement("INSERT INTO Book (b_count,b_stock) VALUES(?,?)");
        b_count ++;
        b_stock--;
        pst1.setInt(1,b_count);
        pst1.setInt(2,b_stock);
        pst1.executeUpdate();

        String query = "INSERT INTO Borrow(b_id,u_id,b_title, b_auth, b_type, b_count,b_price) VALUES (?,?,?,?,?,?,?)";
        String query2 = "SELECT * FROM Borrow WHERE b_id = "+b_id;
        ResultSet rs1 = st.executeQuery(query2);
        int bo_count = 0;
        double bo_amt = 0;
        while (rs1.next()) {
            bo_count=rs.getInt(5);
        }

        PreparedStatement pst = con.prepareStatement(query);
        bo_count++;
        bo_amt+=b_price;
        pst.setInt(1,b_id);
        pst.setInt(2,Library.user_id);
        pst.setString(3, b_title);
        pst.setString(4, b_auth);
        pst.setString(5, b_type);
        pst.setInt(6, bo_count);
        pst.setDouble(7, bo_amt);
        pst.executeUpdate();

        String query3 = "SELECT * FROM User WHERE u_id = "+Library.user_id;
        Statement st2 = con.createStatement();
        ResultSet rs3= st.executeQuery(query3);
        double wallet = 0;
        while (rs3.next()) {
            wallet = rs3.getInt(4);
        }
        PreparedStatement pst3 = con.prepareStatement("UPDATE User SET u_amt = ? WHERE u_id = ?");
        wallet = wallet-b_price;
        pst3.setDouble(1,wallet);
        pst3.setInt(2,Library.user_id);
        pst3.executeUpdate();

        rs3.close();
        pst1.close();
        pst3.close();
        pst.close();
        con.close();
        System.out.println(
                "Book Borrowed successfully!!! You must return the book within 15 days otherwise you will be fined...");
    }

    private static int checkBookAlreadyBorOrNot(int b_id) throws Exception{
        int book_exit = 0;
        Connection con = JDBC.getConnection();
        Statement st = con.createStatement();
        String query = "SELECT * FROM Borrow WHERE b_id = "+b_id;
        ResultSet rs = st.executeQuery(query);
        while (rs.next()){
                if (rs.getInt(1)==b_id){
                    book_exit++;
                }
            }
        return book_exit;
    }
}
