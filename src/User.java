/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Anthony
 */
public class User 
{
    private int user_id;
    private String user_name;
    private int balance;
    public User(){
        
    }
    
    public User(int user_id,String user_name, int balance)
    {
        this.user_id = user_id;
        this.user_name = user_name;
        this.balance = balance;
    }
    
    public int getUserId()
    {
        return user_id;
    }
    
    public String getUserName()
    {
        return user_name;
    }
    
    public int getBalance()
    {
        return balance;
    }
    
}
