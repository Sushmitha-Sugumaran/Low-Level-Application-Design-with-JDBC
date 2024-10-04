import java.sql.*;

public class MoviesDAO {
    public static void displayMovies(String schedule) throws Exception{
        Connection con = JDBC.getConnection();
        String query = "SELECT * FROM Movies WHERE mschedule = ?";
         PreparedStatement pst = con.prepareStatement(query);
        pst.setString(1, schedule);
        ResultSet rs = pst.executeQuery();

        System.out.println(String.format("%-15s %-15s %-15s %-15s %-15s  %-20s %-15s",
                "Movie ID", "Movie Schedule", "Movie Time","Movie Lang","Movie Name","Movie genre","Tickets", "Per Ticket Cost"));
        boolean foundMovies = false;
        while (rs.next()){
            foundMovies = true;
                System.out.println(String.format("%-15s %-15s %-15s %-15s %-15s %-20s %-15s",rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getInt(7),rs.getInt(8)));
            }
        if (!foundMovies) {
            System.out.println("No movies found for the given schedule.");
            BOOKMYSHOWManagament.orgUserMenu();
        }
    }

    public static int getMovieID(String movie, String schedule, String language, String genre) throws Exception{
        int movieID = -1 ;
        Connection con = JDBC.getConnection();
        String query = "SELECT * FROM Movies WHERE mname = ? AND mschedule = ? AND mlan = ? AND mgenre = ?";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setString(1,movie);
        pst.setString(2,schedule);
        pst.setString(3,language);
        pst.setString(4,genre);
        ResultSet rs = pst.executeQuery();

        while (rs.next()){
            movieID = rs.getInt(1);
            break;
            }
        return movieID;
    }

    public static int getTotalTickets(int movieId) throws Exception{
        int tickets = -1 ;
        Connection con = JDBC.getConnection();
        Statement st = con.createStatement();
        String query = "SELECT * FROM Movies WHERE id = "+movieId;
        ResultSet rs = st.executeQuery(query);
        while (rs.next()){
            tickets =rs.getInt(7);
        }
        return tickets;
    }

    public static String getShowTime(int movieId) throws Exception{
        String schedule = null;
        Connection con = JDBC.getConnection();
        Statement st = con.createStatement();
        String query = "SELECT * FROM Movies WHERE id = "+movieId;
        ResultSet rs = st.executeQuery(query);
        while (rs.next()){
            schedule=rs.getString(3);
        }
        return schedule;
    }

    public static boolean isValid(String language, String genre, String movie) throws Exception{
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        con = JDBC.getConnection();
        String query = "SELECT * FROM Movies WHERE mlan =? AND mgenre=? AND mname=?";
        pst = con.prepareStatement(query);
        pst.setString(1, language);
        pst.setString(2,genre);
        pst.setString(3,movie);

        rs = pst.executeQuery();

        return rs.next();
    }
}
