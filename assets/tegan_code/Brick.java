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
    private boolean visible;
    private Image bone;
    private Image bone1;
    private Image bone2;
    private int hits;

    private final String BONE_IMAGE_FILENAME = "bone.png";
    private final String BONE1_IMAGE_FILENAME = "bone1.png";
    private final String BONE2_IMAGE_FILENAME = "bone2.png";

    // accessors
    public int getX() {return x;}
    public int getY() {return y;}
    public int getWidth() {return width;}
    public int getHeight() {return height;}
    public boolean isVisible() {return visible;}

    // mutators
    public void setX(int x) {this.x = x;}
    public void setY(int y) {this.y = y;}
    public void setWidth(int width) {this.width = width;}
    public void setHeight(int height) {this.height = height;}
    public void setVisible(boolean visible) {this.visible = visible;}

    // constructors
    public Brick(int x, int y, int width, int height, int hits)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.visible = true;
        this.hits=hits;
        try {
            bone = ImageIO.read(new File(BONE_IMAGE_FILENAME));
            bone1 = ImageIO.read(new File(BONE1_IMAGE_FILENAME));
            bone2 = ImageIO.read(new File(BONE2_IMAGE_FILENAME));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public Brick()
    {
    }
    
    // method to set the color of the brick based on its "hits" value
    public Image setImage()
    {
        switch (hits)
        {
            case 0:
            {
                return bone;
                //break;
            }
            case 1:
            {
                return bone1;
                //break;
            }
            case 2:
            {
                return bone2;
                //break;
            }
            default:
            {
                return bone;
            }
        }
    }
    // draw method
    public void draw (Graphics g)
    {
        g.drawImage(setImage(), x, y, null);
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