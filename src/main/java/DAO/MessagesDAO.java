package DAO;
import Model.Message;
import Util.ConnectionUtil;
// import net.bytebuddy.description.annotation.AnnotationValue.State;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

// import org.junit.runners.model.Statement;
import java.sql.Statement;

// import org.junit.runners.model.Statement;

import java.sql.Connection;
import java.util.ArrayList;

// message_id int primary key auto_increment,
//     posted_by int,
//     message_text varchar(255),
//     time_posted_epoch bigint,
public class MessagesDAO {
    
    public Message createMessage(Message message){
        
        try(Connection connection = ConnectionUtil.getConnection()){
            String sql = "INSERT INTO message(posted_by, message_text, time_posted_epoch) VALUES(?,?,?)";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1,message.getPosted_by());
            ps.setString(2,message.getMessage_text());
            ps.setLong(3,message.getTime_posted_epoch());
             ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            while(rs.next()){
                int generated_message_id = (int)rs.getInt(1);
                return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    public void deleteMessageById(int id){
        String sql = "DELETE FROM message WHERE message_id=?";
        
        try(Connection connection = ConnectionUtil.getConnection()){
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1,id);
            
             ps.executeUpdate();
            
               
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

        
    }
    
    public List<Message> getAllMessagesByAccountId(Integer id){
        String sql = "SELECT * FROM message WHERE message_id=?";
        
        List<Message> messages = new ArrayList<Message>();
        try(Connection connection = ConnectionUtil.getConnection()){
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1,id);
            ResultSet rs  = ps.executeQuery();
            while(rs.next()){
                Message messageTo = new Message(rs.getInt("message_id"),rs.getInt("posted_by"), rs.getString("message_text"), rs.getInt("time_posted_epoch"));
                messages.add(messageTo);
                return messages;
            }        
            
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
            
        }
        return messages;
    }
    
    
    public List<Message> getAllMessages(){
        String sql = "SELECT * FROM message";
        
        List<Message> messages = new ArrayList<Message>();
        try(Connection connection = ConnectionUtil.getConnection()){
            PreparedStatement  ps = connection.prepareStatement(sql);
            
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Message acc = new Message(rs.getInt("message_id"),rs.getInt("posted_by"), rs.getString("message_text"),rs.getInt("time_posted_epoch"));
                messages.add(acc);
                
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    } 
    
    public Message getMessageById(int id){
        String sql = "SELECT * FROM message WHERE message_id=?";
        
        try(Connection connection = ConnectionUtil.getConnection()){
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Message msg = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getInt("time_posted_epoch"));
                return msg;
            }
            
        }
    
    catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    public void updateMessage(Message message,int id){
        String sql = "UPDATE message SET message_text=? WHERE message_id=?";
        
        try(Connection connection = ConnectionUtil.getConnection()){
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1,message.getMessage_text());
            ps.setInt(2,id);
            ps.executeUpdate();
          
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        
    }
    
    
}
