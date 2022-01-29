package com.cds;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.sound.sampled.*;
import java.io.*;
import javax.imageio.*;


public class BrickBreakerGame extends JPanel implements KeyListener, ActionListener, MouseListener
{
    // instance variables
    private Timer timer;
    private boolean play;
    private ArrayList<Brick> bricks = new ArrayList<>();
    private ArrayList<Ball> balls = new ArrayList<>();
    private Paddle paddle;
    private int screenToShow;                                       // which screen to show
    private Image title_image;                                      // title or start screen
    private Image loser_image;                                      // final lose screen
    private Image win_image;                                        // final win screen
    private boolean winner;
    private int score;
    private int level;
    public int balls_to_add;

    // screen to show options (should be an enum)
    private final int START_SCREEN = 1;
    private final int GAME_SCREEN = 2;
    private final int LOSE_SCREEN = 3;
    private final int WIN_SCREEN = 4;

    // files containing images for the game
    private final String TITLE_IMAGE_FILENAME = "C:\\Users\\Tegan Asprey\\source\\Java\\cds\\Brick_Breaker\\assets\\images\\start_screen.jpg";
    private final String LOSER_IMAGE_FILENAME = "C:\\Users\\Tegan Asprey\\source\\Java\\cds\\Brick_Breaker\\assets\\images\\died_screen.jpg";
    private final String WIN_IMAGE_FILENAME = "C:\\Users\\Tegan Asprey\\source\\Java\\cds\\Brick_Breaker\\assets\\images\\died_screen.jpg";

    // integer representations of brick patterns (should be an enum)
    private final int STAIRCASE = 1;
    private final int RECTANGLE = 2;
    private final int DIAMOND = 3;
    private final int COLUMNS = 4;
    private final int ODD_ROWS = 5;

