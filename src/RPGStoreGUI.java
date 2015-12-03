// Programmer:  Michael Baccia, Tran Dao, Anthony Nedumgottil, Aaron Zheng
// Assignment:  Project 4 - Final Project
// Date:        12/03/15
// Description: This is front-end GUI of the RPG store. This class links the front-end to
//              the back-end.

import java.sql.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.sound.sampled.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class RPGStoreGUI extends JPanel implements MouseListener, KeyListener
{
    public static final String FONTNAME = "Verdana";  //The font to be used.
    public static final int FONTSTYLE = Font.PLAIN;   //The font style to be used.    
    public static final int ITEMSPERPAGE = 10;        //The number of items per page.
    public static final int NUMSTORES = 4;            //The number of stores.
    public static final String[] SORTOPTIONS = {"Alpha: Ascending", "Alpha: Decending", "Price: Low to High", "Price: High to Low"};
    
    private int itemSelected;                 //Identifies which item was selected in the menu.
    private int currentPage;                  //The current page.
    private int totalPages;                   //The total number of pages.
    private boolean mode;                     //The mode (buy/sell) for the store.
    private int store;                        //The current store.
    private boolean help;                     //Determines whether or not to display the help message.
    
    private CustomButton buyTab;              //The button for the buy tab.
    private CustomButton sellTab;             //The button for the sell tab.
    private CustomButton leftTab;             //The button for the left tab.
    private CustomButton rightTab;            //The button for the right tab.
    private CustomButton buySellButton;       //The button for the buy/sell button.
    
    private ScaledPoint[] inventoryPos;       //The position of the inventory box.
    private ScaledPoint[] storeImage;         //The scaled points for the store image.
    private ScaledPoint[] nameLocation;       //The scaled points for the store name.  
    private ScaledPoint[] storeMessagePos;    //The scaled points for the dialogue box.
    private ScaledPoint[][] itemPositions;    //The positions of each item box.
    
    private int sizeQueryResult;                            //The number of rows returned from query
    private int orderToSort;    
    private int itemSelectedID;

    private ResultSet setOfResults;
    
    
    public RPGStoreGUI()
    //  POST: Constructs a RPGStoreGUI object. Initializes location of all GUI elements.
    //        itemSelected is set to -1 (none) and mode is set to false (buy).
    {   
        super();        
        setLayout(new FlowLayout());
        addKeyListener(this);
        addMouseListener(this);
        setFocusable(true);       
        
        help = false;
     
        switchStore(0); 
        initializeButtons();
        
        inventoryPos = new ScaledPoint[2];
        inventoryPos[0] = new ScaledPoint(0.02, .2);
        inventoryPos[1] = new ScaledPoint(0.57, .84);        
        
        storeImage = new ScaledPoint[2];
        storeImage[0] = new ScaledPoint(0.6, .11);
        storeImage[1] = new ScaledPoint(0.995, .60);   
        
        nameLocation = new ScaledPoint[2];        
        nameLocation[0] = new ScaledPoint(0.005, 0.005);
        nameLocation[1] = new ScaledPoint(0.8, .1);
        
        storeMessagePos = new ScaledPoint[2];
        storeMessagePos[0] = new ScaledPoint(0.6, .61);
        storeMessagePos[1] = new ScaledPoint(0.995, .80);
        
        itemPositions = initializeItemPositions();
        
    }
    
    @Override
    public void paint(Graphics g)
    {   
        super.paint(g);
        CustomButton.setGraphics(g);
        ScaledPoint.setWindowDimensions(getWidth(), getHeight());
        
        if(help)        //display help message
        {
            showHelp(g);
            return;
        }
 
        //Draw main GUI components
        drawStoreBG(g);
        drawStoreName(g);
        drawStoreImage(g);
        drawStoreMessage(g);
        drawInventory(g);
        drawBalance(g);
        drawPageIndicator(g);
                
        deactivateButtons();
        CustomButton.draw();
       
    }
    
    private void drawPageIndicator(Graphics g)
    //  PRE:  g must be initialized.
    //  POST: Draws a page indicator above the OK button. The filled in circle represents which
    //        page we're currently on.
    {
        int x1;             //First x coordinate for drawing area.
        int y1;             //First y coordinate for drawing area.
        int x2;             //Second x coordinate for drawing area.
        int y2;             //Second y coordinate for drawing area.
        int width;          //Width of drawing area.
        int height;         //Height of drawing area.
        int offset;         //Offset between circles.
        Graphics2D g2;      //Graphics2D to change brush stroke.
        
        x1 = ScaledPoint.scalerToX(0.275);
        x2 = ScaledPoint.scalerToX(0.325);
        y1 = ScaledPoint.scalerToY(0.85);
        y2 = ScaledPoint.scalerToY(0.89);
        
        width = (x2 - x1)/2;
        height = y2-y1;
        
        offset = ScaledPoint.scalerToX(0.005)/2;
        
        g2 = (Graphics2D) g;
        
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(1));
        
        if(currentPage == 0)        //if we are on the first page
        {            
            g.fillOval(x1 - offset, y1, width, height);
            g.drawOval(x1+width + offset, y1, width, height);
        }
        
        else                        //if we are on the second page.
        {
            g.drawOval(x1 - offset, y1, width, height);
            g.fillOval(x1+width + offset, y1, width, height);
        }
    }
    
    private void showHelp(Graphics g)
    //  PRE:  g must be initialized.
    //  POST: Displays the help image to the screen.
    {
	JOptionPane.showMessageDialog(this, "Once you are satisfied, click anywhere to continue!");
        Drawing.drawImage(g, 0, 0, getWidth(), getHeight(), ".\\images\\accessoryMerchant.jpg");
    }
    
    private void deactivateButtons()
    //  POST: Deactivates buttons that should not be pressed.
    {
        if(currentPage == 0)    //if we are at the first page, disable leftTab
        {
            leftTab.deactivate();
        }
        
        else    //otherwise activate it
        {
            leftTab.activate();
        }
        
        if(currentPage == totalPages - 1)  //if we are at the last page, disable rightTab
        {
            rightTab.deactivate();
        }
        
        else    //otherwise activate it
        {
            rightTab.activate();
        }
        
        if(itemSelected == -1)      //if no item has been selected
        {
            buySellButton.deactivate();
        }
        
        else    //otherwise activate it
        {
            buySellButton.activate();
        }
        
        if(!mode)   //if we are in buying mode
        {
            buyTab.deactivate();
            sellTab.activate();
        }
        
        else    //if we are in selling mode
        {
            sellTab.deactivate();
            buyTab.activate();
        }
    }
    
    private void drawStoreImage(Graphics g)
    //  PRE:  g must be initialized.
    //  POST: Draws the image associated with the store at the storeImage location.
    //        All images should be located in the \images\ directory.
    {
        String filePath;                //the image file path
        int x;                          //x coordinate of upper-right corner of image.
        int y;                          //y coordinate of upper-right corner of image.
        int width;                      //width of image
        int height;                     //height of image
        
        x = storeImage[0].getScaledX();
        y = storeImage[0].getScaledY();
        width = storeImage[1].getScaledX() - storeImage[0].getScaledX();
        height = storeImage[1].getScaledY() - storeImage[0].getScaledY();
        
        switch (store)    //initialize file path to image based on store
        {
            case 0:
                filePath = ".\\images\\weaponSmith.jpg";
                break;
                
            case 1:
                filePath = ".\\images\\armorSmith.jpg";
                break;
              
            case 2:
                filePath = ".\\images\\accessoryMerchant.jpg";
                break;
                
            case 3:
                filePath = ".\\images\\generalMerchant.png";
                break;
                
            default:
                filePath = ".\\images\\generalMerchant.png";
        }
        
        Drawing.drawImage(g, x, y, width, height, filePath);
    }
    
    private void drawStoreBG(Graphics g)
    //  PRE:  g must be initialized.
    //  POST: Draws the background associated with store. 
    {          
        switch (store)      //draw background based on value of store.
        {
            case 0:                
                Drawing.drawGradient(g, new Color(180, 80, 80), new Color(20, 0, 0), 0, 0, getWidth(), getHeight()/2);
                Drawing.drawGradient(g, new Color(20, 0, 0), new Color(180, 80, 80), 0, getHeight()/2, getWidth(), getHeight()/2+1);
                break;
                
            case 1:
                Drawing.drawGradient(g, new Color(80, 180, 80), new Color(0, 20, 0), 0, 0, getWidth(), getHeight());
                break;
                
            case 2:
                Drawing.drawGradient(g, new Color(230, 170, 50), new Color(60, 20, 0), 0, 0, getWidth(), getHeight());
                break;
                
            case 3:
                Drawing.drawGradient(g, new Color(120, 120, 120), new Color(20, 20, 20), 0, 0, getWidth(), getHeight());
                break;
        
            default:
                Drawing.drawGradient(g, new Color(80, 80, 180), new Color(0, 0, 20), 0, 0, getWidth(), getHeight());
        }
    }
    
    private void drawStoreName(Graphics g)
    //  PRE:  g must be initialized.
    //  POST: Draws the store name along with a rounded border at the top of the window.
    {
        int rectWidth;                      //The width of the drawing area.
        int rectHeight;                     //The height of the drawing area.
        String storeName;                   //The name of the store.
        Font font;                          //The font to be used.        
        
        rectWidth = nameLocation[1].getScaledX() - nameLocation[0].getScaledX();
        rectHeight = nameLocation[1].getScaledY() - nameLocation[0].getScaledY();
        
        switch(store)   //set message based on store value.
        {
            case 0:
                storeName = "Raneac's Crucible";
                break;
                
            case 1:
                storeName = "The Iron Maiden";
                break;
                
            case 2:
                storeName = "The Grand Bazaar";
                break;
            
            default:
                storeName = "Miko's Wares";
        }
        
        font = Drawing.getFont(storeName, rectWidth, rectHeight, FONTNAME, FONTSTYLE);
        
        g.setFont(font); 
        g.setColor(Color.WHITE);
        g.drawString(storeName, nameLocation[0].getScaledX(), (int)(nameLocation[1].getScaledY()*.98));
        Drawing.drawRoundedRect(g, nameLocation, Color.WHITE);
    }
    
    private void drawStoreMessage(Graphics g)
    //  PRE:  g must be initialized.
    //  POST: Draws the store's dialogue box beneath the store image.
    {
        int x1;                         //Upper-left x coordinate.
        int y1;                         //Upper-left y coordinate.
        int x2;                         //Bottom-right x coordinate.
        int y2;                         //Bottom-right y coordinate.
        int width;                      //Width of dialogue box.
        int height;                     //Height of dialogue box.
        int numLines;                   //Number of lines in dialogue box.
        String message;                 //Message to be printed.
        
        x1 = ScaledPoint.scalerToX(storeMessagePos[0].getXScaler()+0.01);
        y1 = ScaledPoint.scalerToY(storeMessagePos[0].getYScaler()+0.01);
        x2 = ScaledPoint.scalerToX(storeMessagePos[1].getXScaler()-0.01);
        y2 = ScaledPoint.scalerToY(storeMessagePos[1].getYScaler()-0.01);
        width = x2 - x1;
        height = y2 - y1;
        
        Drawing.drawRoundedRect(g, storeMessagePos, Color.WHITE);
        
        switch(store)   //change message based on value of store.
        {
            case 0:
                message = "If yer lookin' for a weapon, you've come to the right place.";
                break;
                
            case 1:
                message = "Welcome! You'll not find tougher steel elsewhere.";
                break;
                
            case 2:
                message = "Come! I guarantee you'll find something that catches your eye!";
                break;
                
            case 3:
                message = "Some say that many of my goods are trash... They're right.";
                break;
            
            default:
                message = "I don't know how you got here...";
        }
        
        numLines = 1;
        while( !(Drawing.drawWrappedString(g, message, numLines++, x1, y1, width, height)) ); //while we need more lines for the message      
    }

    private void drawBalance(Graphics g)
    //  POST: Draws the balance of the current player
    {
        Font font;
        String message;
        Color oldColor;
        int x1;
        int y1;
        int x2;
        int y2;
        int width;
        int height;
        int offset;
        int balance;
        
        balance = 4000;
        oldColor = g.getColor();
        
        x1 = ScaledPoint.scalerToX(0.72);
        y1 = ScaledPoint.scalerToY(0.81);
        x2 = ScaledPoint.scalerToX(0.88);
        y2 = ScaledPoint.scalerToY(0.86);
        width = x2-x1;
        height = y2-y1;
        message = "Balance:";
        font = Drawing.getFont(message, width, height, FONTNAME, FONTSTYLE);
        offset = (width - getFontMetrics(font).stringWidth(message))/2;
        g.setColor(Color.WHITE);
        g.drawString(message, x1+offset, y2);

        x1 = ScaledPoint.scalerToX(0.72);
        y1 = ScaledPoint.scalerToY(0.865);
        x2 = ScaledPoint.scalerToX(0.88);
        y2 = ScaledPoint.scalerToY(0.915);
        width = x2-x1;
        height = y2-y1;
        message = "$" + Integer.toString(balance);
        font = Drawing.getFont(message, width, height, FONTNAME, FONTSTYLE);
        offset = (width - getFontMetrics(font).stringWidth(message))/2;
        g.drawString(message, x1+offset, y2);
    
        g.setColor(oldColor);
    }
    
    private void drawInventory(Graphics g)
    //  PRE:  g must be initialized.
    //  POST: Draws the inventory of the store if mode == 0, else draws the players inventory.
    //        If an item is selected, its border is highlighted white.
    {
        Graphics2D g2;                  //Graphics 2D object to allow for different brush stroke widths.
        Color start;                    //The start color for the inventory window gradient.
        Color end;                      //The end color for the inventory window gradient.
        int x1;                         //Upper-left x coordinate.
        int y1;                         //Upper-left y coordinate.
        int x2;                         //Bottom-right x coordinate.
        int y2;                         //Bottom-right y coordinate.
        int width;                      //Width of the inventory box.
        int height;                     //Height of the inventory box.
        int itemHeight;                 //The height of an item box.
        int itemY;                      //The y coordinate of the item.
        
        g2 = (Graphics2D) g;
        start = new Color(120, 120, 220);
        end = new Color(50, 50, 150);
        
        x1 = inventoryPos[0].getScaledX();
        y1 = inventoryPos[0].getScaledY();
        x2 = inventoryPos[1].getScaledX();
        y2 = inventoryPos[1].getScaledY();
        width = x2 - x1;
        height = y2 - y1;
        
        while(height % ITEMSPERPAGE != 0)  //ensure that the item boxes do not fall short of the window
        {
            height = ++y2 - y1;
        }
        
        itemHeight = height/ITEMSPERPAGE;
        
        Drawing.drawGradient(g2, start, end, x1, y1, width, height/2);
        Drawing.drawGradient(g2, end, start, x1, y1 + height/2, width, height/2);
        
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(3));
        g2.drawRect(--x1, --y1, ++width, height);
        g2.setColor(Color.WHITE);
        
        switch(store)
        {
            case 0:
                //itemName = (getUserItems("Weapon" , orderToSort))[count][0];
                //itemPrice = (getUserItems("Weapon", orderToSort))[count][1];
                //iconPath = (getUserItems("Weapon", orderToSort))[count][2];
                setOfResults = getUserItems("Weapon", orderToSort);
                break;
            case 1:
                //itemName = (getUserItems("ArmorSmith", orderToSort))[count][0];
                //itemPrice = (getUserItems("ArmorSmith", orderToSort))[count][1];
                //iconPath = (getUserItems("ArmorSmith", orderToSort))[count][2];
                setOfResults = getUserItems("ArmorSmith", orderToSort);
                break;
            case 2:
                //itemName = (getUserItems("Accessory", orderToSort))[count][0];
                //itemPrice = (getUserItems("Accessory", orderToSort))[count][1];
                //iconPath = (getUserItems("Accessory", orderToSort))[count][2];
                setOfResults = getUserItems("Accessory", orderToSort);
                break;
            case 3:
                //itemName = (getUserItems("General", orderToSort))[count][0];
                //itemPrice = (getUserItems("General", orderToSort))[count][1];
                //iconPath = (getUserItems("General", orderToSort))[count][2];
                setOfResults = getUserItems("General", orderToSort);
                break;
        }
        
        String itemName ="";
        String iconPath = "";
        String itemPrice = "";
        int rowcount = 0;
        
        //String[] itemNameArray = setOfResults.getArray("item_name");
        
        int count = 0;
        count = currentPage * ITEMSPERPAGE;
        
        try{
            if(count > 9){
                setOfResults.absolute(10);
            }
      
        
        for(int i = 0; i < ITEMSPERPAGE; i++)  //draw each item into the window
        {
            
            itemY = y1 + (itemHeight)*i;
            
            
            if(itemSelected == i)  //if the item was selected, highlight it
            {
                g2.drawRect(x1, itemY, width, itemHeight);
                itemSelectedID = setOfResults.getInt("item_id");
            }
            
            if(setOfResults.next()){
            itemName = setOfResults.getString("item_name");
            itemPrice = setOfResults.getString("price");
            iconPath = setOfResults.getString("item_path");
            }else{
                return;
            }
            //System.out.println(i +" Item name :" + itemName);
            //System.out.println(i + "Item path :" + iconPath);
            //System.out.println(i +"Item price :" + itemPrice);
            
            drawItem(g2, i, count,itemName, iconPath, itemPrice);
            
            //System.out.println("COUNT IS " + count);
            //System.out.println("i is " + i);
            count++;
        }
            
        /*
        for(int i = 0; setOfResults.next(); i++){
            
       
                if(i >= ITEMSPERPAGE){
                    System.out.println("B");
                    return;
                }
           
            
            itemY = y1 + (itemHeight)*i;

            if(itemSelected == i)  //if the item was selected, highlight it
            {
                g2.drawRect(x1, itemY, width, itemHeight);
            }
            
            //if(setOfResults.next()){
            itemName = setOfResults.getString("item_name");
            itemPrice = setOfResults.getString("price");
            iconPath = setOfResults.getString("item_path");
            
            //}
            //System.out.println(i +" Item name :" + itemName);
            //System.out.println(i + "Item path :" + iconPath);
            //System.out.println(i +"Item price :" + itemPrice);
            
            drawItem(g2, i, count,itemName, iconPath, itemPrice);
            
            System.out.println("COUNT IS " + count);
            System.out.println("i "+ i);
            count++;
        */
        /*
        int i = count;
        while(setOfResults.next()){
            
            if (i > ITEMSPERPAGE -1){
                if(i > 9){
                   i = count;
                }
                i = count;
                break;
            }
            
            itemY = y1 + (itemHeight)*i;

            if(itemSelected == i)  //if the item was selected, highlight it
            {
                g2.drawRect(x1, itemY, width, itemHeight);
            }
            
            itemName = setOfResults.getString("item_name");
            itemPrice = setOfResults.getString("price");
            iconPath = setOfResults.getString("item_path");
            
            drawItem(g2, i, count, itemName, iconPath, itemPrice);
            
            System.out.println("COUNT IS " + count);
            System.out.println("i "+ i);
            
            count++;
            i++;
        }
        */
        }catch(Exception e){
                System.out.println(e.toString());
                System.out.println("Oh no, not like this...");
            }
    }
        
    private void drawItem(Graphics g, int item,int count, String itemName,String iconPath, String itemPrice)
    {
        Font font;          //The font to be used.
        int x1;             //The first x coordinate of drawing area.
        int y1;             //The first y coordinate of drawing area.
        int x2;             //The second x coordinate of drawing area.
        int y2;             //The second y coordinate of drawing area.
        int width;          //Width of drawing area.
        int height;         //Height of drawing area.
        
        int iconX;          //x coordinate of the icon.
        int iconY;          //y coordinate of the icon.
        int iconLength;     //Length of the icon.
        //String iconPath;    //The file path to the icon.
        
        int itemNameX;      //The x coordinate of the item name.
        int itemNameLength; //The length of the item name.
        //String itemName;    //The name of the item.
        
        int itemPriceX;     //The x coordinate of the item price.
        int itemPriceWidth; //The width of the item price.
        //String itemPrice;   //The price of the item.
        
        x1 = itemPositions[item][0].getScaledX();
        x2 = itemPositions[item][1].getScaledX();
        y1 = itemPositions[item][0].getScaledY();
        y2 = itemPositions[item][1].getScaledY();        
        width = x2 - x1;
        height = y2 - y1;  
         
        //itemName = "";
       // itemPrice = "";
        //iconPath = "";
        
         /*
        if(count > sizeQueryResult)
        {
            return;
        }*
            
         /*
        try{
          //  while(setOfResults.next()){
              for(int i =0; setOfResults.next(); i++){  
                  
                itemName = setOfResults.getString("item_name");
                itemPrice = setOfResults.getString("price");
                iconPath = setOfResults.getString("item_path");
                System.out.println(i);        
            }
        }catch(Exception e){
            System.out.println(e.toString());
            System.out.println("Oh no, not like this...");
        }*/
         
         //System.out.println(" Item name :" + itemName);
            //System.out.println("Item path :" + iconPath);
            //System.out.println("Item price :" + itemPrice);
        
        iconX = x1+(int)(width*.01);
        iconY = y1+(int)(height*.1);
        iconLength = (int)(height*.8);

        Drawing.drawImage(g, iconX, iconY, iconLength, iconLength, iconPath);

        itemNameX = iconX + (int)(iconLength*1.5);
        itemNameLength = (int)(width*0.6);

        font = Drawing.getFont(itemName, itemNameLength, (int)(iconLength*0.8), FONTNAME, FONTSTYLE);
        
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString(itemName, itemNameX, iconY+(int)(iconLength*0.9));

        itemPriceX = x1 + (int)(width * 0.8);
        itemPriceWidth = (int)(width*0.15);

        font = Drawing.getFont(itemPrice, itemPriceWidth, iconLength, FONTNAME, FONTSTYLE);
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString(itemPrice, itemPriceX, iconY+(int)(iconLength*.9));
        
        return;
    }
    
    private void wasItemSelected(int x, int y)
    //  PRE:  x and y should be non-negative values.
    //  POST: Determines whether an item was clicked on based on inventoryPos. If an item was
    //        was clicked, sets itemSelected to the corresponding item.
    {
        int x1;                         //Upper-left x coordinate.
        int y1;                         //Upper-left y coordinate.
        int x2;                         //Bottom-right x coordinate.
        int y2;                         //Bottom-right y coordinate.
        int height;                     //Height of the inventory box.
        int itemHeight;                 //The height of an item box.
        int itemY;                      //The y coordinate of the item.
        
        x1 = inventoryPos[0].getScaledX();
        y1 = inventoryPos[0].getScaledY();
        x2 = inventoryPos[1].getScaledX();
        y2 = inventoryPos[1].getScaledY();
        
        if(x < x1 || x > x2 || y < y1 || y > y2)    //if x and y are outside of the inventory box
        {
            return;
        }
        
        height = y2 - y1;
        
        while(height % ITEMSPERPAGE != 0)  //ensure that the item boxes do not fall short of the window
        {
            height = ++y2 - y1;
        }
        
        itemHeight = height/ITEMSPERPAGE;
        
        for(int i = 0; i < ITEMSPERPAGE; i ++)  //for each item
        {
            itemY = y1 + (itemHeight)*i;
            
            if(y >= itemY && y <= itemY+itemHeight)     //if this was the item selected
            {
                itemSelected = i;
                
                return;
            }
        }
    }
    
    private ScaledPoint[][] initializeItemPositions()
    //  POST: FCTVAL == ScaledPoint[][] of item positions based on the inventory window and ITEMSPERPAGE
    {
        ScaledPoint[][] itemPositions;  //Array of item positions to be returned.
        ScaledPoint item;               //Position of the current item.
        int x1;                         //First x coordinate.
        int y1;                         //First y coordinate.
        int x2;                         //Second x coordinate.
        int y2;                         //Second y coordinate.
        int windowHeight;               //Height of the drawing window.
        int itemHeight;                 //Height of an individual item pane.

        windowHeight = inventoryPos[1].getScaledY() - inventoryPos[0].getScaledY();        
        
        itemHeight = windowHeight / ITEMSPERPAGE;
        itemPositions = new ScaledPoint[ITEMSPERPAGE][];
                
        for(int i = 0; i < ITEMSPERPAGE; i++)  //for each item
        {
            x1 = inventoryPos[0].getScaledX();
            x2 = inventoryPos[1].getScaledX();
            y1 = inventoryPos[0].getScaledY() + (itemHeight * i) + i/2;
            y2 = inventoryPos[0].getScaledY() + (itemHeight * (i+1) + i/2);
            
            itemPositions[i] = new ScaledPoint[2];
            item = new ScaledPoint(x1, y1);
            itemPositions[i][0] = item;
            item = new ScaledPoint(x2, y2);
            itemPositions[i][1] = item;
        }
        
        return itemPositions;
    }
    
    private void initializeButtons()
    //  POST: Initializes all GUI buttons.
    {      
        ScaledPoint[] buySellButtonPos;     //The scaled points for the buySell button.
        ScaledPoint[] leftTabPos;           //The scaled points for the left tab.
        ScaledPoint[] rightTabPos;          //The scaled points for the right tab.
        ScaledPoint[] buyTabPos;            //The scaled points for the buy tab.
        ScaledPoint[] sellTabPos;           //The scaled points for the sell tab.
        ScaledPoint[] nextStorePos;         //The scaled points for the next store button.
        ScaledPoint[] sortPos;              //The scaled points for the sort button.
        ScaledPoint[] helpPos;              //The scaled points for the help button.
        
        buySellButtonPos = new ScaledPoint[2];
        buySellButtonPos[0] = new ScaledPoint(0.25, .90);
        buySellButtonPos[1] = new ScaledPoint(0.35, .95);
        buySellButton = new CustomButton("button", "OK", buySellButtonPos);
        
        leftTabPos = new ScaledPoint[2];
        leftTabPos[0] = new ScaledPoint(0.02, .85);
        leftTabPos[1] = new ScaledPoint(0.07, .88);
        leftTab = new CustomButton("leftTab", "PREV", leftTabPos);
        
        rightTabPos = new ScaledPoint[2];
        rightTabPos[0] = new ScaledPoint(0.52, .85);
        rightTabPos[1] = new ScaledPoint(0.57, .88);
        rightTab = new CustomButton("rightTab", "NEXT", rightTabPos);
        
        buyTabPos = new ScaledPoint[2];
        buyTabPos[0] = new ScaledPoint(0.02, .147);
        buyTabPos[1] = new ScaledPoint(0.12, .197);
        buyTab = new CustomButton("buyTab", "BUY", buyTabPos);
        
        sellTabPos = new ScaledPoint[2];
        sellTabPos[0] = new ScaledPoint(0.12, .147);
        sellTabPos[1] = new ScaledPoint(0.22, .197);
        sellTab = new CustomButton("sellTab", "SELL", sellTabPos);
        
        nextStorePos = new ScaledPoint[2];
        nextStorePos[0] = new ScaledPoint(0.84, .015);
        nextStorePos[1] = new ScaledPoint(0.99, .085);
        new CustomButton("nextStore", "NEXT STORE", nextStorePos);
        
        sortPos = new ScaledPoint[2];
        sortPos[0] = new ScaledPoint(0.47, .147);
        sortPos[1] = new ScaledPoint(0.57, .197);
        new CustomButton("sort", "SORT", sortPos);

        helpPos = new ScaledPoint[2];
        helpPos[0] = new ScaledPoint(0.005, .945);
        helpPos[1] = new ScaledPoint(0.105, 0.995);
        new CustomButton("help", "HELP", helpPos);
    }
    
    private void switchStore(int store)
    //  PRE:  store must correspond to one of the existing stores.
    //  POST: Switches to the given store: deselects all items, sets mode to buy, sets current
    //        page to 0, updates total pages and places the audio associated with store.
    {
        this.store = store;
        itemSelected = -1;
        mode = false;
        currentPage = 0;
        totalPages = 2;
        playAudio(store);
    }
    
    private void playAudio(int store)
    //  PRE:  store must correspond to one of the existing stores.
    //  POST: Plays the audio associated with store. All files should be located in
    //        the \audio\ file path.
    {
        AudioInputStream audio;     //The audio stream.
        String filePath;            //The file path to the audio file.
        File audioFile;             //The audio file.
        Clip audioClip;             //The audio clip.

        switch(store)   //create file path for audio associated with store
        {
            case -1:
                filePath = ".\\audio\\click.wav";
                break;
                
            case 0:
                filePath = ".\\audio\\weaponSmith.wav";
                break;
                
            case 1:
                filePath = ".\\audio\\armorSmith.wav";
                break;
                
            case 2:
                filePath = ".\\audio\\accessoryMerchant.wav";
                break;
            
            default:
                filePath = ".\\audio\\generalMerchant.wav";
        }
        
        audioFile = null;   
        
        try 
        {
            audioClip = AudioSystem.getClip();
            audioFile = new File(filePath);
            audio = AudioSystem.getAudioInputStream(audioFile);
            audioClip.open(audio);   
            audioClip.start();
        } 
        
        catch (Exception e)     //if there was an error playing the clip
        {
            System.err.println("Error playing clip!");
        }        
    }
    
    @Override
    public void mouseClicked(MouseEvent arg0) 
    {
    }

    @Override
    public void mouseEntered(MouseEvent arg0) 
    {
    }

    @Override
    public void mouseExited(MouseEvent arg0) 
    {
    }

    @Override
    public void mousePressed(MouseEvent e) 
    {
        CustomButton buttonPressed;     //Button that was pressed.
        String buttonName;              //The name of the button.
        int option;                     //Option chosen by user.
        
        if(help)            //if in help mode
        {
            help = false;
            repaint();
            return;
        }
        
        wasItemSelected(e.getX(), e.getY());
        buttonPressed = CustomButton.wasPressed(e.getX(), e.getY());
        
        if(buttonPressed == null)   //if no button was pressed
        {
            return;
        }
        
        buttonName = buttonPressed.getName();
        playAudio(-1);
        
        switch(buttonName)      //handle event associated with button name
        {
            case "rightTab":
                currentPage++;
                itemSelected = -1;
                break;
                
            case "leftTab":
                currentPage--;
                itemSelected = -1;
                break;
                
            case "buyTab":
                mode = false;
                break;
                
            case "sellTab":
                mode = true;
                break;
                
            case "button":
                
                /********************CHANGE TO FUNCTION CALL *********************/
                String itemPrice = "";
                try{
                     itemPrice=setOfResults.getString("price");
                }catch(Exception e3){
                    
                }
               
                
                option = JOptionPane.showConfirmDialog(this, ((!mode)?"Buy ":"Sell ") + "for " + itemPrice +"?");
                
                if(option == 0)     //if they choose to buy/sell
                {
                    
                }
                
                itemSelected = -1;
                break;
                
            case "nextStore":
                if(++store == NUMSTORES)    //if we're on the last store, loop around
                {
                    store = 0;
                }
                
                switchStore(store);
                break;
                
            case "sort":
                //something
                orderToSort = JOptionPane.showOptionDialog(this, "Sort by:", "Sort", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, SORTOPTIONS, null);
                
                break;
                
            case "help":
                help = true;
                break;
                
            default:
        }
        
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent arg0) 
    {
        CustomButton.depress();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) 
    {       
        int option;     //The option the user selected.
        int item_id;
        int price;
        String buyer_name;
        String seller_name;
        
        if(help)    //if in help mode
        {
            return;
        }
        
        switch(e.getKeyCode())              //handle event associated with key
        {
            case KeyEvent.VK_F1:            //if f1 was pressed
                switchStore(0);
                break;
            
            case KeyEvent.VK_F2:            //if f2 was pressed
                switchStore(1);
                break;
            
            case KeyEvent.VK_F3:            //if f3 was pressed
                switchStore(2);
                break;
            
            case KeyEvent.VK_F4:            //if f4 was pressed
                switchStore(3);
                break;
            
            case KeyEvent.VK_D:             //if d or right was pressed
            case KeyEvent.VK_RIGHT:
                if(currentPage != totalPages-1)      //if we are not on the last page
                {
                    currentPage++;
                    itemSelected = -1;
                }
                break;
                
            case KeyEvent.VK_A:             //if a or left was pressed
            case KeyEvent.VK_LEFT:
                if(currentPage != 0)      //if we are not on the first page
                {
                    currentPage--;
                    itemSelected = -1;
                }
                break;
                
            case KeyEvent.VK_W:             //if w or up was pressed
            case KeyEvent.VK_UP:
                if(itemSelected == -1)      //if no items currently selected
                {
                    itemSelected = 0;
                }
                
                else                        //if an item was selected
                {
                    itemSelected--;
                    if(itemSelected < 0)    //loop if at beginning of list
                    {
                        itemSelected = ITEMSPERPAGE-1;
                    }
                }
                
                break;
            
            case KeyEvent.VK_S:            //if s or down was pressed
            case KeyEvent.VK_DOWN:
                itemSelected++;
                
                if(itemSelected >= ITEMSPERPAGE)       //if at end of list, loop around
                {
                    itemSelected = 0;
                }
                
                break;
                
            case KeyEvent.VK_ENTER:        //if enter was pressed
                if(itemSelected == -1)     //if no item was selected
                {
                    break;
                }
                //Get item_id from itemSelected 
                 //item_id = 
                item_id = 0;
                price = 0;
                System.out.println("item selected "+ itemSelectedID);
                
                try{
                //   setOfResults.absolute(itemSelectedID); 
                //   item_id = setOfResults.getInt("item_id");
                //   price = setOfResults.getInt("price");
                }
                catch(Exception e3)
                {
                    
                }
                  
                /********************CHANGE TO FUNCTION CALL *********************/
                option = JOptionPane.showConfirmDialog(this, ((!mode)?"Buy ":"Sell ") + "for " + "$"+price+"?");
                
                if(option == 0)     //if they choose to buy/sell
                {
                    buyer_name = "Player";
                    switch(store)   //change message based on value of store.
                    {
                        case 0:
                            seller_name = "Weapon";
                            break;

                        case 1:
                            seller_name = "ArmorSmith";
                            break;

                        case 2:
                            seller_name = "Accessory";
                            break;

                        case 3:
                            seller_name = "General";
                            break;

                        default:
                            seller_name = "ERROR";
                    }
                    
                    buyItemFromUser(buyer_name,seller_name, item_id);
                 
                }
                else if(option == 1)
                {
                    seller_name = "Player";
                    switch(store)   //change message based on value of store.
                    {
                        case 0:
                            buyer_name = "Weapon";
                            break;
                        case 1:
                            buyer_name = "ArmorSmith";
                            break;
                        case 2:
                             buyer_name = "Accessory";
                            break;
                        case 3:
                             buyer_name = "General";
                            break;
                        default:
                             buyer_name = "ERROR";
                    }
                    //buyItemFromUser(buyer_name,seller_name, item_id);
                }
                
                itemSelected = -1;
                break;
                
            default:
                return;
        }
        
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent arg0) 
    {
    }

    @Override
    public void keyTyped(KeyEvent e) 
    {        
    }
    
    public String sortList(int sortOrder)
    {
        String order = "";
     
        switch(sortOrder)
        {
            case 1: 
                order = "order by item_name ASC";
                break;
            case 2:
                order = "order by item_name DESC";
                break;
            case 3:
                order = "order by item_price ASC";
                break;
            case 4:
                order = "order by item_price DESC";
                break;
            default:
                order = "";
                break;
        }
        
        return order;
    }
    /*
    public String[][] getUserItems(String user_name, int sortOrder)
    {   
        String order = "";
        
        switch(sortOrder)
        {
            case 0: 
                order = "order by item_name ASC";
                break;
                
            case 1:
                order = "order by item_name DESC";
                break;
            case 2:
                order = "order by price ASC";
                break;
            case 3:
                order = "order by price DESC";
                break;
            default:
                order = "";
                break;
        }
        
        String str = String.format("select * from items "
                    + "inner join "
                    + "("
                    + "select * from users where user_name = '%s' "
                    + ") AS temp "
                    + "ON temp.user_id = items.owner_id "
                    + "%s" ,user_name,order);
        
        System.out.println(str);
        
        String[][] results = connect(str);
        
        return results;
    }
    */
    public ResultSet getUserItems(String user_name, int sortOrder)
    {   
        String order = "";
        
        switch(sortOrder)
        {
            case 0: 
                order = "order by item_name ASC";
                break;
                
            case 1:
                order = "order by item_name DESC";
                break;
            case 2:
                order = "order by price ASC";
                break;
            case 3:
                order = "order by price DESC";
                break;
            default:
                order = "";
                break;
        }
        
        String str = String.format("select * from items "
                    + "inner join "
                    + "("
                    + "select * from users where user_name = '%s' "
                    + ") AS temp "
                    + "ON temp.user_id = items.owner_id "
                    + "%s" ,user_name,order);
        
        System.out.println(str);
        
        ResultSet results = connect(str);
        
        return results;
    }
    
    public void buyItemFromUser(String buyer_name, String seller_name, int item_id)
    {
        
        String str;                                                    //String to store a sql query
        int new_buyer_balance = -1;
         int new_seller_balance = -1;
                 
        str = String.format("select user_id, balance from users "
                + "where user_name = '%s'",buyer_name);                 //Query to select info about the buyer
        
        ResultSet buyer_results = connect(str);                         //ResultSet about the buyer
        
        str = String.format("select user_id, balance from users "
                + "where user_name = '%s'",seller_name);                //Query to select info about the seller
        
        ResultSet seller_results = connect(str);                        //ResulrtSet about the seller
        
        str = String.format("select owner_id, price from items "
                + "where item_id  = %d",item_id);                       //Query to select info about the item
        
        ResultSet item_results = connect(str);                          //ResultSet about the item
        
        try{
            new_buyer_balance = buyer_results.getInt("balance") - item_results.getInt("price");  //The buyer's new balance
            new_seller_balance = seller_results.getInt("balance") + item_results.getInt("price");//The seller's new balance

            if(new_buyer_balance > 0)      //If the buyer wont go negative
            {
                str = String.format("Update users"
                        + "set balance = %d"
                        + "where user_name = '%s'", new_buyer_balance,buyer_name);
               
                connectNon(str);                                     //Executre non scalar query
                
                str = String.format("Update users"
                        + "set balance = %d"
                        + "where user_name = '%s'", new_seller_balance,seller_name);
                
                connectNon(str);                                     //Executre non scalar query
                    
                str = String.format("Update items"
                        + "set owner_id = %d", buyer_results.getInt("user_id"));
                
                connectNon(str);                                     //Executre non scalar query
            }
            else
            {
                //Prompt the user with an error
                JOptionPane.showMessageDialog(null, "You will go bankrupt if you try buying that, try selling some items.");
            }   
        }catch(Exception e){
            //FIX
        }
    }
    
    public void sellItemFromUser(String buyer_name, String seller_name, int item_id)//Proably dont need use Buy with names swapped
    {
        
        String str;
        
        str = String.format("select user_id, balance from users "
                + "where user_name = '%s'",buyer_name);
        
        ResultSet buyer_results = connect(str);
        
        str = String.format("select user_id, balance from users "
                + "where user_name = '%s'",seller_name);
        
        ResultSet seller_results = connect(str);
        
        str = String.format("select owner_id, price from items "
                + "where item_id  = %d",item_id);
        
        ResultSet item_results = connect(str);
        
        try{
            int new_seller_balance = seller_results.getInt("balance") - item_results.getInt("price");
            int new_buyer_balance = buyer_results.getInt("balance") + item_results.getInt("price");

            if(new_seller_balance > 0)  
            {
                str = String.format("Update users"
                        + "set balance = %d"
                        + "where user_name = '%s'", new_buyer_balance,buyer_name);
                //Executre non scalar query
                
                connectNon(str);
                
                str = String.format("Update users"
                        + "set balance = %d"
                        + "where user_name = '%s'", new_seller_balance,seller_name);
                //do non scalar
                
                connectNon(str);
                
                str = String.format("Update items"
                        + "set owner_id = %d", buyer_results.getInt("user_id"));
                
                connectNon(str);
            }
            else
            {
                //Prompt the user with an error
                JOptionPane.showMessageDialog(null, "The shopkeep will go bankrupt if you try  that, try buying some items.");
            }   
        }catch(Exception e){
            
        }
       
   
    }
    /*
    public String[][] connect(String query )
    {
        String driver = "org.apache.derby.jdbc.ClientDriver";               //Driver for DB
        String url ="jdbc:derby://localhost:1527/ShopDataBase";             //Url for DB
        String user = "root";                                               //Username for db
        String pass = "root";                                               //Password for db
        Connection myConnection;                                            //Connection obj to db
        Statement stmt;                                                     //Statement to execute a result appon
        ResultSet results;                                                  //A set containing the returned results 
        String[][] fulldata = new String[20][20];
        
        try
        {                                                                //Try connection to db
        
        Class.forName(driver).newInstance();                                //Create our db driver
        
        myConnection = DriverManager.getConnection(url, user , pass);       //Initalize our connection
        
                                             //Query to select all 'users' in users table
        
        stmt = myConnection.createStatement();                              //Create a new statement
        results = stmt.executeQuery(query);                             //Store the results of our query
        
 
        sizeQueryResult = -1;
        while(results.next())                                               //Itterate through the results set
        {
            sizeQueryResult++;
            fulldata[sizeQueryResult][0] = (results.getString("item_name"));
            fulldata[sizeQueryResult][1] = (results.getString("price"));            
            fulldata[sizeQueryResult][2] = (results.getString("item_path"));
        }
        
        System.out.println("TOTAL SIZE OF RESULT: " + sizeQueryResult);
        results.close();                                                    //Close the ResultSet
        stmt.close();                                                        //Close the statement
        myConnection.close();                                               //Close the connection to db
        
        }catch(Exception e)
        {                                                //Cannot connect to db
            System.out.println(e.toString());
            System.out.println("Error, something went horribly wrong!");
        }
        
        return fulldata;
    }*/
     public void connectNon(String query )
    {
        String driver = "org.apache.derby.jdbc.ClientDriver";               //Driver for DB
        String url ="jdbc:derby://localhost:1527/ShopDataBase";             //Url for DB
        String user = "root";                                               //Username for db
        String pass = "root";                                               //Password for db
        Connection myConnection;                                            //Connection obj to db
        Statement stmt;                                                     //Statement to execute a result appon
        ResultSet results= null;                                                  //A set containing the returned results 
        String[][] fulldata = new String[20][20];
        
        try
        {                                                                //Try connection to db
        
        Class.forName(driver).newInstance();                                //Create our db driver
        
        myConnection = DriverManager.getConnection(url, user , pass);       //Initalize our connection
        
                                             //Query to select all 'users' in users table
        
        stmt = myConnection.createStatement();                              //Create a new statement
        stmt.executeUpdate(query);                             //Store the results of our query
        
        /*
        sizeQueryResult = -1;
        while(results.next())                                               //Itterate through the results set
        {
            sizeQueryResult++;
            fulldata[sizeQueryResult][0] = (results.getString("item_name"));
            fulldata[sizeQueryResult][1] = (results.getString("price"));            
            fulldata[sizeQueryResult][2] = (results.getString("item_path"));
        }
        
        System.out.println("TOTAL SIZE OF RESULT: " + sizeQueryResult);
        results.close();                                                    //Close the ResultSet
        stmt.close();                                                        //Close the statement
        myConnection.close();                                               //Close the connection to db
        */
        }catch(Exception e)
        {                                                //Cannot connect to db
            System.out.println(e.toString());
            System.out.println("Error, something went horribly wrong!");
        }
        
        //return results;
    }
    public ResultSet connect(String query )
    {
        String driver = "org.apache.derby.jdbc.ClientDriver";               //Driver for DB
        String url ="jdbc:derby://localhost:1527/ShopDataBase";             //Url for DB
        String user = "root";                                               //Username for db
        String pass = "root";                                               //Password for db
        Connection myConnection;                                            //Connection obj to db
        Statement stmt;                                                     //Statement to execute a result appon
        ResultSet results= null;                                                  //A set containing the returned results 
        String[][] fulldata = new String[20][20];
        
        try
        {                                                                //Try connection to db
        
        Class.forName(driver).newInstance();                                //Create our db driver
        
        myConnection = DriverManager.getConnection(url, user , pass);       //Initalize our connection
        
                                             //Query to select all 'users' in users table
        
        stmt = myConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
    ResultSet.CONCUR_READ_ONLY);                              //Create a new statement
        results = stmt.executeQuery(query);                             //Store the results of our query
        
        /*
        sizeQueryResult = -1;
        while(results.next())                                               //Itterate through the results set
        {
            sizeQueryResult++;
            fulldata[sizeQueryResult][0] = (results.getString("item_name"));
            fulldata[sizeQueryResult][1] = (results.getString("price"));            
            fulldata[sizeQueryResult][2] = (results.getString("item_path"));
        }
        
        System.out.println("TOTAL SIZE OF RESULT: " + sizeQueryResult);
        results.close();                                                    //Close the ResultSet
        stmt.close();                                                        //Close the statement
        myConnection.close();                                               //Close the connection to db
        */
        }catch(Exception e)
        {                                                //Cannot connect to db
            System.out.println(e.toString());
            System.out.println("Error, something went horribly wrong!");
        }
        
        return results;
    }
}
