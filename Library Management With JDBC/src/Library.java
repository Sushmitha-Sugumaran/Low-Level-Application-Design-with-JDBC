import java.util.*;

public class Library {
     static int user_id = 53, index = 0;
    private static Scanner scan;
    static BookDAO bookDAO = new BookDAO();
    static UserDAO userDAO = new UserDAO();
    static BorrowDAO borrowDAO = new BorrowDAO();
    static ReturnDAO returnDAO = new ReturnDAO();

    public static void main(String[] args) {
        scan = new Scanner(System.in);
        library_home();
    }

    private static void library_home() {
        System.out.println("\t-----------Welcome to EC Library Management System------------");
        System.out.println("1.Admin LogIn");
        System.out.println("2.User LogIn");
        System.out.println("3.Exit");
        System.out.print("Enter your Choice :");
        int ch = scan.nextInt();
        switch (ch) {
            case 1:
                admin();
                break;
            case 2:
                user();
                break;
            case 3:
                System.exit(0);
            default:
                System.out.println("Enter valid Input... ");
                library_home();
                break;
        }
    }

    private static void admin() {
        System.out.println("--------Welcome Admin--------");
        System.out.print("Enter Admin ID :");
        int admin_id = scan.nextInt();
        System.out.print("Enter Admin Password :");
        int admin_pass = scan.nextInt();
        if (admin_id == 100 && admin_pass == 1234) {
            orgAdmin();
        } else {
            System.out.println("Please Enter valid Admin Credentials....");
            admin();
        }
    }

    private static void orgAdmin() {

        System.out.println("\t\t------- Welcome Admin Amrith --------");
        System.out.println("1.Add Book");
        System.out.println("2.Book Details");
        System.out.println("3.Delete Book");
        System.out.println("4.Search Book");
        System.out.println("5.User Details");
        System.out.println("6.Borrow Details");
        System.out.println("7.Return and Fine");
        System.out.println("8.Exit");
        System.out.print("Enter Your Choice :");
        int choice = scan.nextInt();
        switch (choice) {
            case 1:
                addBook();
                break;
            case 2:
                bookDetails();
                break;
            case 3:
                deleteBook();
                break;
            case 4:
                searchBook();
                break;
            case 5:
                userDetails();
                break;
            case 6:
                borrow_details();
                break;
            case 7:
                return_fine();
                break;
            case 8:
                library_home();
                break;
            default:
                System.out.println("Please Enter valid Input...");
                orgAdmin();
        }
    }

    // ----------------------- add book -------------------------//

    private static void addBook() {
        System.out.println("--------- Add Book ---------");
        System.out.print("Enter the Book Title :");
        String b_title = scan.next();
        System.out.print("Enter the Book Author :");
        String b_auth = scan.next();
        System.out.print("Enter the Book Type :");
        String b_type = scan.next();
        System.out.print("Enter the Book Price :");
        double b_price = scan.nextDouble();
        System.out.print("Enter the Quantity of the book :");
        int b_stock= scan.nextInt();
        try {
            bookDAO.addBook(b_title, b_auth, b_type,0,b_stock,b_price);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("Book added Successfully....:)");
        orgAdmin();
    }

    // ------------------- book list ---------------------//
    private static void bookDetails() {
        try {
            BookDAO.displayBooks();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        orgAdmin();
    }

    // ---------------------------------- return fine ----------------------- //
    private static void return_fine() {
        try {
            returnDAO.displayreturnList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        orgAdmin();
    }

    // ----------------------------- borrow details ------------------------------//
    private static void borrow_details() {
        try {
            borrowDAO.displayborrowList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        orgAdmin();
    }

    // -----------------------delete book----------------------//
    private static void deleteBook() {
        try {
            bookDAO.deleteBook();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        orgAdmin();
    }

    private static void searchBook() {
        try {
            bookDAO.searchBook();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        orgAdmin();
    }

    // ---------------------------- User Details -------------------------//
    private static void userDetails() {
        try {
            userDAO.displayuserList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        orgAdmin();
    }

    // ---------------------------------users-------------------------------//
    private static void user() {
        System.out.println("\033[H\033[2J");
        System.out.flush();
        System.out.println("\t------------Welcome Library Users-----------");
        System.out.println("1.New User");
        System.out.println("2.Existing User");
        System.out.println("3.Exit");
        System.out.print("Enter your choice :");
        int ch = scan.nextInt();
        switch (ch) {
            case 1:
                newUser();
                break;
            case 2:
                existUser();
                break;
            case 3:
                library_home();
                break;
            default:
                System.out.println("Enter valid Input");
        }

    }

    // --------------------------------- New User -----------------------------------//
    private static void newUser() {
        try {
            userDAO.createUser();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        user();
    }

    // ------------------------- existing user ------------------------//
    private static void existUser() {
        try {
            userDAO.existUser();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    static void orgUser() {
        System.out.println("Welcome " + "USER SECTION"+ " in Library.....:)");
        System.out.println("1.Borrow Book");
        System.out.println("2.Borrow History");
        System.out.println("3.Return");
        System.out.println("4.Wallet");
        System.out.println("5.Add Money in your Wallet");
        System.out.println("6.Exit");
        System.out.print("Enter your choice :");
        int choice = scan.nextInt();
        switch (choice) {
            case 1:
                borrow_book();
                break;
            case 2:
                borrow_history();
                break;
            case 3:
                book_return();
                break;
            case 4:
                wallet();
                break;
            case 5:
                addMoney();
                break;
            case 6:
                user();
                break;
            default:
                System.out.println("Enter valid Input.....");
                orgUser();
                break;
        }
    }

    private static void addMoney() {
        try {
            userDAO.addMoney();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        orgUser();
    }

    private static void borrow_book() {
        try {
            borrowDAO.borrowBook();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        orgUser();
    }

    private static void borrow_history() {
        try {
            borrowDAO.displayuserBorrowList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        orgUser();
    }

    private static void book_return() {
        System.out.println("---------- Book Return Section -------");
        System.out.println("1.Book Return");
        System.out.println("2.Book Miss");
        System.out.print("Enter Your Choice :");
        int ch = scan.nextInt();
        if (ch == 1) {
            book_ret();
        } else if (ch == 2) {
            book_miss();
        } else {
            System.out.println("Please enter Valid input....");
            book_return();
        }
        orgUser();

    }

    // -------------------- book-return -------------------------//
    private static void book_ret() {
        try {
            returnDAO.returnBook();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        orgUser();
    }

    // -------------------- book Miss -------------------------//
    private static void book_miss() {
        try {
            returnDAO.bookMiss();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        orgUser();
    }

    private static void wallet() {
        try {
            userDAO.walletCheck(user_id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        orgUser();
    }

}

