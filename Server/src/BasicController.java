import java.sql.*;

public class BasicController {
    public Boolean checkClient(String username, String password) throws SQLException {
        Connection con = Database.getConnection();
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("select '1' from clients where username = '" + username + "' and  password = '" + password + "'")) {
            return rs.next() ? true : false;
        }
    }

    public Boolean register(String username, String password, String email) throws SQLException {
        Connection con = Database.getConnection();
        Statement stmt = con.createStatement();
        String sql = "{ ? = call registerF(?,?) }";
        CallableStatement statement = con.prepareCall(sql);
        statement.setString(2,username);
        statement.setString(3,email);
        statement.registerOutParameter(1, Types.VARCHAR);
        statement.execute();
        String result = statement.toString();
        if(result.equals("Succes")){
            System.out.println("asd");
            stmt.execute("begin registerP("+username+","+password+","+email+"); end ");
            return true;
        }
        return false;
    }
}