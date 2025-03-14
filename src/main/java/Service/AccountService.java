package Service;

import DAO.AccountDAO;
import Model.Account;
// import Util.ConnectionUtil;


public class AccountService {
    AccountDAO accountDAO;
    
    public AccountService(){
        accountDAO = new AccountDAO();
    }
    
    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }
   
    public Account userLogin(Account account){
        return accountDAO.userLogin(account);

        
    }
    
    public Account createUser(Account account){
        if(account.getPassword().length() < 4 || account.getUsername().length() == 0){
            return null;
        }
        else{
        
            return accountDAO.registerUser(account);
        }
        
    }
    
    
}
