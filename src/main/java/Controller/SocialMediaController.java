package Controller;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

// import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

// import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

// import org.eclipse.jetty.http.HttpTester.Message;

// import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
// import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;
    
    public SocialMediaController(){
        accountService = new AccountService();
        messageService = new MessageService();
    }
    
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     * 
     * 
     * 
As a user, I should be able to create a new Account on the endpoint POST localhost:8080/register. The body will contain a representation of a JSON Account, but will not contain an account_id.

- The registration will be successful if and only if the username is not blank, the password is at least 4 characters long, and an Account with that username does not already exist. If all these conditions are met, the response body should contain a JSON of the Account, including its account_id. The response status should be 200 OK, which is the default. The new account should be persisted to the database.
- If the registration is not successful, the response status should be 400. (Client error)
     */
    
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::createMessage);
        app.get("/messages", this::getAllMessages);
        app.delete("messages/{message_id}", this::deleteMessageById);
        app.get("messages/{message_id}",this::getAllMessagesById);
        app.patch("messages/{message_id}", this::updateMessage);
        app.get("accounts/{account_id}/messages", this::getAllMessageByAccountId);
        return app;
    }
    private void registerHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account acc = mapper.readValue(ctx.body(), Account.class);
        Account newAcc = accountService.createUser(acc);
        System.out.println(acc);
        if(newAcc == null){
            ctx.status(400);
        }
        else{
            ctx.json(mapper.writeValueAsString(newAcc));
        }
    } 
    
    
//     As a user, I should be able to verify my login on the endpoint POST localhost:8080/login. The request body will contain a JSON representation of an Account, not containing an account_id. In the future, this action may generate a Session token to allow the user to securely use the site. We will not worry about this for now.

// // - The login will be successful if and only if the username and password provided in the request body 
// JSON match a real account existing on the database.
//  If successful, the response body should contain a JSON of the account in the response body, 
//  including its account_id. The response status should be 200 OK, which is the default.
// // - If the login is not successful, the response status should be 401. (Unauthorized)

private void loginHandler(Context ctx) throws JsonProcessingException{
    ObjectMapper mapper = new ObjectMapper();
    Account acc = mapper.readValue(ctx.body(), Account.class);
    Account login = accountService.userLogin(acc);
    if(login == null){
        ctx.status(401);
    }else{
        ctx.json(mapper.writeValueAsString(login));
    }
}


// As a user, I should be able to submit a new post on the endpoint POST localhost:8080/messages. The request body will contain a JSON representation of a message, which should be persisted to the database, but will not contain a message_id.

// - The creation of the message will be successful if and only if the message_text is not blank, is not over 255 characters, and posted_by refers to a real, existing user. If successful, the response body should contain a JSON of the message, including its message_id. The response status should be 200, which is the default. The new message should be persisted to the database.
// - If the creation of the message is not successful, the response status should be 400. (Client error)

private void createMessage(Context ctx) throws JsonProcessingException{
    ObjectMapper mapper = new ObjectMapper();
    Message msg = mapper.readValue(ctx.body(), Message.class);
    Message newMsg = messageService.createMessage(msg);
    if(newMsg == null){
        ctx.status(400);
    }else{
        ctx.json(mapper.writeValueAsString(newMsg));
    }
    
}


// As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/messages.

// - The response body should contain a JSON representation of a list containing all messages retrieved from the database. 
// It is expected for the list to simply be empty if there are no messages. 
// The response status should always be 200, which is the default.

private void getAllMessages(Context ctx){
    ctx.json(messageService.getAllMessages());
}


// As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/messages/{message_id}.

// - The response body should contain a JSON representation of the message identified by the message_id.
//  It is expected for the response body to simply be empty if there is no such message. 
//  The response status should always be 200, which is the default.

private void getAllMessagesById(Context ctx){
    if(messageService.getMessageById(Integer.parseInt(ctx.pathParam("message_id"))) == null){
        ctx.status(200);
    }else{
        
        ctx.json(messageService.getMessageById(Integer.parseInt(ctx.pathParam("message_id"))));
    }
    
}

// As a User, I should be able to submit a DELETE request on the endpoint DELETE localhost:8080/messages/{message_id}.

// - The deletion of an existing message should remove an existing message from the database. 
// If the message existed, the response body should contain the now-deleted message. The response status should be 200, 
//  is the default.
// // - If the message did not exist, the response status should be 200, but the response body should be empty. 
// This is because the DELETE verb is intended to be idempotent, ie, 
// multiple calls to the DELETE endpoint should respond with the same  type of response.

private void deleteMessageById(Context ctx){
    if(messageService.deleteMessageById(Integer.parseInt(ctx.pathParam("message_id"))) == null){
        ctx.status(200);
    }else{
        
        ctx.json(messageService.deleteMessageById(Integer.parseInt(ctx.pathParam("message_id"))));
    }
}

// As a user, I should be able to submit a PATCH request on the endpoint PATCH localhost:8080/messages/{message_id}.
//  The request body should contain a new message_text values to replace the message identified by message_id. 
//  The request body can not be guaranteed to contain any other information.
// - The update of a message should be successful if and only if the message id already exists and the new message_text is not blank 
// and is not over 255 characters. If the update is successful, the response body should contain the full updated message (including message_id,
//  posted_by, message_text, and time_posted_epoch), and the response status should be 200, which is the default. 
//  The message existing on the database should have the updated message_text.
// - If the update of the message is not successful for any reason, the response status should be 400. (Client error)

private void updateMessage(Context ctx) throws JsonProcessingException{
    ObjectMapper mapper = new ObjectMapper();
    Message msg = mapper.readValue(ctx.body(),Message.class);
    int messageId = Integer.parseInt(ctx.pathParam("message_id"));
    Message newTxt = messageService.updateMessage(msg, messageId);
 if(newTxt == null){
     ctx.status(400);
}else{
    ctx.json(mapper.writeValueAsString(newTxt));
        
    }
}

// As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/accounts/{account_id}/messages.

// - The response body should contain a JSON representation of a list containing all messages posted by a particular user, 
// which is retrieved from the database. 
// It is expected for the list to simply be empty if there are no messages. The response status should always be 200, which is the default.


private void getAllMessageByAccountId(Context ctx){
     ctx.json(messageService.getAllMessagesByAccountId(Integer.parseInt(ctx.pathParam("account_id"))));
    
}










}
