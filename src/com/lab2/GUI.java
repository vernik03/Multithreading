package com.lab2;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;



public class GUI extends JFrame {
    public JPanel grid;
    JLabel bear;
    public GUI() {
        super("GridLayoutTest");
        setSize(380*4, 380*2);
        setLocation(100, 100);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        grid = new JPanel();
        grid.setLayout(null);
        ArrayList<BeeThread> MyThreadArray = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
           for (int j = 0; j < 4; j++) {
               MyThreadArray.add(new BeeThread(j, i));
               MyThreadArray.get(MyThreadArray.size()-1).start();
           }
        }
        bear = new JLabel(new ImageIcon("src/com/lab2/img/bear.png"));
        grid.add(bear);
        bear.setBounds(50, 100, 94, 89);
        BearThread bear_thread = new BearThread();
        bear_thread.start();
        getContentPane().add(grid);

        // Открываем окно
        setVisible(true);
    }

    public class BearThread extends Thread {
        int speed_x = 2;
        int speed_y = 1;
        public void run() {
            while (true) {
                bear.setLocation(bear.getX() + speed_x, bear.getY() + speed_y);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public class BeeThread extends Thread {
        public boolean run_flag = true;
        JLabel bee;
        BufferedImage bee_left;
        BufferedImage bee_right;
        JLabel back;
        Coords coords;
        public class Coords{
            public int x;
            public int y;
            public Coords(int x, int y){
                this.x=x*380;
                this.y=y*380;
            }
        }

        public BeeThread(int x, int y){
            coords = new Coords(x, y);
            bee = new JLabel();
            try {
                bee_left= ImageIO.read(new File("src/com/lab2/img/bee_left.png"));
                bee_right= ImageIO.read(new File("src/com/lab2/img/bee_right.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            bee.setIcon(new ImageIcon(bee_right));
            grid.add(bee);
            bee.setBounds(5+coords.x, 5+coords.y, 48, 45);

            back=new JLabel(new ImageIcon("src/com/lab2/img/back.jpg"));
            grid.add(back);
            bee.setBounds(coords.x, coords.y, 380, 380);
        }
        public void run() {
            int speed = 5;
            int delay = 10;
            while (bee.getY() < 380+coords.y) {
                bee.setIcon(new ImageIcon(bee_right));
                while (bee.getX() < 380+coords.x) {
                    bee.setLocation(bee.getX() + speed, bee.getY());
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                bee.setLocation(bee.getX(), bee.getY() + speed);
                bee.setIcon(new ImageIcon(bee_left));
                while (bee.getX() > 0+coords.x) {
                    bee.setLocation(bee.getX() - speed, bee.getY());
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                bee.setLocation(bee.getX(), bee.getY() + speed);
            }
        }
    }
}
