// Programmer:  Michael Baccia, Tran Dao, Anthony Nedumgottil, Aaron Zheng
// Assignment:  Project 4 - Final Project
// Date:        12/03/15
// Description: This class is used to represent the X and Y coordinates of a given point as
//              percentages of the width and height of the window.

public class ScaledPoint
{
    private static int windowWidth = 800;   // The width of the window in pixels.
    private static int windowHeight = 600;  // The height of the window in pixels.

    private double xScaler;                 // The scaler to apply for the X coordinate.
    private double yScaler;                 // The scaler to apply for the Y coordinate.

    public ScaledPoint()
    // POST: This creates an object of ScaledPoint with xScaler set to 0 and yScaler
    //       set to 0.
    {
        xScaler = 0;
        yScaler = 0;
    }

    public ScaledPoint(double xScaler, double yScaler)
    // PRE:  xScaler and yScaler must be values between 0 and 1 (inclusive).
    // POST: This creates an object of ScaledPoint with scaledX set to xScaler and yScaler
    //       set to yScaler.
    {
        this.xScaler = xScaler;
        this.yScaler = yScaler;
    }

    public ScaledPoint(int xCoordinate, int yCoordinate)
    // PRE:  xCoordinate and yCoordinate must be non-negative values <= windowWidth
    //       and windowHeight respectively.
    // POST: This creates an object of ScaledPoint with scaledX set to xCoordinate/windowWidth
    //       and scaledY set to yCoordinate/windowHeight.
    {
        xScaler = (double)xCoordinate/(double)windowWidth;
        yScaler = (double)yCoordinate/(double)windowHeight;
        
        if(xScaler > 1)  //if xCoordinate was greater than the windowWidth
        {
            xScaler = 1;
        }
        
        if(yScaler > 1)  //if yCoordinate was greater than the windowHeight
        {
            yScaler = 1;
        }
        
        if(xScaler < 0)  //if xCoordinate was negative
        {
            xScaler = 0;
        }
        
        if(yScaler < 0)  //if yCoordinate was negative
        {
            yScaler = 0;
        }
    }
    
    public double getXScaler()
    // POST: FCTVAL == xScaler
    {
        return xScaler;
    }
    
    public double getYScaler()
    // POST: FCTVAL == yScaler
    {
        return yScaler;
    }

    public void setXScaler(double xScaler)
    // PRE:  xScaler must be a value between 0 and 1 (inclusive).
    // POST: This sets xScaler to xScaler.
    {
        if(xScaler < 0)     //if xScaler was below 0, set to 0
        {
            this.xScaler = 0;
        }
        
        else if(xScaler > 1)  //if xScaler was above 1, set to 1
        {
            this.xScaler = 1;
        }
        
        else    //xScaler is valid, set to xScaler
        {
            this.xScaler = xScaler;
        }
    }

    public void setYScaler(double yScaler)
    // PRE:  yScaler must be a value between 0 and 1 (inclusive).
    // POST: This sets yScaler to yScaler.
    {
        if(yScaler < 0)    //if yScaler was below 0, set to 0
        {
            this.yScaler = 0;
        }
        
        else if(yScaler > 1)  //if yScaler was above 1, set to 1
        {
            this.yScaler = 1;
        }
        
        else    //yScaler is valid, set to yScaler
        {
            this.yScaler = yScaler;
        }
    }
    
    public void convertX(int xCoordinate)
    // PRE:  xCoordinate must be non-negative and <= windowWidth.
    // POST: This sets xScaler to xCoordinate/windowWidth.
    {
        if(xCoordinate == 0)    //if xCoordinate was zero, set to 0 and return;
        {
            xScaler = 0;
            return;
        }
        
        xScaler = (double)xCoordinate/(double)windowWidth;
        
        if(xScaler > 1)  //if the xCoordinate was larger than windowWidth set to 1
        {
            xScaler = 1;
        }
        
        if(xScaler < 0)  //if the xCoordinate was negative, set to 0
        {
            xScaler = 0;
        }
    }
    
