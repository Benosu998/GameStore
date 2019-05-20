import java.sql.*;

public class BasicController {
    public String findById(int id) throws SQLException {
        Connection con = Database.getConnection();
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("select username from clients where id = '" + id + "'")) {
            return rs.next() ? rs.getString(1) : null;
        }
    }
}