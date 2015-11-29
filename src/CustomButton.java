// Programmer:  Michael Baccia, Tran Dao, Anthony Nedumgottil, Aaron Zheng
// Assignment:  Project 4 - Final Project
// Date:        12/03/15
// Description: This class creates custom buttons for use in the GUI.

import java.awt.*;
import java.util.Vector;
import javax.swing.JPanel;

public class CustomButton {
    private static Vector<CustomButton> buttons = new Vector<CustomButton>(10, 2); //All the buttons that have been created.
    private static Graphics g = null;                   //The graphics object to draw to.
    
    private final String message;                       //The message on this button.
    
    private String name;                                //The name of this button.    
    private ScaledPoint[] position;                     //The position of this button.
    private boolean isPressed;                          //The state of this button: pressed or depressed.
    private boolean isActive;                           //Determines whether this button can be pressed or not.
    
    public CustomButton()
    //  POST: Create a CustomButton object. The message is set to "OK", position is set to the
    //        upper-left corner, name is set to the number of buttons created. isPressed is set
    //        to false and isActive is set to true.
    {
        message = "OK";
        position = new ScaledPoint[2];
        position[0] = new ScaledPoint(0, 0);
        position[1] = new ScaledPoint(0, 0);
        buttons.addElement(this);
        name = Integer.toString(buttons.size());
        isPressed = false;
        isActive = true;
    }
    
    public CustomButton(String name, String message, ScaledPoint[] position)
    //  PRE:  name, message and position must be initialized.
    //  POST: Create a CustomButton object. The message is set to message, position is set to
    //        position, name is set to name. isPressed is set to false and isActive is set to true.
    {
        this.name = name;
        this.message = message;
        this.position = position;
        isPressed = false;
        isActive = true;
        buttons.addElement(this);        
    }
    
    public static void setGraphics(Graphics g)
    //  PRE:  g must be initialized.
    //  POST: Sets g to g.
    {
        CustomButton.g = g;
    }
    
    public static CustomButton wasPressed(int x, int y)
    //  PRE:  x and y must be >= 0.
    //  POST: FCTVAL == name of the button that contains the x and y coordinates, if any.
    //        Also sets isPressed to true if button was found and redraws it. Returns null if none found.        
    {
        int xStart;     //The start of the x range.
        int xEnd;       //The end of the x range.
        int yStart;     //The start of the y range.
        int yEnd;       //The end of the y range.
        
        for(CustomButton button : buttons)      //for each button created
        {
            xStart = button.position[0].getScaledX();
            xEnd = button.position[1].getScaledX();
            yStart = button.position[0].getScaledY();
            yEnd = button.position[1].getScaledY();
            
            if(x >= xStart && x <= xEnd && y >= yStart && y<= yEnd && button.isActive)  //if x and y are contained in this button
                                                                                        //  and the button is active
            {
                button.isPressed = true;
                return button;
            }
        }
        
        return null;
    }
    
    public static void draw()
    //  PRE:  g must be initialized.
    //  POST: Draws all buttons to Graphics g.
    {
        if(g == null)  //if g was never initialized
        {
            return;
        }
        
        for(CustomButton button : buttons)  //for each button created
        {
            button.redraw();
        }
    }
    
    public static void depress()
    //  POST: Sets isPressed to false for all buttons.
    {
        for(CustomButton button : buttons)  //for each button created
        {
            if(button.isPressed)   //if the button was pressed
            {
                button.isPressed = false;
            }           
        }
    }
    
    public String getName()
    //  POST: FCTVAL == name of this CustomButton object.
    {
        return name;
    }
    
    public void activate()
    //  POST: Sets isActive to true
    {
        isActive = true;
    }
    
    public void deactivate()
    //  POST: Sets isActive to false
    {
        isActive = false;
    }
    
    private void redraw()
    //  POST: Redraws this button, given its current state and position, to g.
    {
        Font font;          //The font to be used.
        Graphics2D g2;      //Object to change brush stroke.
        JPanel panel;       //Panel to enable use of FontMetrics
        int x;              //x coordinate to draw to.
        int y;              //y coordinate to draw to.
        int width;          //Width of draw space.
        int height;         //Height of draw space.
        int xGrad;          //The x coordinate for the gradient.
        int yGrad;          //The y coordinate for the gradient.
        int wGrad;          //The width for the gradient.
        int hGrad;          //The height for the gradient.
        int messageWidth;   //The width of the message.
        int messageOffset;  //The offset for the message.
                
        g2 = (Graphics2D) g;
        panel = new JPanel();
        
        x = position[0].getScaledX();
        y = position[0].getScaledY();
        width = position[1].getScaledX() - position[0].getScaledX();
        height = position[1].getScaledY() - position[0].getScaledY();
        
        xGrad = position[0].getScaledX()+2;
        yGrad = position[0].getScaledY()+2;
        wGrad = position[1].getScaledX() - position[0].getScaledX()-4;
        hGrad = position[1].getScaledY() - position[0].getScaledY()-4;
        
        //draw the border
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(x, y, width, height);
        
        //get parameters to draw message
        font = Drawing.getFont(message, (int)(width*.9), height/2, RPGStoreGUI.FONTNAME, RPGStoreGUI.FONTSTYLE);
        g2.setFont(font);
        messageWidth = panel.getFontMetrics(font).stringWidth(message);
        messageOffset = (width - messageWidth)/2;
                
        //draw gradient
        if(!isActive)  //if this button is inactive
        {
            Drawing.drawGradient(g, new Color(180, 195, 200), new Color(240, 240, 240), xGrad, yGrad, wGrad, hGrad/3);
            Drawing.drawGradient(g, new Color(240, 240, 240), new Color(180, 195, 200), xGrad, yGrad+hGrad/3, wGrad, hGrad-hGrad/3+1);
            g2.setColor(new Color(120, 120, 120));
        }
        
        else if(!isPressed)  //else if this button is not pressed
        {
            Drawing.drawGradient(g, new Color(190, 205, 210), new Color(255, 255, 255), xGrad, yGrad, wGrad, hGrad/3);
            Drawing.drawGradient(g, new Color(255, 255, 255), new Color(190, 205, 210), xGrad, yGrad+hGrad/3, wGrad, hGrad-hGrad/3+1);
        }
        
        else  //if this button was pressed
        {
            Drawing.drawGradient(g, new Color(180, 195, 200), new Color(240, 240, 240), xGrad, yGrad, wGrad, hGrad/3);
            Drawing.drawGradient(g, new Color(240, 240, 240), new Color(180, 195, 200), xGrad, yGrad+hGrad/3, wGrad, hGrad-hGrad/3+1);
            g2.setColor(new Color(80, 80, 80));
        }
        
        g2.drawString(message, x+messageOffset, y+3*height/4);
    }
}