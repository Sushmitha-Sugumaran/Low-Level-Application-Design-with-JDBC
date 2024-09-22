import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class ReturnDAO {
        public void displayreturnList() throws Exception{
        Connection con = JDBC.getConnection();
        Statement st = con.createStatement();
        String query = "SELECT * FROM Return_book";
        ResultSet rs = st.executeQuery(query);
        System.out.println("--------------------Return and Fine Details---------------------");
        int flag = 0;
        System.out.printf("  %-15s||  %-15s||  %-15s||  %-15s||\n", "User ID", "BOOK ID", "Fine Amount","Days Taken");
        while (rs.next()){
            System.out.printf("  %-15s||  %-15s||  %-15s||  %-15s||\n", rs.getInt(1),rs.getInt(2),rs.getString(3),rs.getString(4));
            flag++;
        }
        if (flag == 0) {
            System.out.println("No Returns so far.... ");
        }
        con.close();
        st.close();
        rs.close();
    }

        public void returnBook() throws Exception {
        Scanner scan = new Scanner(System.in);
        BorrowDAO.displayuserBorrowList();
        System.out.print("Enter the Book ISBN number: ");
        int temp_isbn = scan.nextInt();

        try (Connection con = JDBC.getConnection();
             PreparedStatement pst = con.prepareStatement(
                     "SELECT * FROM Borrow WHERE u_id = ? AND b_id = ?",
                     ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_UPDATABLE)) {

            pst.setInt(1, Library.user_id);
            pst.setInt(2, temp_isbn);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    System.out.print("Enter the Duration taken for the Book since Borrow: ");
                    int days = scan.nextInt();

                    if (days <= 15) {
                        handleNormalReturn(rs, con, temp_isbn, days);
                    } else {
                        handleLateReturn(rs, con, temp_isbn, days);
                    }
                } else {
                    System.out.println("No borrowed book found with the given ISBN for this user.");
                }
            }
        }
    }

        private void handleNormalReturn(ResultSet rs, Connection con, int temp_isbn, int days) throws Exception {
        rs.deleteRow();
        System.out.println("Book Returned Successfully....:)");
        updateBookStock(con, temp_isbn);
        addReturn(Library.user_id, temp_isbn, "No Fine", String.valueOf(days));
    }

        private void handleLateReturn(ResultSet rs, Connection con, int temp_isbn, int days) throws Exception {
        int temp = days - 15;
        double temp_amt = temp * 15;
        String fine = String.valueOf(temp_amt);
        System.out.println("You have Fined Amount of " + temp_amt + " due to exceed of returning date...");

        if (updateUserWallet(con, fine)) {
            rs.deleteRow();
            updateBookStock(con, temp_isbn);
            addReturn(Library.user_id, temp_isbn, fine, String.valueOf(days));
            System.out.println("Book Returned Successfully....:)");
        } else {
            System.out.println("FIRST ADD MONEY IN YOUR WALLET");
            Library.orgUser();
        }
    }
        private static boolean updateUserWallet(Connection con, String fine) throws Exception {
            String query = "SELECT u_amt FROM User WHERE u_id = ?";
            try (PreparedStatement pst = con.prepareStatement(query)) {
                pst.setInt(1, Library.user_id);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        double wallet = rs.getDouble("u_amt");
                        if (wallet >= Double.parseDouble(fine)) {
                            wallet -= Double.parseDouble(fine);
                            try (PreparedStatement updatePst = con.prepareStatement("UPDATE User SET u_amt = ? WHERE u_id = ?")) {
                                updatePst.setDouble(1, wallet);
                                updatePst.setInt(2, Library.user_id);
                                updatePst.executeUpdate();
                                return true;
                            }
                        }
                    }
                }
            }
            return false;
        }
        private static void updateBookStock(Connection con, int temp_isbn) throws Exception {
            String query = "UPDATE Book SET b_count = 0, b_stock = b_stock + 1 WHERE b_id = ?";
            try (PreparedStatement pst = con.prepareStatement(query)) {
                pst.setInt(1, temp_isbn);
                pst.executeUpdate();
            }
        }

        private static void addReturn(int user_id, int b_id, String fine, String duration) throws Exception {
            String query = "INSERT INTO Return_Book(u_id, b_id, fineAmt, days) VALUES (?, ?, ?, ?)";
            try (Connection con = JDBC.getConnection();
                 PreparedStatement pst = con.prepareStatement(query)) {
                pst.setInt(1, user_id);
                pst.setInt(2, b_id);
                pst.setString(3, fine);
                pst.setString(4, duration);
                pst.executeUpdate();
            }
        }

        public static void bookMiss() throws Exception {
            Scanner scan = new Scanner(System.in);
            BorrowDAO.displayuserBorrowList();
            System.out.print("Enter the Book ISBN number:");
            int temp_isbn = scan.nextInt();

            try (Connection con = JDBC.getConnection();
                 Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery("SELECT * FROM Borrow WHERE u_id = " + Library.user_id)) {

                boolean found = false;
                while (rs.next()) {
                    found = true;
                    handleMissingBook(rs, con, temp_isbn);
                }

                if (!found) {
                    System.out.println("Entered ISBN is not in your Borrow History. Please try again...");
                    bookMiss();
                }
            }
        }

        private static void handleMissingBook(ResultSet rs, Connection con, int temp_isbn) throws Exception {
            double temp_amt = rs.getDouble(7) / 2;
            System.out.println("You have Fined Amount of " + temp_amt + " due to Lose the Book...");

            updateBookCount(con, temp_isbn);

            String fine = String.valueOf(temp_amt);
            if (updateUserWallet(con, fine)) {
                System.out.println("Fine Amount Paid Successfully....:)");
                addReturn(Library.user_id, temp_isbn, fine, "Missed");
                rs.deleteRow();
            } else {
                System.out.println("FIRST ADD MONEY IN YOUR WALLET");
                Library.orgUser();
            }
        }

        private static void updateBookCount(Connection con, int temp_isbn) throws Exception {
            String query = "UPDATE Book SET b_count = 0 WHERE b_id = ?";
            try (PreparedStatement pst = con.prepareStatement(query)) {
                pst.setInt(1, temp_isbn);
                pst.executeUpdate();
            }
        }
    }

