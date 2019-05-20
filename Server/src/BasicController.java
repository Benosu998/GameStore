import java.sql.*;

public class BasicController {
    public Boolean checkClient(String username,String password) throws SQLException {
        Connection con = Database.getConnection();
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("select '1' from clients where username = '" + username + "' and  password = '" + password + "'")) {
            return rs.next() ? true : false;
        }
    }
}