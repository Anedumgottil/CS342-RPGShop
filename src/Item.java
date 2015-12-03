// Programmer:  Michael Baccia, Tran Dao, Anthony Nedumgottil, Aaron Zheng
// Assignment:  Project 4 - Final Project
// Date:        12/03/15
// Description: This is the backend Item object class to read and store 
//              data from the Items table
public class Item 
{
    private int item_id;
    private String item_name;
    private String item_type;
    private int price;
    private int owner_id;
    private String item_path;
    
    public Item()
    {
        this(0,null,null,0,0,null);
    }
    
    public Item(int item_id, String item_name, String item_type,int price, int owner_id, String item_path)
    {
        this.item_id = item_id;
        this.item_name = item_name;
        this.item_type = item_type;
        this.price = price;
        this.owner_id = owner_id;
        this.item_path = item_path;
        
    }
    
    public int getItemId()
    {
        return item_id;
    }
    
    public String getItemName()
    {
        return item_name;
    }
    
    public String getItemTyep()
    {
        return item_type;
    }
    public int getPrice()
    {
        return price;
    }
    public int getOwner()
    {
        return owner_id;
    }
    
    public String getItemPath(){
        return item_path;
    }
}