    public void convertY(int yCoordinate)
    // PRE:  yCoordinate must be non-negative and <= windowWidth.
    // POST: This sets yScaler to yCoordinate/windowHeight.
    {
        if(yCoordinate == 0)    //if yCoordinate was zero, set to 0 and return;
        {
            yScaler = 0;
            return;
        }
        
        yScaler = (double)yCoordinate/(double)windowHeight;
        
        if(yScaler > 1)  //if the yCoordinate was larger than windowHeight set to 1
        {
            yScaler = 1;
        }
        
        if(yScaler < 0)  //if the yCoordinate was negative, set to 0
        {
            yScaler = 0;
        }
    }
    
    public void convertXY(int xCoordinate, int yCoordinate)
    // PRE:  xCoordinate must be non-negative and <= windowWidth.
    //       yCoordinate must be non-negative and <= windowWidth.
    // POST: This sets xScaler to xCoordinate/windowWidth, sets yScaler 
    //       to yCoordinate/windowHeight.
    {
        if(xCoordinate == 0)    //if xCoordinate was zero, set to 0
        {
            xScaler = 0;
        }
        
        else    //else calculate new xScaler
        {
            xScaler = (double)xCoordinate/(double)windowWidth;
        }
        
        if(yCoordinate == 0)    //if yCoordinate was zero, set to 0
        {
            yScaler = 0;
        }
        
        else   //else calculate new yScaler
        {
            yScaler = (double)yCoordinate/(double)windowHeight;
        }    
        
        if(xScaler > 1)  //if the xCoordinate was larger than windowWidth set to 1
        {
            xScaler = 1;
        }
        
        if(yScaler > 1)  //if the yCoordinate was larger than windowHeight set to 1
        {
            yScaler = 1;
        }
        
        if(xScaler < 0)  //if the xCoordinate was negative, set to 0
        {
            xScaler = 0;
        }
        
        if(yScaler < 0)  //if the yCoordinate was negative, set to 0
        {
            yScaler = 0;
        }
    }

    public int getScaledX()
    // POST: FCTVAL == the scaled x coordinate in pixels. FCTVAL == windowWidth * xScaler as
    //       an integer.
    {
        return (int) (windowWidth * xScaler);
    }

    public int getScaledY()
    // POST: FCTVAL == the scaled y coordinate in pixels. FCTVAL == windowHeight * yScaler
    //       as an integer.
    {
        return (int) (windowHeight * yScaler);
    }
    
    public String toString()
    // POST: FCTVAL == a string representation of the ScaledPoint class in the following
    //       format: windowWidth: , windowHeight: , xScaler: , yScaler: 
    {
        return "windowWidth: " + windowWidth + ", windowHeight: " + windowHeight +
               ", xScaler: " + xScaler + ", yScaler: " + yScaler;
    }
    
    public static void setWindowDimensions(int width, int height)
    // PRE:  width and height must be values greater than zero.
    // POST: This sets windowWidth to width and sets windowHeight to height.
    {
        if(width <= 0 || height <= 0)  //if dimensions are invalid, just return
        {
            return;
        }
        
        windowWidth = width;
        windowHeight = height;
    }
    
    public static int scalerToX(double scaler)
    // PRE:  scaler should be a value >= 0 and <= 1.
    // POST: FCTVAL == scaler*windowWidth, or 0 when scaler < 0, 1 if scaler > 1.
    {
        double value;   //value to be returned
        value = scaler;
        
        if(value > 1)   //if scaler is outside of upper bound
        {
            value = 1;
        }
        
        if(value < 0)   //if scaler is outside of lower bound
        {
            value = 0;
        }
        
        return (int) (value*windowWidth);        
    }
    
    public static int scalerToY(double scaler)
    // PRE:  scaler should be a value >= 0 and <= 1.
    // POST: FCTVAL == scaler*windowHeight, or 0 when scaler < 0, 1 if scaler > 1.
    {
        double value;    //value to be returned
        value = scaler;
        
        if(value > 1)    //if scaler is outside of upper bound
        {
            value = 1;
        }
        
        if(value < 0)    //if scaler is outside of lower bound
        {
            value = 0;
        }
        
        return (int) (value*windowHeight);        
    }
}