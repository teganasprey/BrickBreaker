import java.awt.*;
import java.lang.*;
import javax.imageio.*;
import java.io.*;

public class Paddle extends Brick
{
    // instance variables
    private int speed;
    private Image brick;
    private final String BRICK_IMAGE_FILENAME = "brick.png";
    
    // accessors
    public int getSpeed() {return speed;}
    public void setSpeed(int sp) {speed = sp;}

    public Paddle(int x, int y, int width, int height, int sp)
    {
        super(x, y, width, height, 0);
        speed = sp;
        try {
            brick = ImageIO.read(new File(BRICK_IMAGE_FILENAME));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
    
    public void moveRight()
    {
        setX(Math.min(getX()+speed, 495-getWidth()));
    }

    public void moveLeft()
    {
        setX(Math.max(getX()-speed, 5));
    }
    // draw method
    public void draw (Graphics g)
    {
        g.fillRect(getX(), getY(), getWidth(), getHeight());
        g.drawImage(brick, getX(), getY(), null);
    }

    public boolean hit()
    {
        // the paddle never disappears
        setVisible(true);
        return true;
    }

}
