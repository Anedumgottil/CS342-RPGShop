// Programmer:  Michael Baccia, Tran Dao, Anthony Nedumgottil, Aaron Zheng
// Assignment:  Project 4 - Final Project
// Date:        12/03/15
// Description: This is front-end GUI of the RPG store. This class links the front-end to
//              the back-end.

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
    
    private int itemSelected;                       //Identifies which item was selected in the menu.
    private int currentPage;                        //The current page.
    private int totalPages;                         //The total number of pages.
    private boolean mode;                           //The mode (buy/sell) for the store.
    private int player;                             //The current player.
    private int store;                              //The current store.
    
    private CustomButton buyTab;              //The button for the buy tab.
    private CustomButton sellTab;             //The button for the sell tab.
    private CustomButton leftTab;             //The button for the left tab.
    private CustomButton rightTab;            //The button for the right tab.
    private CustomButton buySellButton;       //The button for the buy/sell button.
    private ScaledPoint[] inventoryPos;       //The position of the inventory box.
    
    public RPGStoreGUI()
    //  POST: Constructs a RPGStoreGUI object. Initializes location of all GUI elements.
    //        itemSelected is set to -1 (none) and mode is set to false (buy).
    {   
        super();        
        setLayout(new FlowLayout());
        addKeyListener(this);
        addMouseListener(this);
        setFocusable(true);       
        
        player = 0;        
        switchStore(0); 
        initializeButtons();
        
        inventoryPos = new ScaledPoint[2];
        inventoryPos[0] = new ScaledPoint(0.02, .2);
        inventoryPos[1] = new ScaledPoint(0.57, .84);
    }
    
    @Override
    public void paint(Graphics g)
    {   
        super.paint(g);
        CustomButton.setGraphics(g);
        ScaledPoint.setWindowDimensions(getWidth(), getHeight());
        
        //Draw main GUI components
        drawStoreBG(g);
        drawStoreName(g);
        drawStoreImage(g);
        drawStoreMessage(g);
        drawInventory(g);
                
        //Draw buttons
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
        
        CustomButton.draw();
        
        g.setColor(Color.WHITE);
        g.drawString("Current item: " + itemSelected, 100, getHeight()/2);
    }
    
    private void drawStoreImage(Graphics g)
    //  PRE:  g must be initialized.
    //  POST: Draws the image associated with the store at the storeImage location.
    //        All images should be located in the \images\ directory.
    {
        String filePath;                //the image file path
        ScaledPoint[] storeImage;       //The scaled points for the store image.
        
        storeImage = new ScaledPoint[2];
        storeImage[0] = new ScaledPoint(0.6, .11);
        storeImage[1] = new ScaledPoint(0.995, .60);
        
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
        
        Drawing.drawImage(g, storeImage, filePath);
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
        ScaledPoint[] nameLocation;         //The scaled points for the store name.     
        
        nameLocation = new ScaledPoint[2];        
        nameLocation[0] = new ScaledPoint(0.005, 0.005);
        nameLocation[1] = new ScaledPoint(0.995, .1);
        
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
        ScaledPoint[] storeMessagePos;  //Position of dialogue box.
        
        storeMessagePos = new ScaledPoint[2];
        storeMessagePos[0] = new ScaledPoint(0.6, .61);
        storeMessagePos[1] = new ScaledPoint(0.995, .80);
        
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
                message = "Come! I guarentee you'll find something that catches your eye!";
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
        
        for(int i = 0; i < ITEMSPERPAGE; i++)  //draw each item into the window
        {
            itemY = y1 + (itemHeight)*i;
            
            if(itemSelected == i)  //if the item was selected, highlight it
            {
                g2.drawRect(x1, itemY, width, itemHeight);
            }
            
            drawItem(g2, i, x1, itemY, width, itemHeight);
        }
    }
    
    private void drawItem(Graphics g, int item, int x, int y, int width, int height)
    {
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
        int width;                      //Width of the inventory box.
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
        
        width = x2 - x1;
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
    
    private void initializeButtons()
    //  POST: Initializes all GUI buttons.
    {      
        ScaledPoint[] buySellButtonPos;     //The scaled points for the buySell button.
        ScaledPoint[] leftTabPos;           //The scaled points for the left tab.
        ScaledPoint[] rightTabPos;          //The scaled points for the right tab.
        ScaledPoint[] buyTabPos;            //The scaled points for the buy tab.
        ScaledPoint[] sellTabPos;           //The scaled points for the sell tab.
        
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
        buyTabPos[0] = new ScaledPoint(0.19, .14);
        buyTabPos[1] = new ScaledPoint(0.29, .19);
        buyTab = new CustomButton("buyTab", "BUY", buyTabPos);
        
        sellTabPos = new ScaledPoint[2];
        sellTabPos[0] = new ScaledPoint(0.31, .14);
        sellTabPos[1] = new ScaledPoint(0.41, .19);
        sellTab = new CustomButton("sellTab", "SELL", sellTabPos);
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
        totalPages = 3;
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
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseEntered(MouseEvent arg0) 
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseExited(MouseEvent arg0) 
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mousePressed(MouseEvent e) 
    {
        CustomButton buttonPressed;     //Button that was pressed.
        String buttonName;              //The name of the button.
        
        wasItemSelected(e.getX(), e.getY());
        buttonPressed = CustomButton.wasPressed(e.getX(), e.getY());
        
        if(buttonPressed == null)   //if no button was pressed
        {
            return;
        }
        
        buttonName = buttonPressed.getName();
        
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
                itemSelected = -1;
                break;
                
            default:
        }
        playAudio(-1);
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
        // TODO Auto-generated method stub
        
    }

    @Override
    public void keyTyped(KeyEvent e) 
    {
        
    }
}
