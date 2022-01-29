package com.cds;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class BrickBallGenerator extends Brick
{
    private final String BONE_IMAGE_FILENAME = "C:\\Users\\Tegan Asprey\\source\\Java\\cds\\Brick_Breaker\\assets\\images\\brick.png";
    private Image bone;
    private BrickBreakerGame game;

    public void setGame(BrickBreakerGame game) {this.game = game;}

    // constructors
    public BrickBallGenerator(int x, int y, int width, int height, Color color, int hits)
    {
        super(x, y, width, height, color, hits);
        try {
            bone = ImageIO.read(new File(BONE_IMAGE_FILENAME));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        this.setColor();
    }

    public void draw(Graphics g)
    {
        super.draw(g);
        //g.drawImage(bone, super.getX(), super.getY(), null);
    }

    public boolean hit()
    {
        game.balls_to_add++;
        return super.hit();
    }
}
