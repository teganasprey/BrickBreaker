package com.cds;

import java.awt.*;
import javax.swing.*;
import javax.imageio.*;
import java.io.*;


public class Brick
{
    // instance variables
    private int x;          // top left corner
    private int y;          // top left corner
    private int width;
    private int height;
    private Color color;
    private boolean visible;
    private Image bone;
    private int hits;

    private final String BONE_IMAGE_FILENAME = "C:\\Users\\Steven Asprey\\source\\Java\\cds\\Brick_Breaker\\assets\\images\\brick.png";

    // accessors
    public int getX() {return x;}
    public int getY() {return y;}
    public int getWidth() {return width;}
    public int getHeight() {return height;}
    public boolean isVisible() {return visible;}
    public Color getColor() {return color;}

    // mutators
    public void setX(int x) {this.x = x;}
    public void setY(int y) {this.y = y;}
    public void setWidth(int width) {this.width = width;}
    public void setHeight(int height) {this.height = height;}
    public void setVisible(boolean visible) {this.visible = visible;}
    public void setColor(Color color) {this.color = color;}

    // constructors
    public Brick(int x, int y, int width, int height, Color color, int hits)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.visible = true;
        this.color = color;
        this.hits = hits;
        try {
            bone = ImageIO.read(new File(BONE_IMAGE_FILENAME));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        this.setColor();
    }

    public Brick()
    {
    }

    // method to set the color of the brick based on its "hits" value
    public void setColor()
    {
        switch (hits)
        {
            case 0:
            {
                color = Color.RED;
                break;
            }
            case 1:
            {
                color = Color.BLUE;
                break;
            }
            case 2:
            {
                color = Color.MAGENTA;
                break;
            }
            default:
            {
                color = Color.BLUE;
            }
        }
    }

    // draw method
    public void draw (Graphics g)
    {
        this.setColor();
        g.setColor(color);
        g.fillRect(x, y, width, height);
        //g.drawImage(bone, x, y, null);
    }

    // hit method
    public boolean hit()
    {
        this.hits--;
        if (this.hits == 0) {
            visible = false;
        }
        return visible;
    }
}
   