    public BrickBreakerGame(int num_bricks)
    {
        try {
            title_image = ImageIO.read(new File(TITLE_IMAGE_FILENAME));
            loser_image = ImageIO.read(new File(LOSER_IMAGE_FILENAME));
            win_image = ImageIO.read(new File(WIN_IMAGE_FILENAME));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        addKeyListener(this);
        addMouseListener(this);
        timer = new Timer(20, this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer.start();

        winner = false;
        screenToShow = START_SCREEN;
    }

    public void initialize()
    {
        // reset the level to the first and the score to zero
        if (!winner) {
            level = 1;
            score = 0;
        }

        // clear all previous bricks and balls
        bricks.clear();
        balls.clear();

        // add a ball to the array list of balls
        balls.add(this.generateBall());

        // instantiate the paddle
        paddle = new Paddle(225, 460, 120, 10, Color.red, 25);

        // set up the array of bricks to make an awesome game!
        int brick_pattern = (int)((5-1+1)*Math.random() + 1);
        this.generateBricks(brick_pattern);

        play = false;
    }

    public Ball generateBall()
    {
        // randomize the velocity of the ball and instantiate
        int min = -4;
        int max = 4;
        int dx = (int)((max-min+1)*Math.random()+ min);
        int dy = (int)((max-min+1)*Math.random()+ min);
        dx = dx == 0 ? dx + 1: dx;
        dy = Math.abs(dy) <= 2 ? Math.abs(dy) + 1 : dy;
        Ball ball = new Ball(250, 250, 20, dx, dy, Color.white);
        ball.setGame(this);
        return ball;
    }

    public void generateBricks(int pattern)
    {
        int[][] brick_positions = new int[5][5];
        switch (pattern) {
            case STAIRCASE: {
                // generate staircase pattern of bricks
                for (int i = 0; i < 5; i++) {
                    for (int j = i; j >= 0; j--) {
                        brick_positions[i][j] = 1;
                    }
                }
                break;
            }
            case RECTANGLE: {
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++) {
                        brick_positions[i][j] = 1;
                    }
                }
                break;
            }
            case COLUMNS: {
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++) {
                        if (j % 2 != 0)
                        brick_positions[i][j] = 1;
                    }
                }
                break;
            }
            case ODD_ROWS: {
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++) {
                        if (i % 2 != 0)
                            brick_positions[i][j] = 1;
                    }
                }
                break;
            }
            case DIAMOND: {
                int[][] diamond_brick_positions = {
                        {0, 0, 1, 0, 0},
                        {0, 1, 1, 1, 0},
                        {1, 1, 1, 1, 1},
                        {0, 1, 1, 1, 0},
                        {0, 0, 1, 0, 0}
                };
                brick_positions = diamond_brick_positions;
                break;
            }
        }

        // now draw the bricks
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                int brick_hits = (int) ((3 - 1 + 1) * Math.random() + 1);
                int x = 5 + (8 * (Math.abs(j - 4) + 1) + 90 * (Math.abs(j - 4)));
                int y = 30 * (i + 1);
                if (brick_positions[i][j] == 1) {
                    if (brick_hits == 3) {
                        BrickBallGenerator b = new BrickBallGenerator(x, y, 70, 20, Color.green, 1);
                        b.setGame(this);
                        bricks.add(b);
                    } else {
                        bricks.add(new Brick(x, y, 70, 20, Color.blue, brick_hits));
                    }
                }
            }
        }
    }

    public void paint(Graphics g)
    {
        // paint the relevant screen
        switch (screenToShow) {
            case START_SCREEN: {
                startScreen(g);
                break;
            }
            case GAME_SCREEN: {
                // draw the walls and background
                g.setColor(Color.yellow);
                g.fillRect(0, 0, 500, 500);
                g.setColor(Color.black);
                g.fillRect(5, 5, 490, 495);

                // draw the score
                g.setFont(new Font("TimesRoman", Font.BOLD, 20));
                g.setColor(Color.green);
                g.drawString("Level: " + level + "      Score: " + score, 10, 20);

                // move and draw the balls
                for (Ball ball : balls) {
                    ball.move(g);
                }

                // draw the bricks and check for collisions
                for (Brick brick : bricks) {
                    if (brick.isVisible()) {
                        brick.draw(g);
                        for (Ball ball : balls) {
                            this.score += ball.collidesWith(brick);
                        }
                    }
                }

                // add any balls generated by BrickBallGenerator hits
                for (int i=0; i<this.balls_to_add; i++) {
                    this.balls.add(this.generateBall());
                }
                this.balls_to_add = 0;

                paddle.draw(g);

                for (Ball ball : balls) {
                    ball.collidesWith(paddle);
                }
                checkWinner();
                break;
            }
            case LOSE_SCREEN: {
                loseScreen(g);
                break;
            }
            case WIN_SCREEN: {
                winScreen(g);
                break;
            }
        }
    }

    public void startScreen(Graphics g)
    {
        g.drawImage(title_image, 0,0, null);
    }
    
    public void loseScreen(Graphics g)
    {
        g.drawImage(loser_image, 0,0, null);
    }

    public void winScreen(Graphics g)
    {
        // TODO: change win screen (uses lose screen for now)
        g.drawImage(win_image, 0, 0, null);
    }

    public void actionPerformed(ActionEvent e)
    {
        if (play) {
            repaint();
        }
    }

    public void checkWinner()
    {
        winner = true;
        for (Brick brick : bricks) {
            // if any bricks are still visible, no winner yet
            if (brick.isVisible()) {
                winner = false;
                break;
            }
        }

        if (winner) {
            repaint();
            screenToShow = WIN_SCREEN;
            play = false;
        }

        // check all balls have stopped moving
        boolean lost = true;
        for (Ball ball : balls) {
            if (ball.getDx() != 0) {
                lost = false;
                break;
            }
        }

        if (lost) {
            repaint();
            screenToShow = LOSE_SCREEN;
            play = false;
        }
    }

    public void keyPressed(KeyEvent e)
    {
        play = true;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            paddle.moveRight();
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            paddle.moveLeft();
        }
    }

    public void keyTyped(KeyEvent e)
    {
    }

    public void keyReleased(KeyEvent e)
    {
    }

    public void mouseClicked(MouseEvent e) 
    {
        int x = e.getX();
        int y = e.getY();

        if (screenToShow == START_SCREEN) {
            // check that the user clicked inside the limits of the start "button"
            if (x >= 183 && x <= 320 && y >= 435 && y <= 460) {
                screenToShow = GAME_SCREEN;
                initialize();
                play = true;
            }
        }
        else if (screenToShow == LOSE_SCREEN) {
            // check that the user clicked inside the "button"
            if (x >= 160 && x <= 340 && y >= 250 && y <= 290) {
                screenToShow = START_SCREEN;
                play = true;
            }
        }
        else if (screenToShow == WIN_SCREEN) {
            if (x >= 160 && x <= 340 && y >= 250 && y <= 290) {
                screenToShow = GAME_SCREEN;
                initialize();
                level++;
                play = true;
            }
        }
    }
    
    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void delay() {
        try {
            Thread.sleep(500);
        }
        catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }

    public static synchronized void playSound(final String url) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    File f = new File(url);
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(f);
                    clip.open(inputStream);
                    clip.start();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }
}
