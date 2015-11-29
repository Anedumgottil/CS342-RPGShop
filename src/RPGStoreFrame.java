// Programmer:  Michael Baccia, Tran Dao, Anthony Nedumgottil, Aaron Zheng
// Assignment:  Project 4 - Final Project
// Date:        12/03/15
// Description: This implements the frame for the front-end GUI of the RPG store. 
//              This contains the main method that should be executed in order to run the program.


import javax.swing.*;

public class RPGStoreFrame 
{
    
    public static void main(String[] args) 
    {
        createFrame();
    }
    
    private static void createFrame() 
    // POST: This method creates the GUI.
    {
        JFrame      frame;   //frame for GUI
        RPGStoreGUI display; //the RPG store display
        
        frame = new JFrame("RPG Store");
        display = new RPGStoreGUI();
        frame.add(display);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,600);
        frame.setVisible(true);
    }
}
