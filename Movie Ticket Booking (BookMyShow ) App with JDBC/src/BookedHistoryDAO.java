import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BookedHistoryDAO {
    static LocalDateTime dateFormat;
    static String todaytime;
    static DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("E, MMM dd yyyy HH:mm:ss");

    public static void addDetails(int userId, String showTime, String userName, String movie, String language, int tickets, int totalTicketPrice) throws Exception{
        dateFormat = LocalDateTime.now();
        todaytime = myFormatObj.format(dateFormat);
        int bookId = 0;
        Connection con = JDBC.getConnection();
        String query = "INSERT INTO BookedHistory(u_id,showTime,todayTime,u_name,m_name,m_lang,tickets,amount) VALUES(?,?,?,?,?,?,?,?)";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1,userId);
        pst.setString(2,showTime);
        pst.setString(3,todaytime);
        pst.setString(4,userName);
        pst.setString(5,movie);
        pst.setString(6,language);
        pst.setInt(7,tickets);
        pst.setInt(8,totalTicketPrice);
        pst.executeUpdate();

        con.close();
        pst.close();

    }

    public static int getBookID(int userId, String showTime, String movie, int tickets) throws Exception{
        int bookId = -1;
        Connection con = JDBC.getConnection();
        String query = "SELECT * FROM BookedHistory WHERE u_id = ?";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1, userId);
        ResultSet rs = pst.executeQuery();
        while (rs.next()){
            if (rs.getString(3).equals(showTime) && rs.getString(6).equals(movie) && rs.getInt(8)==tickets){
                bookId = rs.getInt(2);
            }
        }
        return bookId;
    }

    public int showBookedHistory(int userId) throws Exception{
        int bookExit=0;
        Connection con = JDBC.getConnection();
        Statement st = con.createStatement();
        String query = "SELECT * FROM BookedHistory WHERE u_id = "+userId;
        ResultSet rs = st.executeQuery(query);

        System.out.println(String.format("%-15s%-15s%-30s%-15s%-15s%-15s%-15s",
                "Booking ID", "Show Time", "Today Time", "Movie Name", "Movie Lang", "Tickets", "Total Ticket Cost"));
        while (rs.next()){
                bookExit++;
                System.out.println(String.format("%-15s%-15s%-30s%-15s%-15s%-15s%-15s",rs.getInt(2) ,rs.getString(3),rs.getString(4),rs.getString(6),
                        rs.getString(7),rs.getInt(8), rs.getInt(9)));
        }
        return bookExit;
    }

    public boolean ticketShowingProcess(int userId, int bookId) throws Exception{
        boolean found = false;
        Connection con = JDBC.getConnection();
        String  query = "SELECT * FROM BookedHistory WHERE u_id =? AND booking_id=? ";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1,userId);
        pst.setInt(2,bookId);
        ResultSet rs = pst.executeQuery();

        while (rs.next()){
                showTicket(userId,bookId);
                found = true;
                BOOKMYSHOWManagament.orgUserMenu();
            }

        con.close();
        pst.close();
        rs.close();

        return found;
    }

    public static void showTicket(int userId, int bookId) throws Exception {
        Connection con = JDBC.getConnection();
        String  query = "SELECT * FROM BookedHistory WHERE u_id =? AND booking_id=? ";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1,userId);
        pst.setInt(2,bookId);
        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            System.out.println("------------------------------------------------------------------");
            System.out.println("---------------------------MOVIE TICKET---------------------------");
            System.out.println("--------------TODAY DATE : "+rs.getString(4)+"--------------");
            System.out.println("----------------------BOOKED ID : "+rs.getInt(2)+"----------------------------");
            System.out.println("----------------------SHOW TIME : "+rs.getString(3)+"--------------------------");
            System.out.println("----------------------BOOKER NAME : "+rs.getString(5)+"------------------------");
            System.out.println("----------------------MOVIE NAME : "+rs.getString(6)+"-------------------------");
            System.out.println("----------------------LANGUAGE : "+rs.getString(7)+"----------------------------");
            System.out.println("----------------------TICKETS : "+rs.getInt(8)+"---------------------------------");
            System.out.println("--------------TOTAL PRICE OF THE TICKET : "+rs.getInt(9)+"---------------------");
            System.out.println("------------------------------------------------------------------");
        }

        con.close();
        pst.close();
        rs.close();
    }
}
