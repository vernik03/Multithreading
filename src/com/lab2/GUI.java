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
    JLabel hive;
    int width = 380*4;
    int height = 380*2;

    public GUI() {
        super("GridLayoutTest");
        setSize(width, height);
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

        hive = new JLabel(new ImageIcon("src/com/lab2/img/hive.png"));
        grid.add(hive);
        hive.setBounds(width/2 - 109/2, height/2 - 197/2 - 50, 109, 197);


        ArrayList<JLabel> BackArray = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                BackArray.add(new JLabel(new ImageIcon("src/com/lab2/img/back.png")));
                grid.add(BackArray.get(BackArray.size()-1));
                BackArray.get(BackArray.size()-1).setBounds(j*380, i*380, 380, 380);
            }
        }


        getContentPane().add(grid);

        // Открываем окно
        setVisible(true);
    }

    public class BearThread extends Thread {
        int speed_x = 1;
        int speed_y = 1;
        BufferedImage bear_normal;
        BufferedImage bear_scared;
        BufferedImage bear_dead;

        public BearThread() {
            try {
                bear_normal = ImageIO.read(new File("src/com/lab2/img/bear_scared.png"));
                bear_scared = ImageIO.read(new File("src/com/lab2/img/bear_scared.png"));
                bear_dead = ImageIO.read(new File("src/com/lab2/img/bear_dead.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            bear.setIcon(new ImageIcon(bear_normal));
        }
        public void run() {
            while (true) {
                bear.setLocation(bear.getX() + speed_x, bear.getY() + speed_y);
                if (bear.getX() + bear.getWidth() >= width || bear.getX() <= 0) {
                    speed_x = -speed_x;
                }
                if (bear.getY() + bear.getHeight() >= height || bear.getY() <= 0) {
                    speed_y = -speed_y;
                }
                try {
                    Thread.sleep(20);
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

        }
        public void run() {
            int speed = 5;
            int delay = 10;
            while (bee.getY() < 380+coords.y) {
                try {
                bee.setIcon(new ImageIcon(bee_right));
                while (bee.getX() < 380+coords.x) {
                    bee.setLocation(bee.getX() + speed, bee.getY());
                        Thread.sleep(delay);
                }
                for (int i = 0; i < 3; i++) {
                    bee.setLocation(bee.getX(), bee.getY() + speed);
                    Thread.sleep(delay);
                }
                bee.setIcon(new ImageIcon(bee_left));
                while (bee.getX() > 0+coords.x) {
                    bee.setLocation(bee.getX() - speed, bee.getY());
                    Thread.sleep(delay);
                }
                for (int i = 0; i < 3; i++) {
                    bee.setLocation(bee.getX(), bee.getY() + speed);
                    Thread.sleep(delay);
                }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
