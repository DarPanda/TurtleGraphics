/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package turtlegraphics;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import uk.ac.leedsbeckett.oop.LBUGraphics;

/**
 *
 * @author Legion
 */
class GraphicsSystem extends LBUGraphics{

    protected String command;
    protected String[] parameters = null;
    protected int error_code = 0;
    protected File canvas = new File("turtle_drawing.png");

    protected File command_store = new File("commands.txt");
    protected ArrayList<String> commandBuffer = new ArrayList<>();
    protected int saveImageStatus = 1;
    protected int saveCommandStatus = 1;


    /**
     * This is a constructor method for the `GraphicsSystem` class.
     * It creates a new `JFrame` object called `MainFrame` and sets its layout to `FlowLayout`.
     * It then adds the current object (`this`), which extends the `LBUGraphics` class
     and represents the turtle graphics panel, to the `MainFrame`.
     * It sets the size of the `MainFrame` using the `pack()` method and makes it visible using the `setVisible()` method.
     * Finally, it sets the default close operation of the `MainFrame` to `JFrame.EXIT_ON_CLOSE`,
     which means that the application will exit when the user closes the window.
     */
    public GraphicsSystem()
    {
        JFrame MainFrame = new JFrame();
        MainFrame.setLayout(new FlowLayout());
        MainFrame.add(this);
        MainFrame.pack();
        MainFrame.setVisible(true);
        MainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        saveChecker(MainFrame);
        if_exists(canvas);

    }







    /**
     * The `about()` method is a custom method defined in the `GraphicsSystem` class.
     * It overrides the `about()` method defined in the `LBUGraphics` class,
     which is called when the user inputs "about" in the text-field.
     */
    @Override
    public void about()
    {
        super.about();
        displayMessage("Darpan Pandey");
    }

    /**
     * The `reset()` method is a custom method defined in the `GraphicsSystem` class.
     * It overrides the 'reset()' method in the `LBUGraphics` class,
     which is called when the user inputs "about" in the text-field.
     * It sets the penStroke and penColor to the default values i.e. (1 and red) respectively.
     */
    @Override
    public void reset()
    {
        super.reset();
        setStroke(1);
        setPenColour(Color.red);
//        penDown();
    }

    /**
     * The `forward()` method is a custom method defined in the `GraphicsSystem` class.
     * It overrides the 'forward()' method in the `LBUGraphics` class,
     which is called when the user inputs "forward" in the text-field passes a number as the pixels the turtle has to travel.
     * It also checks if the turtle has gone out of the 'MainFrame' frame while moving.
     */
    @Override
    public void forward(String amount)
    {
        super.forward(amount);
        turtleOutOfBoundCheck();
    }


    /**The `square()` method takes a string parameter called "passedValue".
     * The method converts the passedValue to an integer and then calculates the x and y positions of the square
     based on the current x and y positions of the object.
     * The method then calls the repaint() method to update the display and draws four lines
     using the current pen color to create a square shape.
     */
    public void square(String passedValue)
    {
        int value = Integer.parseInt(passedValue);
        int tempxPos = xPos+value;
        int tempyPos = yPos+value;

        repaint();

        drawLine(PenColour, xPos, yPos, xPos, tempyPos);
        drawLine(PenColour, xPos, tempyPos, tempxPos, tempyPos);
        drawLine(PenColour, tempxPos, tempyPos, tempxPos, yPos);
        drawLine(PenColour, tempxPos, yPos, xPos, yPos);
    }

    /**The `penColor()` method takes in an array of strings as a parameter.
     * It then converts the string values to integers and stores them in an integer array called "rgbValues". It then creates a new Color object using the RGB values from the integer array and sets the pen color to this new color.
     */
    public void penColor(String[] passedArray)
    {
        int n = 0;
        int[] rgbValues = new int[3];

        for(String value:passedArray)
        {
            rgbValues[n] = Integer.parseInt(value);
            n++;
        }

        Color newPenColor = new Color(rgbValues[0], rgbValues[1], rgbValues[2]);
        setPenColour(newPenColor);
    }

    public void triangle(String passedValue)
    {
        penDown();
        turnLeft(90);
        forward(passedValue);
        turnLeft(120);
        forward(passedValue);
        turnLeft(120);
        forward(passedValue);
        penUp();
    }

    public int[] findAngles(double len1, double len2, double len3)
    {
        double ang1,ang2;
        int[] angle = new int[2];

        ang1 = Math.acos((((len1 * len1) + (len2 * len2)) - (len3 * len3)) / (2 * len1 * len2));
        ang2 = Math.acos((((len2 * len2) + (len3 * len3) - (len1 * len1))) / (2 * len2 * len3));

        angle[0] = (int)(180 - Math.toDegrees(ang1));
        angle[1] = (int)(180 - Math.toDegrees(ang2));

        return angle;
    }

