import java.util.*;

public class MoviesSection {
    static int movieId;
    static Scanner scan = new Scanner(System.in);

    public static void bookMovie() throws Exception {
        System.out.println("Enter your show schedule timing that you want");
        System.out.println("1-->MORNING");
        System.out.println("2-->AFTERNOON");
        System.out.println("3-->EVENING");
        System.out.println("4-->NIGHT");
        System.out.println("5-->EXIT");
        System.out.println("Enter Your Choice");
        int choice = scan.nextInt();
        switch (choice) {
            case 1:
                String schedule1 = "Morning";
                bookingPart(schedule1);
                break;
            case 2:
                String schedule2 = "Afternoon";
                bookingPart(schedule2);
                break;
            case 3:
                String schedule3 = "Evening";
                bookingPart(schedule3);
                break;
            case 4:
                String schedule4 = "Night";
                bookingPart(schedule4);
                break;
            case 5:
                BOOKMYSHOWManagament.orgUserMenu();
                break;
            default:
                System.out.println("ENTER VALID CHOICE");
                bookMovie();
                break;
        }
    }

    private static void bookingPart(String schedule) throws Exception {
        MoviesDAO.displayMovies(schedule);
        String language = chooseLanguage();
        String genre = chooseGenre();
        String movie = chooseMovie(schedule,language,genre);
        int tickets = chooseTickets();
        String showTime = MoviesDAO.getShowTime(movieId);
        String userName =UserDAO.getUserName(BOOKMYSHOWManagament.userId);
        boolean found = true;
        if (MoviesDAO.isValid(language,genre,movie)) {

            int totalTicketPrice = CalculatePrice.moviePay(movieId,BOOKMYSHOWManagament.userId,tickets);
            int wallet = UserDAO.getWalletAmount(BOOKMYSHOWManagament.userId);

            if (totalTicketPrice<=wallet) {

                System.out.println("TICKET BOOKED SUCCESSFULLY");

                BookedHistoryDAO.addDetails(BOOKMYSHOWManagament.userId,showTime,userName,movie,language,tickets,totalTicketPrice);
                int bookId = BookedHistoryDAO.getBookID(BOOKMYSHOWManagament.userId,showTime,movie,tickets);
                //System.out.println(bookId);

                UserDAO.updateWallet(BOOKMYSHOWManagament.userId,totalTicketPrice);

                BookedHistoryDAO.showTicket(BOOKMYSHOWManagament.userId,bookId);
            }
            else {
                System.out.println("ADD MONEY IN YOUR WALLET THEN BOOK YOUR TICKET");
                BOOKMYSHOWManagament.addMoneyInWallet();
                bookingPart(schedule);
            }
        }
        else {
            System.out.println("ENTER VALID DETAILS");
            bookingPart(schedule);
        }
        BOOKMYSHOWManagament.orgUserMenu();
    }

    private static String chooseLanguage() {
        System.out.println("Enter Language");
        String language = scan.next();
        return language;
    }

    private static String chooseGenre() {
        System.out.println("Enter Genre");
        String genre = scan.next();
        return genre;
    }

    private static String chooseMovie(String Schedule, String language, String genre) throws Exception{
        System.out.println("Enter Movie Name");
        String movie = scan.next();
        movieId = MoviesDAO.getMovieID(movie,Schedule,language,genre);
        //System.out.println(movieId);
        return movie;
    }

    private static int chooseTickets() throws Exception{
        int totalTickets = MoviesDAO.getTotalTickets(movieId);
        System.out.println("Total Tickets For this Movie : "+totalTickets);
        System.out.println("Enter ticket count that you want");
        int tickets = scan.nextInt();
        if (tickets>totalTickets){
            System.out.println("CHOOSE VALID TICKET COUNT WHICH SHOULD BE LESS THAN TOTAL TICKETS");
            chooseTickets();
        }
        return tickets;
    }

}
