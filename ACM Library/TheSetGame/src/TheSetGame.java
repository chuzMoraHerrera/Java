package Homework;

import acm.program.*;
import acm.graphics.*;
import acm.util.*;

import java.awt.Color;
import java.awt.event.MouseEvent;

import acm.io.IODialog;

@SuppressWarnings("serial")
public class TheSetGame extends GraphicsProgram 
{

    IODialog dialog = new IODialog();
    private final RandomGenerator rGen = new RandomGenerator();
    private static final int ROWS = 3;
    private static final int COLUMNS = 4;
    private static final int WIDTH = 120;
    private static final int HEIGHT = 90;
    public static final int APPLICATION_WIDTH = (WIDTH) * (COLUMNS);
    public static final int APPLICATION_HEIGHT = (HEIGHT) * (ROWS);
    private static final int TOTAL_CardS = ROWS * COLUMNS;
    private static final int SET_OF_CardS = 3;

    private int counter = 0;
    private Card[] clicked_Cards = new Card[SET_OF_CardS];
    private Card[] CardArray = new Card[TOTAL_CardS];
    private Color[] backColors = {Color.CYAN, Color.YELLOW, Color.PINK};

    public void run() 
    {
        setSize(APPLICATION_WIDTH, APPLICATION_HEIGHT);

        int arrayIndex = 0;

        for (int row = 0; row < ROWS; row++) 
        {
            for (int col = 0; col < COLUMNS; col++) 
            {
                int color = rGen.nextInt(1, 3);
                int shape = rGen.nextInt(1, 3);
                int shade = rGen.nextInt(1, 3);
                int num = rGen.nextInt(1, 3);

                double xLoc = col * (WIDTH);
                double yLoc = row * (HEIGHT);

                int x = (color * 3) + (shape * 9) + (shade * 27) + num - 39;

                String image = "images/" + x + ".gif";
                GImage myImage = new GImage(image);

                CardArray[arrayIndex] = new Card(color, shape, shade, num, xLoc, yLoc, myImage, WIDTH, HEIGHT, arrayIndex);
                add(CardArray[arrayIndex]);
                arrayIndex++;
            }
        }
        
        addMouseListeners();

    }

    public void mouseClicked(MouseEvent e) 
    {

        GObject maybe_Card = getElementAt(e.getX(), e.getY());

        if (maybe_Card == null) 
        {
            return;
        }

        for (int i = 0; i < SET_OF_CardS; i++) 
        {
            if (clicked_Cards[i] == (Card) maybe_Card) 
            {
                return;
            }
        }

        clicked_Cards[counter] = (Card) maybe_Card;
        clicked_Cards[counter].setBackgroundColor(backColors[counter]);
        counter = counter + 1;
        if (counter == 3) 
        {
            endTurn(checkForMatch(clicked_Cards[0], clicked_Cards[1], clicked_Cards[2]));
        }

        return;
    }

    private void anyMatches() 
    {

        for (int i = 0; i < TOTAL_CardS; i++) 
        {
            for (int j = 0; j < TOTAL_CardS; j++) 
            {
                for (int k = 0; k < TOTAL_CardS; k++) 
                {

                    if (checkForMatch(CardArray[i], CardArray[j], CardArray[k]) && j != k && j != i && i != k) 
                    {
                        return;
                    }
                }
            }
        }
        return;
    }

    private void endTurn(Boolean match) 
    {
        counter = 0;

        if (match) 
        {
            for (int i = 0; i < clicked_Cards.length; i++) 
            {
                double new_CardX = clicked_Cards[i].getXLoc();
                double new_CardY = clicked_Cards[i].getYLoc();
                int color = rGen.nextInt(1, 3);
                int shape = rGen.nextInt(1, 3);
                int shade = rGen.nextInt(1, 3);
                int num = rGen.nextInt(1, 3);
                int index = clicked_Cards[i].getIndex();
                int x = (color * 3) + (shape * 9) + (shade * 27) + num - 39;
                String image = "images/" + x + ".gif";

                GImage myImage = new GImage(image);

                CardArray[index] = new Card(color, shape, shade, num, new_CardX, new_CardY, myImage, WIDTH, HEIGHT, index);
                remove(clicked_Cards[i]);
                clicked_Cards[i] = null;
                add(CardArray[index]);
            }
            dialog.println("Great! You got a match!");

            anyMatches();

            return;

        } 
        else 
        {
            for (int i = 0; i < clicked_Cards.length; i++) 
            {
                clicked_Cards[i].removeBorder();
                clicked_Cards[i] = null;
            }
            dialog.println("Sorry, not a match.");
            return;
        }
    }

    private Boolean checkForMatch(Card cardOne, Card cardTwo, Card cardThree) 
    {

        int num = cardOne.getNum() + cardTwo.getNum() + cardThree.getNum();
        int color = cardOne.getTheColor() + cardTwo.getTheColor() + cardThree.getTheColor();
        int shape = cardOne.getShape() + cardTwo.getShape() + cardThree.getShape();
        int shade = cardOne.getshade() + cardTwo.getshade() + cardThree.getshade();

        int x = (num % 3) + (color % 3) + (shape % 3) + (shade % 3);

        if (x == 0) 
        {
            return true;
        }

        return false;
    }
}