    public void triangle(String[] passedValue)
    {
        int n = 0;
        penDown();
        double[] lengthTriangle = new double[3];

        for (String length:passedValue)
        {
            lengthTriangle[n] = Double.parseDouble(length);
            n++;
        }
        int[] angleTriangle = findAngles(lengthTriangle[0], lengthTriangle[1], lengthTriangle[2]);

        if(lengthTriangle[0]<lengthTriangle[1]+lengthTriangle[2] &&
                lengthTriangle[1]<lengthTriangle[0]+lengthTriangle[2] &&
                lengthTriangle[2]<lengthTriangle[0]+lengthTriangle[1])
        {
            penDown();
            turnLeft(90);
            forward(passedValue[0]);
            turnLeft((angleTriangle[0]));
            forward(passedValue[1]);
            turnLeft((angleTriangle[1]));
            forward(passedValue[2]);
            penUp();
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Invalid values of a triangle", command, HEIGHT);
        }
    }

    public void maze()
    {
        repaint();
        //upperwall
        drawLine(PenColour, 60, 315, 715, 315);
        drawLine(PenColour, 715, 315, 715, 110);
        drawLine(PenColour, 715, 110, 85, 110);
        drawLine(PenColour, 85, 110, 85, 175);
        drawLine(PenColour, 85, 175, 300, 175);
        //lowerwall
        drawLine(PenColour, 60, 390, 790, 390);
        drawLine(PenColour, 790, 390, 790, 35);
        drawLine(PenColour, 790, 35, 10, 35);
        drawLine(PenColour, 10, 35, 10, 250);
        drawLine(PenColour, 10, 250, 300, 250);
    }

