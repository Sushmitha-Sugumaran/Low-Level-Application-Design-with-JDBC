import java.sql.*;

public class EventsDAO {
    public static void displayEvents(String schedule) throws Exception{
        Connection con = JDBC.getConnection();
        String query = "SELECT * FROM Events WHERE eschedule = ?";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setString(1, schedule);
        ResultSet rs = pst.executeQuery();

        System.out.println(String.format("%-15s %-15s %-15s %-15s %-15s  %-20s %-15s",
                "Event ID", "Event Schedule", "Event Time","Event Lang","Event Name","Event genre","Tickets", "Per Ticket Cost"));
        boolean foundEvents = false;
        while (rs.next()){
            foundEvents = true;
            System.out.println(String.format("%-15s %-15s %-15s %-15s %-15s %-20s %-15s",rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getInt(7),rs.getInt(8)));
        }
        if (!foundEvents) {
            System.out.println("No events found for the given schedule.");
            BOOKMYSHOWManagament.orgUserMenu();
        }
    }

    public static int getEventID(String event, String schedule, String language, String genre) throws Exception{
        int eventID = -1 ;
        Connection con = JDBC.getConnection();
        String query = "SELECT * FROM Events WHERE ename = ? AND eschedule = ? AND elan = ? AND egenre = ?";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setString(1, event);
        pst.setString(2,schedule);
        pst.setString(3,language);
        pst.setString(4,genre);
        ResultSet rs = pst.executeQuery();

        while (rs.next()){
            eventID = rs.getInt(1);
            break;
        }
        return eventID;
    }

    public static int getTotalTickets(int eventId) throws Exception{
        int tickets = -1 ;
        Connection con = JDBC.getConnection();
        Statement st = con.createStatement();
        String query = "SELECT * FROM Events WHERE id = "+ eventId;
        ResultSet rs = st.executeQuery(query);
        while (rs.next()){
            tickets =rs.getInt(7);
        }
        return tickets;
    }

    public static String getShowTime(int eventId) throws Exception{
        String schedule = null;
        Connection con = JDBC.getConnection();
        Statement st = con.createStatement();
        String query = "SELECT * FROM Events WHERE id = "+ eventId;
        ResultSet rs = st.executeQuery(query);
        while (rs.next()){
            schedule=rs.getString(3);
        }
        return schedule;
    }

    public static boolean isValid(String language, String genre, String event) throws Exception{
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        con = JDBC.getConnection();
        String query = "SELECT * FROM Events WHERE elan =? AND egenre=? AND ename=?";
        pst = con.prepareStatement(query);
        pst.setString(1, language);
        pst.setString(2,genre);
        pst.setString(3, event);

        rs = pst.executeQuery();

        return rs.next();
    }

}
