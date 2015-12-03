// Programmer:  Michael Baccia, Tran Dao, Anthony Nedumgottil, Aaron Zheng
// Assignment:  Project 4 - Final Project
// Date:        12/03/15
// Description: This is the backend User object class to read and store 
//              data from the Users table

public class User 
{
    private int user_id;            //Int to store the user_id
    private String user_name;       //String to store the user_name
    private int balance;            //Int to store the user's balance
    
    public User()
    {
        this(0,null,0);
    }
    
    public User(int user_id,String user_name, int balance)
    {
        this.user_id = user_id;
        this.user_name = user_name;
        this.balance = balance;
    }
    
    public int getUserId()
    //  POST: FCTVAL == this user's user_id
    {
        return user_id;
    }
    
    public String getUserName()
    //  POST: FCTVAL == this user's user_name
    {
        return user_name;
    }
    
    public int getBalance()
    //  POST: FCTVAL == this user's balance
    {
        return balance;
    }
    
}
