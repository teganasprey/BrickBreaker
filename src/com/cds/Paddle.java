package com.cds;

import java.awt.*;
import java.lang.*;


public class Paddle extends Brick
{
    // instance variables
    private int speed;

    // accessors
    public int getSpeed() {return speed;}
    public void setSpeed(int sp) {speed = sp;}

    public Paddle(int x, int y, int width, int height, Color color, int sp)
    {
        super(x, y, width, height, color, 0);
        speed = sp;
    }

    public void moveRight()
    {
        setX(Math.min(getX()+speed, 495-getWidth()));
    }

    public void moveLeft()
    {
        setX(Math.max(getX()-speed, 5));
    }

    public boolean hit()
    {
        // the paddle never disappears
        setVisible(true);
        return true;
    }

}
