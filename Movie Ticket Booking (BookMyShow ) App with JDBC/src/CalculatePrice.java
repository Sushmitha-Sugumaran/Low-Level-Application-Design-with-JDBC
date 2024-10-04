import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public class CalculatePrice {
    public static int moviePay(int movieId, int userId, int tickets) throws Exception{
        int amount = 0;
        Connection con = JDBC.getConnection();
        Statement st = con.createStatement();
        String query = "UPDATE Movies SET seatCount = ? WHERE id = ?";
        String query1 = "SELECT * FROM Movies Where id = "+ movieId;
        ResultSet rs = st.executeQuery(query1);
        int u_tickets = tickets;
        while (rs.next()){
            tickets=rs.getInt(7)-tickets;
            amount = rs.getInt(8);
        }

        amount*=u_tickets;
        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1,tickets);
        pst.setInt(2,movieId);
        pst.executeUpdate();
        return amount;
    }

    public static int eventPay(int eventId, int userId, int tickets) throws Exception{
        int amount = 0;
        Connection con = JDBC.getConnection();
        Statement st = con.createStatement();
        String query = "UPDATE Events SET seatCount = ? WHERE id = ?";
        String query1 = "SELECT * FROM Events Where id = "+ eventId;
        ResultSet rs = st.executeQuery(query1);
        int u_tickets = tickets;
        while (rs.next()){
            tickets=rs.getInt(7)-tickets;
            amount = rs.getInt(8);
        }

        amount*=u_tickets;
        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1,tickets);
        pst.setInt(2,eventId);
        pst.executeUpdate();
        return amount;
    }
}
