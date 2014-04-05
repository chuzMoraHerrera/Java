package Homework;
import acm.graphics.*;

import java.awt.Color;

@SuppressWarnings("serial")
public class Card extends GCompound 
{

    private final int shape, shade, color, num;
    private final double xLoc, yLoc;

    private final GImage image;
    private final double width;
    private final double height;
    private final int index;
    private static final double IMAGE_WIDTH = 95;
    private static final double IMAGE_HEIGHT = 60;
    private GRect border;

    public Card(int color, int shape, int shade, int num, double xLoc, double yLoc, GImage cardImage, double width, double height, int index) 
    {
        this.color = color;
        this.shape = shape;
        this.shade = shade;
        this.num = num;
        this.xLoc = xLoc;
        this.yLoc = yLoc;
        this.image = cardImage;
        this.width = width;
        this.height = height;
        this.index = index;

        setLocation(xLoc, yLoc);
        displayImage();
    }

    public int getTheColor() 
    {
        return color;
    }

    public int getShape() 
    {
        return shape;
    }

    public int getshade() 
    {
        return shade;
    }

    public int getNum() 
    {
        return num;
    }

    public double getXLoc() 
    {
        return xLoc;
    }

    public double getYLoc() 
    {
        return yLoc;
    }

    public int getIndex() 
    {
        return index;
    }

    private void displayImage() 
    {
        double imageX = (width - IMAGE_WIDTH) / 2;
        double imageY = (height - IMAGE_HEIGHT) / 2;
        image.setBounds(imageX, imageY, IMAGE_WIDTH, IMAGE_HEIGHT);
        image.sendToFront();
        add(image);
    }

    public void setBackgroundColor(Color color) 
    {   border = new GRect(5, 5, width - 10, height - 10);
        border.setColor(color);
        border.setFilled(true);
        add(border);
        border.sendToBack();
    }

    public void removeBorder() 
    {
        remove(border);
    }
}
