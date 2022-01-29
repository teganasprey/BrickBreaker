
import java.awt.*;
import javax.swing.*;
import javax.imageio.*;
import java.io.*;


public class Ball
{
    // instance variables - replace the example below with your own
    private int x;          // top left x value
    private int y;          // top left y value
    private int size;       // diameter
    private int dx;
    private int dy;
    private Object objectLastHit = new Object();
    private Image ghost;
    private boolean gameOver;
    
    private final String BALL_IMAGE_FILENAME = "ghost.png";
    
    // encapsulation
    // accessors
    public int getX() {return x;}
    public int getY() {return y;}
    public int getDx() {return dx;}
    public int getDy() {return dy;}
    public int getSize() {return size;}
    public boolean getGameOver() {return gameOver;}

    // mutators
    public void setX(int x) {this.x = x;}
    public void setY(int y) {this.y = y;;}
    public void setDx(int dx) {this.dx = dx;}
    public void setDy(int dy) {this.dy = dy;}
    public void setSize(int size) {this.size = size;}

    // constructors
    public Ball(int x, int y, int size, int dx, int dy)
    {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.size = size;
        try {
            ghost = ImageIO.read(new File(BALL_IMAGE_FILENAME));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public Ball()
    {
    }

    public void move(Graphics g)
    {
        x += dx;

        // check collision with left and right walls
        if (x < 5)
        {
            x = 5;
            dx = -dx;
        }
        else if (x+size > 495)
        {
            x = 495-size;
            dx = -dx;
        }

        y += dy;

        // check collision with top and bottom walls
        if (y < 5)
        {
            y = 5;
            dy = -dy;
        }
        else if (y+size > 500)
        {
            y = 500-size;
            dy = 0;
            dx = 0;
            gameOver = true;
        }

        draw(g);
    }

    public void draw(Graphics g)
    {
        g.fillOval(x, y, size, size);
        g.drawImage(ghost, x, y, null);
        
    }

    public int collidesWith(Brick b)
    {
        // initialize the score to increment
        int scoreToAdd = 0;

        // keep track whether a collision has occurred
        boolean hasCollided = false;

        // set up min and max limits for left collision
        int leftMinY = b.getY();
        int leftMaxY = b.getY() + b.getHeight();
        int leftMinX = b.getX();
        int leftMaxX = b.getX() + Math.min(Math.abs(dx), b.getWidth());

        // set up min and max limits for right collision
        int rightMinY = leftMinY;
        int rightMaxY = leftMaxY;
        int rightMinX = Math.max(b.getX(), b.getX() + b.getWidth() - Math.abs(dx));
        int rightMaxX = b.getX() + b.getWidth();

        // set up min and max limits for top collision
        int topMinX = b.getX();
        int topMaxX = b.getX() + b.getWidth();
        int topMinY = b.getY();
        int topMaxY = b.getY() + Math.min(Math.abs(dy), b.getHeight());

        // set up min and max limits for bottom collision
        int bottomMinX = topMinX;
        int bottomMaxX = topMaxX;
        int bottomMinY = Math.max(b.getY(), b.getY() + b.getHeight() - Math.abs(dy));
        int bottomMaxY = b.getY() + b.getHeight();

        // check all collisions
        if ((x+size >= leftMinX) && (x+size <= leftMaxX) && (y <= leftMaxY) && (y >= leftMinY) && (dx > 0))
        {
            // left collision
            dx =- dx;
            x = b.getX() - size;
            hasCollided = true;
        }
        else if ((x <= rightMaxX) && (x >= rightMinX) && (y <= rightMaxY) && (y >= rightMinY) && (dx < 0))
        {
            // right collision
            dx =- dx;
            x = b.getX() + b.getWidth();
            hasCollided = true;
        }
        else if ((y + size >= topMinY) && (y + size <= topMaxY) && (x <= topMaxX) && (x >= topMinX) && (dy > 0))
        {
            // top collision
            dy = -dy;
            y = b.getY() - size;
            hasCollided = true;
        }
        else if ((y <= bottomMaxY) && (y >= bottomMinY) && (x <= bottomMaxX) && (x >= bottomMinX) && (dy < 0))
        {
            // bottom collision
            dy = -dy;
            y = b.getY() + b.getHeight();
            hasCollided = true;
        }

        if (!hasCollided)
        {
            // check that the ball ever enters the brick (even corners)
            if ((x+size >= b.getX()) && (y+size >= b.getY()) && (x <= b.getX()+b.getWidth()) && (y <= b.getY()+b.getHeight()))
            {
                dx = -dx;
                dy = -dy;
                hasCollided = true;
            }
        }

        if (hasCollided)
        {
            BrickBreakerGame.playSound("quick-blip.wav");
            if (b instanceof Paddle) {
                if (objectLastHit instanceof Paddle)
                {
                    // the paddle has been hit twice in a row, speed up the ball
                    dx *= 2;
                    dy *= 2;
                }
            }
            else {
                boolean brickAlive = b.hit();
                if (!brickAlive)
                {
                    scoreToAdd = 100;
                }
            }
            
            
            // keep track of the object last hit
            objectLastHit = b;
        }

        return scoreToAdd;
    }
}
