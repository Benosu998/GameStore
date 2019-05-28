
import jdk.nashorn.internal.codegen.CompilerConstants;

import javax.print.DocFlavor;
import javax.xml.crypto.Data;
import java.sql.*;

public class BasicController {

    public Boolean checkClient(String username, String password) throws SQLException {
        try (

                Statement stmt = Database.getConnection().createStatement();
                ResultSet rs = stmt.executeQuery("select '1' from clients where username = '" + username + "' and  password = '" + password + "'")) {
                Boolean result = rs.next();
                return result;
        }
    }

    public Boolean register(String username, String password, String email) throws SQLException {
        Connection con = Database.getConnection();
        String sql = "{ ? = call registerF(?,?) }";
        CallableStatement statement = con.prepareCall(sql);
        statement.setString(2, username);
        statement.setString(3, email);
        statement.registerOutParameter(1, Types.VARCHAR);
        statement.execute();
        String result = statement.getString(1);
        System.out.println(result);
        if (result.equals("Succes")) {
            System.out.println("asd");
//            stmt.execute("begin registerP("+username+","+password+","+email+"); end ");
            CallableStatement myCall = con.prepareCall("{call registerP(?,?,?)}");
            myCall.setString(1, username);
            myCall.setString(2, password);
            myCall.setString(3, email);
            myCall.executeUpdate();
            return true;
        }
        return false;
    }

    Boolean addFunds(int Value, String name, String paymentMethod) throws SQLException {
        Connection con = Database.getConnection();
        String sql = "{call addFunds(?,?,?)}";
        CallableStatement myCall = con.prepareCall(sql);
        myCall.setString(1, name);
        myCall.setInt(2, Value);
        myCall.setString(3, paymentMethod);
        myCall.executeUpdate();
        Database.commit();
        return true;
    }

    String getFunds(String username) throws SQLException {
        String sql = "{ ? = call wallet(?)}";
        return databaseReq(sql,username);
    }
    String getGames(String username) throws SQLException {
        String sql = "{ ? = call get_games(?)}";
        return databaseReq(sql,username);
    }
    String getGameName(int id) throws SQLException{
        String sql = "{ ? = call get_gameName(?)}";
        CallableStatement stmt = Database.getConnection().prepareCall(sql);
        stmt.setInt(2,id);
        stmt.registerOutParameter(1,Types.VARCHAR);
        stmt.execute();
        stmt.closeOnCompletion();
        String result = stmt.getString(1);
        return result;
    }
    String getGameComments(int id) throws SQLException {
        String sql = "{ ? = call getComents(?)}";
        CallableStatement stmt = Database.getConnection().prepareCall(sql);
        stmt.setInt(2,id);
        stmt.registerOutParameter(1,Types.VARCHAR);
        stmt.execute();
        stmt.closeOnCompletion();
        String result = stmt.getString(1);
        return result;
    }
    String getGamesByName(String game) throws SQLException {
        String sql = "{ ? = call get_games_by_name(?)}";
        return databaseReq(sql,game);
    }
    String getCategories() throws  SQLException {
        String sql = "{? = call get_categories()}";
        return databaseReq(sql,null);
    }
    String getGamesByCategory(String category) throws SQLException{
        String sql = "{? = call get_games_by_category(?)}";
        return databaseReq(sql,category);
    }
    String databaseReq(String sql,String par) throws  SQLException{
        CallableStatement stmt = Database.getConnection().prepareCall(sql);
        if(par != null) stmt.setString(2,par);
        stmt.registerOutParameter(1,Types.VARCHAR);
        stmt.execute();
        stmt.closeOnCompletion();
        String result = stmt.getString(1);
        return result;
    }
    String getGameFranchises() throws  SQLException{
        String sql = "{? = call getGameFranchises()}";
        return databaseReq(sql,null);
    }
    String getHighRated() throws  SQLException{
        String sql = "{? = call get_hot()}";
        return databaseReq(sql,null);

    }
    String showHistory(String name) throws  SQLException{
        String sql = "{? = call showHistory(?)}";
        return databaseReq(sql,name);
    }
    String getMost() throws  SQLException{
        String sql = "{? = call get_most_bought()}";
        return databaseReq(sql,null);
    }
    String getSales() throws  SQLException{
        String sql = "{? = call get_sales()}";
        return databaseReq(sql,null);
    }
    String getPrice(String game) throws  SQLException{
        String sql = "{? = call get_price(?)}";
        return databaseReq(sql,game);
    }
}