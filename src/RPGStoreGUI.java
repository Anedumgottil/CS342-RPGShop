// Programmer:  Michael Baccia, Tran Dao, Anthony Nedumgottil, Aaron Zheng
// Assignment:  Project 4 - Final Project
// Date:        12/03/15
// Description: This is front-end GUI of the RPG store. This class links the front-end to
//              the back-end.

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class RPGStoreGUI extends JPanel implements MouseListener, KeyListener
{
    public static final String FILEPATH = "D:\\Users\\Michael\\workspace\\CS342_Final";
    public static final String FONTNAME = "Verdana";  //The font to be used.
    public static final int FONTSTYLE = Font.PLAIN;   //The font style to be used.
    
    
    private int itemSelected;                       //Identifies which item was selected in the menu
    private int page;                               //The current page
    private boolean mode;                           //The mode (buy/sell) for the store.
    private int player;                             //The current player.
    private int store;                              //The current store.
    
    private ScaledPoint[] storeName;          //The scaled points for the store name.
    private ScaledPoint[] storeImage;         //The scaled points for the store image.
    private ScaledPoint[] storeMessage;       //The scaled points for the store message
    
    public RPGStoreGUI()
    //  POST: Constructs a RPGStoreGUI object.
    //        itemSelected is set to -1 (none) and mode is set to false (buy).
    {   
        super();
        setLayout(new FlowLayout());
        addKeyListener(this);
        setFocusable(true);
        
        player = 0;
        
        switchStore(0);
        
        storeName = new ScaledPoint[2];
        storeName[0] = new ScaledPoint(0.005, 0.005);
        storeName[1] = new ScaledPoint(0.995, .1);
        
        storeImage = new ScaledPoint[2];
        storeImage[0] = new ScaledPoint(0.6, .11);
        storeImage[1] = new ScaledPoint(0.995, .60);
        
        storeMessage = new ScaledPoint[2];
        storeMessage[0] = new ScaledPoint(0.6, .61);
        storeMessage[1] = new ScaledPoint(0.995, .80);
    }
    
    @Override
    public void paint(Graphics g)
    {   
        super.paint(g);
        ScaledPoint.setWindowDimensions(getWidth(), getHeight());
        
        drawGradient(g, new Color(180, 80, 80), new Color(20, 0, 0), 0, 0, getWidth(), getHeight());
        drawStoreBG(g);
        drawStoreName(g);
        drawStoreImage(g);
        drawStoreMessage(g);
    }
    
    private void drawStoreImage(Graphics g)
    {
        BufferedImage image;        //image to be drawn
        String filePath;            //the image file path
        int x;                      //image x coordinate
        int y;                      //image y coordinate
        int width;                  //width of image
        int height;                 //height of image
        
        x = storeImage[0].getScaledX();
        y = storeImage[0].getScaledY();
        width = storeImage[1].getScaledX() - storeImage[0].getScaledX();
        height = storeImage[1].getScaledY() - storeImage[0].getScaledY();
        
        image = null;
        
        switch (store)
        {
            case 0:
                filePath = FILEPATH + "\\images\\weaponSmith.jpg";
                break;
                
            case 1:
                filePath = FILEPATH + "\\images\\armorSmith.jpg";
                break;
              
            case 2:
                filePath = FILEPATH + "\\images\\accessoryMerchant.jpg";
                break;
                
            case 3:
                filePath = FILEPATH + "\\images\\generalMerchant.png";
                break;
                
            default:
                filePath = null;
        }
        
        try 
        {
            image = ImageIO.read(new File(filePath));
        } 
        catch (Exception e) 
        {
            System.err.println("Error opening image!");
        }
        
        g.drawImage(image, x, y, width, height, null);
    }
    
    private void drawStoreBG(Graphics g)
    {          
        switch (store)
        {
            case 0:
                drawGradient(g, new Color(180, 80, 80), new Color(20, 0, 0), 0, 0, getWidth(), getHeight()/2);
                drawGradient(g, new Color(20, 0, 0), new Color(180, 80, 80), 0, getHeight()/2, getWidth(), getHeight()/2);
                break;
                
            case 1:
                drawGradient(g, new Color(80, 180, 80), new Color(0, 20, 0), 0, 0, getWidth(), getHeight());
                break;
                
            case 2:
                drawGradient(g, new Color(230, 170, 50), new Color(60, 20, 0), 0, 0, getWidth(), getHeight());
                break;
                
            case 3:
                drawGradient(g, new Color(120, 120, 120), new Color(20, 20, 20), 0, 0, getWidth(), getHeight());
                break;
        
            default:
                drawGradient(g, new Color(80, 80, 180), new Color(0, 0, 20), 0, 0, getWidth(), getHeight());
        }
    }
    
    private void drawGradient(Graphics g, Color start, Color end, int x, int y, int width, int height)
    {
        double redS;       //red component of start
        double redE;       //red component of end
        double greenS;     //green component of start
        double greenE;     //green component of end
        double blueS;      //blue component of start
        double blueE;      //blue component of end
        
        Color  oldColor;    //the old color
        Color  color;       //the color to be drawn
        double red;         //red value
        double green;       //green value
        double blue;        //blue value
        double redInc;      //value to increment red by
        double greenInc;    //value to increment green by
        double blueInc;     //value to increment blue by
        
        redS = start.getRed();
        redE = end.getRed();
        greenS = start.getGreen();
        greenE = end.getGreen();
        blueS = start.getBlue();
        blueE = end.getBlue();
        
        redInc = (redE - redS)/height;   
        greenInc = (greenE - greenS)/height;
        blueInc = (blueE - blueS)/height;
        
        red = redS;
        green = greenS;
        blue = blueS;
        
        oldColor = g.getColor();
        
        for(int i = 0; i < height; i++)   //draw each line of the given rect and update color
        {
            color = new Color((int)red, (int)green, (int)blue);
            g.setColor(color);
            g.drawLine(x, y+i, x+width, y+i);
            red += redInc;
            green += greenInc;
            blue += blueInc;
        }
        
        g.setColor(oldColor);
    }
    
    private void drawStoreName(Graphics g)
    {
        int rectWidth;
        int rectHeight;
        String message;
        Font font;
             
        rectWidth = storeName[1].getScaledX() - storeName[0].getScaledX();
        rectHeight = storeName[1].getScaledY() - storeName[0].getScaledY();
        
        switch(store)
        {
            case 0:
                message = "Raneac's Crucible";
                break;
                
            case 1:
                message = "The Iron Maiden";
                break;
                
            case 2:
                message = "The Grand Bazaar";
                break;
            
            default:
                message = "Miko's Wares";
        }
        
        font = getFont(message, rectWidth, rectHeight, FONTNAME, FONTSTYLE);
        
        g.setFont(font); 
        g.setColor(Color.WHITE);
        g.drawString(message, storeName[0].getScaledX(), (int)(storeName[1].getScaledY()*.98));
        drawRoundedRect(g, storeName, Color.WHITE);
    }
    
    private void drawStoreMessage(Graphics g)
    {
        int x1;
        int y1;
        int x2;
        int y2;
        int width;
        int height;
        int numLines;
        String message;
        
        x1 = ScaledPoint.scalerToX(storeMessage[0].getXScaler()+0.01);
        y1 = ScaledPoint.scalerToY(storeMessage[0].getYScaler()+0.01);
        x2 = ScaledPoint.scalerToX(storeMessage[1].getXScaler()-0.01);
        y2 = ScaledPoint.scalerToY(storeMessage[1].getYScaler()-0.01);
        width = x2 - x1;
        height = y2 - y1;
        
        drawRoundedRect(g, storeMessage, Color.WHITE);
        
        numLines = 1;
        
        switch(store)
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
        
        while( !(drawWrappedString(g, message, numLines++, x1, y1, width, height)) );
        
    }
    
    private boolean drawWrappedString(Graphics g, String message, int lines, int x, int y, int width, int height)
    {
        int lineHeight;             //The height of an individual line.
        int lineWidth;              //The width of the current line.
        int currentLine;            //The current line.
        int[] yCoordinates;         //The y coordinates of the lines to be printed.
        String[] words;             //The array of words to be printed.
        String[] messages;          //The messages to be printed per line.
        String lineMessage;         //The message for an individual line.
        Font font;                  //The font to be used.
        
        lineHeight = (int)((double)height/(double)lines*0.9);
        yCoordinates = new int[lines];
        yCoordinates[0] = y + lineHeight;
        
        for(int i = 1; i < lines; i++)
        {
            yCoordinates[i] = (int)(yCoordinates[i-1] + (double)lineHeight/0.9);
        }
        
        currentLine = 0;
        words = message.split(" ");        
        messages = new String[lines];       
        lineMessage = "";
        font = getFont(" ", getWidth(), lineHeight, FONTNAME, FONTSTYLE);
        g.setFont(font);
                
        for(String word : words)
        {
            if(currentLine >= lines)
            {
                return false;
            }
            
            lineWidth = getFontMetrics(font).stringWidth(lineMessage + " " + word);
            
            if(lineWidth > width)
            {
                messages[currentLine++] = lineMessage;
                lineMessage = word;
            }
            
            else
            {
                lineMessage += " " + word;
            }           
        }
        
        if(currentLine >= lines)
        {
            return false;
        }
        
        messages[currentLine] = lineMessage;
        
        for(int i = 0; i < messages.length; i++)
        {
            if(messages[i] != null)
            {
                g.drawString(messages[i], x, yCoordinates[i]);               
            }            
        }

        return true;
    }
    
    private void drawRoundedRect(Graphics g, ScaledPoint[] object, Color color)
    {
        int x1;
        int y1;
        int x2;
        int y2;
        int rectWidth;
        int rectHeight;
        int arcDiameter;        
        Graphics2D g2;
        
        x1 = object[0].getScaledX();
        y1 = object[0].getScaledY();
        x2 = object[1].getScaledX();
        y2 = object[1].getScaledY();
        g2 = (Graphics2D) g;
             
        rectWidth = x2 - x1;
        rectHeight = y2 - y1;
        arcDiameter = (rectWidth>rectHeight) ? rectHeight : rectWidth;
        arcDiameter /= 5;
        g2.setColor(color);
        g2.setStroke(new BasicStroke(4));
        g2.drawRoundRect(x1, y1, rectWidth, rectHeight, arcDiameter, arcDiameter);
    }
    
    private Font getFont(String message, int width, int height, String name, int style)
    //  PRE:  message must be initialized. width and height must be non-negative values.
    //        name must correspond to a valid font name. style must be a valid font style.
    //  POST: Will return a font of the given name and style, with its size set to the largest
    //        possible value given the width and height constraints.
    {
        final double HEIGHTSCALER = 1.55;   //the scaler for the height
        int size;                           //the size of the font
        int fontHeight;                     //the height of the test font
        int messageWidth;                   //the width of the sting using test font
        Font testFont;                      //test font
        
        size = 1;
        fontHeight = 0;
        messageWidth = 0;
        
        while(fontHeight <= height*HEIGHTSCALER && messageWidth <= width)  //while we can increase
                                                                           //  the font size
        {
            testFont = new Font(name, style, size++);
            fontHeight = getFontMetrics(testFont).getHeight();
            messageWidth = getFontMetrics(testFont).stringWidth(message);
        }
        
        return new Font(name, style, size-1);
    }

    private void switchStore(int store)
    {
        this.store = store;
        itemSelected = 0;
        mode = false;
        page = 0;
        playAudio(store);
    }
    
    private void playAudio(int store)
    {
        AudioInputStream audio;
        String filePath;
        File audioFile;
        Clip audioClip;

        switch(store)
        {
            case 0:
                filePath = FILEPATH + "\\audio\\weaponSmith.wav";
                break;
                
            case 1:
                filePath = FILEPATH + "\\audio\\armorSmith.wav";
                break;
                
            case 2:
                filePath = FILEPATH + "\\audio\\accessoryMerchant.wav";
                break;
            
            default:
                filePath = FILEPATH + "\\audio\\generalMerchant.wav";
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
        
        catch (Exception e) {
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
    public void mousePressed(MouseEvent arg0) 
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseReleased(MouseEvent arg0) 
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void keyPressed(KeyEvent e) 
    {       
        switch(e.getKeyCode())
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
                
            case KeyEvent.VK_W:             //if w or up was pressed
            case KeyEvent.VK_UP:
                if(itemSelected == -1)
                {
                    itemSelected = 0;
                }
                
                else
                {
                    itemSelected--;
                    if(itemSelected < 0)
                    {
                        itemSelected = 9;
                    }
                }
                
                break;
            
            case KeyEvent.VK_S:            //if s or down was pressed
            case KeyEvent.VK_DOWN:
                itemSelected++;
                
                if(itemSelected > 9)
                {
                    itemSelected = 0;
                }
                
                break;
                
            case KeyEvent.VK_ENTER:        //if enter was pressed
                itemSelected = -1;
                break;                      
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
