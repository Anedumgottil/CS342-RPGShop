// Programmer:  Michael Baccia, Tran Dao, Anthony Nedumgottil, Aaron Zheng
// Assignment:  Project 4 - Final Project
// Date:        12/03/15
// Description: This class contains a variety of helper methods to draw to the GUI.
//              As this class was never meant to be instantiated, it is implemented
//              as an abstract class.

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public abstract class Drawing {
        
    public static boolean drawImage(Graphics g, int x, int y, int width, int height, String filePath)
    //  PRE:  g and filePath must be initialized. x, y, width and height must be non-negative
    //  POST: Attempts to open the image located at the specified file path and draw it within
    //        the given position. FCTVAL == true if successful, false otherwise.
    {
        BufferedImage image;        //image to be drawn
        
        image = null;
        
        try 
        {
            image = ImageIO.read(new File(filePath));
        } 
        catch (IOException e) //if image could not be opened
        {
            return false;
        }
        
        g.drawImage(image, x, y, width, height, null);
        return true;
    }
    
    public static void drawGradient(Graphics g, Color start, Color end, int x, int y, int width, int height)
    //  PRE:  g, start and end must be initialized. x, y, width and height must be non-negative values.
    //  POST: Draws a gradient vertically starting at (x, y) for width and height. The start color will begin
    //        at (x, y) and evenly fade to the end color at (x+width, y+height).
    {
        double redS;       //red component of start.
        double redE;       //red component of end.
        double greenS;     //green component of start.
        double greenE;     //green component of end.
        double blueS;      //blue component of start.
        double blueE;      //blue component of end.
        
        Color  oldColor;    //the old color.
        Color  color;       //the color to be drawn.
        double red;         //red value.
        double green;       //green value.
        double blue;        //blue value.
        double redInc;      //value to increment red by.
        double greenInc;    //value to increment green by.
        double blueInc;     //value to increment blue by.
        
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
    
    public static void drawRoundedRect(Graphics g, ScaledPoint[] position, Color color)
    //  PRE:  g, position and color must be initialized. Position should only include non-negative values.
    //  POST: Draws a rounded rectangle of Color color and width 4 around position.
    {
        int x1;             //First x coordinate.
        int y1;             //First y coordinate.
        int x2;             //Second x coordinate.
        int y2;             //Second y coordinate.
        int rectWidth;      //Width of the rectangle.
        int rectHeight;     //Height of the rectangle.
        int arcDiameter;    //Arc diameter for the corners of the rectangle.
        Graphics2D g2;      //Graphics2D object to enable a larger border width.
        
        x1 = position[0].getScaledX();
        y1 = position[0].getScaledY();
        x2 = position[1].getScaledX();
        y2 = position[1].getScaledY();
        g2 = (Graphics2D) g;
             
        rectWidth = x2 - x1;
        rectHeight = y2 - y1;
        arcDiameter = (rectWidth>rectHeight) ? rectHeight : rectWidth;
        arcDiameter /= 5;
        g2.setColor(color);
        g2.setStroke(new BasicStroke(4));
        g2.drawRoundRect(x1, y1, rectWidth, rectHeight, arcDiameter, arcDiameter);
    }
    
    public static Font getFont(String message, int width, int height, String name, int style)
    //  PRE:  message must be initialized. width and height must be non-negative values.
    //        name must correspond to a valid font name. style must be a valid font style.
    //  POST: Will return a font of the given name and style, with its size set to the largest
    //        possible value given the message, width and height constraints.
    {
        final double HEIGHTSCALER = 1.55;   //The scaler for the height.
        int size;                           //The size of the font.
        int fontHeight;                     //The height of the test font.
        int messageWidth;                   //The width of the sting using test font.
        Font testFont;                      //The test font.
        JPanel panel;                       //Panel to enable usage of FontMetrics.
        
        size = 1;
        fontHeight = 0;
        messageWidth = 0;
        panel = new JPanel();
        
        while(fontHeight <= height*HEIGHTSCALER && messageWidth <= width)  //while we can increase
                                                                           //  the font size
        {
            testFont = new Font(name, style, size++);
            fontHeight = panel.getFontMetrics(testFont).getHeight();
            messageWidth = panel.getFontMetrics(testFont).stringWidth(message);
        }
        
        return new Font(name, style, size-1);
    }
    
    public static boolean drawWrappedString(Graphics g, String message, int lines, int x, int y, int width, int height)
    //  PRE:  g, and message must be initialized. lines, x, y, width and height must
    //        be non-negative values.
    //  POST: Attempts to draw the given message given the restraints lines, x, y, width
    //        and height. Returns false if unable to print message, true otherwise.
    {
        int lineHeight;             //The height of an individual line.
        int lineWidth;              //The width of the current line.
        int currentLine;            //The current line.
        int[] yCoordinates;         //The y coordinates of the lines to be printed.
        String[] words;             //The array of words to be printed.
        String[] messages;          //The messages to be printed per line.
        String lineMessage;         //The message for an individual line.
        Font font;                  //The font to be used.
        JPanel panel;               //Panel to enable use of FontMetrics
        
        lineHeight = (int)((double)height/(double)lines*0.75);
        yCoordinates = new int[lines];
        yCoordinates[0] = y + lineHeight;
        panel = new JPanel();
        
        for(int i = 1; i < lines; i++)  //calculate y coordinate for each individual line
        {
            yCoordinates[i] = (int)(yCoordinates[i-1] + (double)lineHeight/0.75);
        }
        
        currentLine = 0;
        words = message.split(" ");        
        messages = new String[lines];       
        lineMessage = "";
        font = Drawing.getFont(" ", lineHeight*100, lineHeight, RPGStoreGUI.FONTNAME, RPGStoreGUI.FONTSTYLE);
        g.setFont(font);
                
        for(String word : words)  //for each word, determine if it can be added to current line
        {
            if(currentLine >= lines)  //if we require more lines, return false.
            {
                return false;
            }
            
            lineWidth = panel.getFontMetrics(font).stringWidth(lineMessage + " " + word);
            
            if(lineWidth > width)  //if we need another line to contain this word
            {
                messages[currentLine++] = lineMessage;
                lineMessage = word;
            }
            
            else    //add this word to the current line
            {
                lineMessage += " " + word;
            }           
        }
        
        if(currentLine >= lines) //if we require more lines, return false
        {
            return false;
        }
        
        messages[currentLine] = lineMessage;
        
        for(int i = 0; i < messages.length; i++)  //print out each line
        {
            if(messages[i] != null)  //print if not null
            {
                g.drawString(messages[i], x, yCoordinates[i]);               
            }            
        }

        return true;
    }
}