    public void solve()
    {

        setxPos(30);
        setyPos(352);
        turnLeft();
        penDown();
        forward (725);
        turnLeft();
        forward(280);
        turnLeft();
        forward (705);
        turnLeft();
        forward (140);
        turnLeft();
        forward (320);
        circle(40);
        penUp();
        JOptionPane.showMessageDialog(null, "Maze Solved", "Solved", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * The `splitCommand()` method takes a string `passedValue` as input,
     which is the command entered by the user.
     * It splits the command into two parts: the command itself and its parameters.
     * It does this by using the `split()` method to split the string at every space character,
     and then assigns the first element of the resulting array to the `command` variable
     * and the rest of the elements to the `parameters` variable using the `Arrays.copyOfRange()` method.
     * This method returns a new array containing a range of elements from the original array,
     in this case from index 1 to the end of the array. This allows the `processCommand()` method
     to access the command and its parameters separately.
     */
    public void splitCommand(String passedValue)
    {
        command = null;
        parameters = null;
        String[] splitValues = passedValue.split(" ");
        command = splitValues[0];

        if (splitValues.length > 1)
        {
            parameters = Arrays.copyOfRange(splitValues,1,splitValues.length);
        }
    }


    /**
     * The `checkCommand()` method is checking if the command entered by the user is a valid command.
     * It takes a string `passedCommand` as input, which is the command entered by the user.
     * It compares this command with a list of valid commands stored in an array `commandList`.
     * If the command entered by the user matches any of the valid commands, the method does nothing.
     * If the command entered by the user does not match any of the valid commands,
     it sets the `error_code` variable to 1 and calls the `errorMessage()` method to display an error message.
     * The method uses a `for` loop to iterate through the `commandList` array and a `break` statement to exit the loop
     once a match is found.
     */
    public void checkCommand(String passedCommand)
    {

        String[] commandList = {"about","pendown","penup","clear","forward","reset",
                "backward","turnright","turnleft","white","cyan","red",
                "pink","magenta","green","black","square","pencolour","penwidth",
                "triangle","forbiddenSign","save","load","savecmd","loadcmd","maze","solve"
        };
        for (String check:commandList)
        {
            if (check.equals(passedCommand))
            {
                error_code = 0;
                checkParamNull(check);

                break;
            }
            else
            {
                error_code = 1;

            }

        }

    }

    public void checkParamNull(String passedValue)
    {
        String[] commandWithParam = {"forward","backward","turnright","turnleft","square","penwidth"};

        if (passedValue.equals("pencolour"))
        {
            if (parameters != null)
            {
                if (parameters.length != 3)
                {
                    error_code = 3;
                }
                else
                {
                    checkParamType();
                }
            }
            else
            {
                error_code = 3;
            }
        }
        else
        {
            for (String checkParam:commandWithParam)
            {
                if (checkParam.equals(passedValue))
                {
                    if (parameters == null)
                    {
                        error_code = 3;
                        break;
                    }

                    else
                    {
                        checkParamType();
                        return;
                    }
                }
            }
        }
    }

    public void checkParamType()
    {
        for(String check:parameters)
        {
            try
            {
                if (check.indexOf('-') == -1)
                {
                    error_code = 0;
                }
                else
                {
                    error_code = 4;
                    break;
                }

                Integer.valueOf(check);
            }
            catch(NumberFormatException error)
            {
                error_code = 4;
            }


        }
    }

    public void checkBlank(String passedValue)
    {
        if(passedValue.length() == 0)
        {
            error_code = 2;
        }
    }

    public void checkTriangle()
    {
        if (command.equals("triangle"))
        {
            if (parameters != null)
            {
                if (parameters.length == 1 || parameters.length == 3)
                {
                    checkParamType();
                }
                else
                {
                    error_code = 3;
                }
            }
            else
            {
                error_code = 3;
            }
        }
    }

    /**
     * The `errorMessage()` method is a helper method that displays an error message in a dialog box
     if an invalid command is entered.
     * It checks the value of the `error_code` variable and displays an appropriate error message
     using the `JOptionPane.showMessageDialog()` method.
     */
    public void errorMessage()
    {
        switch (error_code)
        {
            case 1:
                JOptionPane.showMessageDialog(null, "Invalid Command Detected",
                        "Invalid Command",JOptionPane.ERROR_MESSAGE
                );
                break;

            case 2:
                JOptionPane.showMessageDialog(null, "No Commands Found",
                        "Command Null", JOptionPane.WARNING_MESSAGE
                );
                break;

            case 3:
                JOptionPane.showMessageDialog(null, "Parameter Missing (Null or Lower than required)",
                        "Missing Parameter", JOptionPane.ERROR_MESSAGE
                );
                break;

            case 4:
                JOptionPane.showMessageDialog(null, "Invalid Parameter Detected",
                        "Invalid Parameter", JOptionPane.ERROR_MESSAGE
                );
                break;
        }
    }

    public void saveImage()
    {
        BufferedImage turtle_drawing = getBufferedImage();

        try
        {
            ImageIO.write(turtle_drawing, "png", canvas);
            saveImageStatus = 1;
        }
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null, "The image couldn't be saved", "saving error", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void loadImage()
    {

        try
        {
            BufferedImage turtle_drawing = ImageIO.read(canvas);
            setBufferedImage(turtle_drawing);
        }
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null, "The image couldn't be loaded", "loading error", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void commandBuffer(String passedCommand, String passedValue)
    {

        if ((!passedCommand.equals("clear")) && (!passedCommand.equals("save")) && (!passedCommand.equals("load"))
                && (!passedCommand.equals("savecmd")) && (!passedCommand.equals("loadcmd"))
        )
        {
            commandBuffer.add(passedValue);
            saveCommandStatus = 0;
            saveImageStatus = 0;

        }
    }

    public void saveCommand()
    {
        try
        {
            FileWriter codeWriter = new FileWriter(command_store);
            for (String cmd:commandBuffer)
            {
                codeWriter.write(cmd);
                codeWriter.write("\n");
            }
            codeWriter.close();
            saveCommandStatus = 1;

        }
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(null, "The commands couldn't be saved", "saving error", JOptionPane.WARNING_MESSAGE);
        }



    }

    public void loadCommand()
    {
        try
        {
            Scanner codes = new Scanner(command_store);
            while(codes.hasNextLine())
            {
                String saved_command = codes.nextLine();
                processCommand(saved_command);
            }
            codes.close();
        }
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(null, "The commands couldn't be loaded", "saving error", JOptionPane.WARNING_MESSAGE);
        }
    }

    public final void saveChecker(JFrame passedFrame)
    {
        passedFrame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent closeEvent)
            {
                if (saveCommandStatus == 0)
                {
                    int saveCommandChoice = JOptionPane.showConfirmDialog(null, "Do you want to save your commands before closing?", "Commands Not Saved", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                    switch (saveCommandChoice)
                    {
                        case JOptionPane.YES_OPTION:
                            if (saveImageStatus == 0)
                            {
                                int saveImageChoice = JOptionPane.showConfirmDialog(null, "Do you want to save your image as well?", "Image Not Saved", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                                if (saveImageChoice == JOptionPane.YES_OPTION)
                                {
                                    saveImage();
                                    saveCommand();
                                    passedFrame.dispose();
                                    System.exit(0);
                                }
                                else
                                {
                                    saveCommand();
                                    passedFrame.dispose();
                                    System.exit(0);
                                }
                            }
                            else
                            {
                                saveCommand();
                                System.exit(0);
                            }

                            break;
                        case JOptionPane.NO_OPTION:
                            if (saveImageStatus == 0)
                            {
                                int saveImageChoice = JOptionPane.showConfirmDialog(null, "Do you want to save your image?", "Image Not Saved", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                                if (saveImageChoice == JOptionPane.YES_OPTION)
                                {
                                    saveImage();

                                    passedFrame.dispose();
                                    System.exit(0);
                                }
                                else
                                {
                                    passedFrame.dispose();
                                    System.exit(0);
                                }
                            }
                            break;
                        case JOptionPane.CANCEL_OPTION:
                            break;
                    }
                }
                else
                {
                    passedFrame.dispose();
                    System.exit(0);
                }
            }
        });
    }


    public final void if_exists(File passedFile)
    {
        if(passedFile.exists())
        {
            int load_choice = JOptionPane.showConfirmDialog(null, "Do you want to load your previously saved image ?", "Saved Image Found", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
            if (load_choice == JOptionPane.YES_OPTION)
            {
                loadImage();
            }
        }
    }

    //The above code is checking if the turtle's current position is out of the frame (800x400). If it is, it prompts the user with a confirmation dialog asking if they want to reset the turtle's position. If the user chooses to reset, the turtle's position is reset to its initial position.
    public void turtleOutOfBoundCheck()
    {
        int x = getxPos();
        int y = getyPos();
        if (x<0 || x>800 || y<0 || y>400)
        {
            int choice = JOptionPane.showConfirmDialog(null, "The Turtle is out of frame. Do you want to reset its position?", "Turtle out of Frame", JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
            if (choice  == JOptionPane.YES_OPTION)
            {
                reset();
            }
        }
    }

    /**
     * The `public void processCommand(String commandLine)` method is a required method
     in the `LBUGraphics` class that is being overridden in the `GraphicsSystem` class.
     * It takes a string `commandLine` as input, which is the command entered by the user in the text field.
     * The method then splits the command into a command and its parameters using the `splitCommand()` method,
     checks if the command is valid using the `checkCommand()` method,
     and then executes the appropriate action based on the command using a `switch` statement.
     * The method is responsible for processing the user's input and updating the turtle graphics panel accordingly.
     */
    //The above code is implementing a method called "processCommand" which takes a string parameter "commandLine". The method splits the command line into individual commands, checks for errors in the command, checks if the command is a triangle command, and then executes the appropriate command based on a switch statement. The commands include moving the turtle forward or backward, turning it left or right, changing the pen color and width, drawing shapes like squares and triangles, saving and loading images and commands, and solving a maze. If there is an error in the command, the method calls the "errorMessage" method.
    @Override
    public void processCommand(String commandLine)
    {
        splitCommand(commandLine);
        checkCommand(command);
        checkBlank(command);
        checkTriangle();


        if (error_code == 0)
        {
            commandBuffer(command, commandLine);
            switch(command)
            {
                case "about":
                    this.about();
                    break;

                case "pendown":
                    setPenState(true);
                    break;

                case "penup":
                    setPenState(false);
                    break;

                case "clear":
                    clear();
                    commandBuffer.clear();
                    break;

                case "forward":
                    forward(parameters[0]);
                    break;

                case "reset":
                    this.reset();
                    break;

                case "backward":
                    String backward_value = "-"+parameters[0];
                    forward(backward_value);
                    break;

                case "turnright":
                    turnRight(parameters[0]);
                    break;

                case "turnleft":
                    turnLeft(parameters[0]);
                    break;

                case "white":
                    setPenColour(Color.WHITE);
                    break;

                case "black":
                    setPenColour(Color.BLACK);
                    break;

                case "cyan":
                    setPenColour(Color.CYAN);
                    break;

                case "red":
                    setPenColour(Color.RED);
                    break;

                case "pink":
                    setPenColour(Color.PINK);
                    break;

                case "magenta":
                    setPenColour(Color.MAGENTA);
                    break;

                case "green":
                    setPenColour(Color.GREEN);
                    break;

                case "square":
                    square(parameters[0]);
                    break;

                case "pencolour":
                    penColor(parameters);
                    break;

                case "penwidth":
                    setStroke(Integer.parseInt(parameters[0]));
                    break;

                case "triangle":
                    if (parameters.length == 1)
                    {
                        triangle(parameters[0]);
                    }
                    else
                    {
                        triangle(parameters);
                    }

                    break;

                case "save":
                    saveImage();
                    break;

                case "load":
                    loadImage();
                    break;

                case "savecmd":
                    saveCommand();
                    break;

                case "loadcmd":
                    loadCommand();
                    break;

                case "maze":
                    maze();
                    break;

                case "solve":
                    solve();
                    break;
            }
        }
        else
        {
            errorMessage();
        }

    }
}
