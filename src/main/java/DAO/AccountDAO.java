package DAO;

import Model.Account;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
// import java.util.ArrayList;
// import java.util.List;
import java.sql.Statement;

// import Model.Account;
import Util.ConnectionUtil;
// import kotlin.Result;
// import java.util.Optional;
public class AccountDAO  {
    // account_id int primary key auto_increment,
    // username varchar(255) unique,
    // password varchar(255)
    public Account userLogin(Account account) {
        String sql = "SELECT * FROM account WHERE username=? AND password=?";
        
        try(Connection connection = ConnectionUtil.getConnection()){
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1,account.getUsername());
            ps.setString(2,account.getPassword());
             
             ResultSet rs = ps.executeQuery();;
            while(rs.next()){
            
             Account acc = new Account(rs.getInt("account_id"),rs.getString("username"), rs.getString("password"));
                return acc;
                
            }
                
            
            
            
        }
    catch(SQLException e){
        System.out.println(e.getMessage());
    }
    return null;
}




public Account registerUser(Account account){
    String sql = "INSERT INTO account(username, password) VALUES(?,?)";
    try(Connection connection = ConnectionUtil.getConnection()){
        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        // ps.setInt(1,account.getAccount_id());
        ps.setString(1,account.getUsername());
        ps.setString(2,account.getPassword());
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if(rs.next()){
            int generated_account_id = (int)rs.getInt(1);
            return new Account(generated_account_id,account.getUsername(), account.getPassword());
        }
        
       
       
    }
    catch(SQLException e){
        System.out.println(e.getMessage());
    }
    return null;
}

}