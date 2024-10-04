import java.util.*;

public class BOOKMYSHOWManagament {
    static Scanner scan;
    static UserDAO userDAO = new UserDAO();
    static BookedHistoryDAO bhDAO = new BookedHistoryDAO();
    static List<User> userList;
    static Map<String,User> userMap;

    static int userId;

    //USE SINGLETON PATTERN TO OBJECT CREATION DONE AT ONCE
    public static volatile BOOKMYSHOWManagament managament; //STEP 1:OBJECT INSTANSIATED

    private BOOKMYSHOWManagament() {// STEP 2 PRIVATE CONSTRUCTOR
        scan = new Scanner(System.in);
        userList = new ArrayList<>();
        userMap = new HashMap<>();
    }

    public static BOOKMYSHOWManagament getmanagement() {
        if (managament==null){
            synchronized (BOOKMYSHOWManagament.class){
                if (managament==null){
                    managament = new BOOKMYSHOWManagament();
                }
            }
        }
        return managament;
    }//STEP 3: METHOD TO GET OBJECT

    // CHOOSE CHOICE TO LOGIN OR REGISTER
    public static void receptionSection() {
        System.out.println("--WELCOME TO BOOKMYSHOW APPLICATION");
        System.out.println("1-->ALREADY HAVE ACCOUNT --> LOGIN");
        System.out.println("2-->DON'T HAVE ACCOUNT --> REGISTER");
        System.out.println("3-->EXIT");
        int choice = scan.nextInt();
        switch (choice){
            case 1:
                try {
                    loginMenu();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            case 2:
                try {
                    registerMenu();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            case 3:
                System.out.println("THANK YOU");
                System.exit(0);
                break;
            default:
                System.out.println("ENTER VALID CHOICE");
                receptionSection();
                break;
        }
    }

    // LOGIN IF EXISTING USER
    public static void loginMenu() throws Exception{
        System.out.println("Enter your Email and Password To Login..");
        String email = scan.next();
        String password = scan.next();
        if(userDAO.isCorrectUser(email,password)){
                //System.out.println(userId);
                System.out.println("LOGGED IN SUCCESSFULLY ..");
                orgUserMenu();
            }
        else {
            System.out.println("ENTER VALID DETAILS");
            loginMenu();
        }
    }

    // REGISTER IF NEW USER
    public static void registerMenu() throws Exception {
        System.out.println("Enter Name ,Phonenumber, amount to store your wallet, Mailid and set your Password To Register");
        String name = scan.next();
        String phoneNumber = scan.next();
        int wallet = scan.nextInt();
        String mailId = scan.next();
        String password = scan.next();
        if (!userDAO.isValid(phoneNumber,mailId,password)){
            userDAO.addUser(name,phoneNumber,wallet,mailId,password);
            System.out.println("USER ADDED SUCCESSFULLY..");
            System.out.println();
        }
        else {
            System.out.println("USER ALREADY EXISTS...TRY AGAIN");
            BOOKMYSHOWManagament.registerMenu();
        }
        System.out.println("NOW TRY TO LOGIN");
        receptionSection();
    }

    // MAIN SECTION TO CHOOSING CHOICE LIKE MOVIE,PLAYS,EVENTS ETC.,
    public static void orgUserMenu(){
        System.out.println("WELCOME BOOKING SECTION");
        System.out.println("BOOK YOUR ONLINE TICKET FROM BELOW CATOGORIES..");

        System.out.println("1-->MOVIES");//COMEDY , ACTIONS MOVIES ETC.,
        System.out.println("2-->EVENTS");//MUSIC SHOWS ,COMEDY SHOWS, KIDS SHOWS ETC .,
        System.out.println("3-->SEE BOOKED HISTORY");
        System.out.println("4-->SHOW TICKETS");
        System.out.println("5-->ADD MONEY IN WALET");
        System.out.println("6-->DISPLAY WALLET");
        System.out.println("7-->EXIT");

        int choice = scan.nextInt();
        switch (choice){
            case 1:
                try {
                    MoviesSection.bookMovie();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            case 2:
                try {
                    EventsSection.bookEvent();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            case 3:
                try {
                    bookedHistory();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            case 4:
                try {
                    showTicket();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            case 5:
                try {
                    addMoneyInWallet();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            case 6:
                try {
                    displayWallet();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            case 7:
                receptionSection();
                break;
            default:
                System.out.println("ENTER VALID CHOICE");
                orgUserMenu();
                break;
        }
    }

    // DISPLAY BOOKED HISTORY
    private static void bookedHistory() throws Exception{
        int bookExit=bhDAO.showBookedHistory(userId);
        if (bookExit==0){
            System.out.println("Booking History is empty..");
            orgUserMenu();
        }
        orgUserMenu();
    }

    // SHOW THE BOOKED TICKET
    private static void showTicket() throws Exception{
        int bookExit = bhDAO.showBookedHistory(userId);
        if (bookExit!=0){
            System.out.println("Enter booking id to see your ticket");
            int bookId = scan.nextInt();
            boolean found = bhDAO.ticketShowingProcess(userId,bookId);
            if (!found) {
                System.out.println("ENTER VALID BOOKING ID");
                showTicket();
            }
        }
        else{
            System.out.println("BOOKING HISTORY IS EMPTY..");
            orgUserMenu();
        }
    }

    // DISPLAY MONEY IN WALLET
    private static void displayWallet() throws Exception{
        userDAO.displayWallet(userId);
        orgUserMenu();
    }

    // ADD MONEY IN WALLET
    public static void addMoneyInWallet() throws Exception{
        userDAO.addMoneyInWallet(userId);
        orgUserMenu();
    }

}
