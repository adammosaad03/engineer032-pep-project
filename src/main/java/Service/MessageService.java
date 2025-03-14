package Service;
import java.util.List;
import DAO.MessagesDAO;
import Model.Message;
public class MessageService {
    MessagesDAO messageDAO;
    
    public MessageService(){
        messageDAO = new MessagesDAO();
    }
    
    public MessageService(MessagesDAO messageDAO){
        this.messageDAO = messageDAO;
    }
    
    public Message createMessage(Message message){
        if(message.getMessage_text().length() > 0 && message.getMessage_text().length() < 255){
            return messageDAO.createMessage(message);
        
            
        }
        return null;
    }
    
    public Message deleteMessageById(int id){        
         if(messageDAO.getMessageById(id) == null){
            return null;
         }
         else{
            // messageDAO.deleteMessageById(id);
            
            return messageDAO.getMessageById(id);

            
         }
        
        
    }
    
    public List<Message> getAllMessages(){
        List<Message> messages = messageDAO.getAllMessages();
        return messages;
    }
    
    public List<Message> getAllMessagesByAccountId(Integer id){
        return messageDAO.getAllMessagesByAccountId(id);
    
    }
    
    public Message updateMessage(Message message, int id){
        if(message.getMessage_text().length() == 0 || message.getMessage_text().length() > 255 || message.getMessage_text() == null){
            return null;
           
        }
        else if (messageDAO.getMessageById(id) == null){
            return null;
        }
          else{
            
           Message msg = messageDAO.getMessageById(id);
           
               msg.setMessage_text(message.getMessage_text());
               return msg;

        }
        
    }
    public Message getMessageById(int id){
    if (messageDAO.getMessageById(id) == null){
        return null;
        
    }
    else{
        return messageDAO.getMessageById(id);
    }
    
    }
    
    
    
}
