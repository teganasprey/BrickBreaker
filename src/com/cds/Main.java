package com.cds;

import javax.swing.JFrame;


public class Main {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setBounds(0, 0, 515, 545);
        frame.setTitle("Brick Breaker by Tegan Asprey");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        BrickBreakerGame game = new BrickBreakerGame(1);
        frame.add(game);
        frame.setVisible(true);
    }
}
