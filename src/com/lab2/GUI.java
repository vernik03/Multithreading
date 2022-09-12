package com.lab2;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.*;



public class GUI extends JFrame {
    public JPanel grid;
    JLabel bear;
    JLabel hive;
    int x_size = 4;
    int y_size = 3;
    int width = 380*x_size;
    int height = 380*y_size;

    boolean dead_bear = false;

    public GUI() {
        super("GridLayoutTest");
        setSize(width, height);
        setLocation(100, 100);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        grid = new JPanel();
        grid.setLayout(null);

        ArrayList<BeeThread> MyThreadArray = new ArrayList<>();
        for (int i = 0; i < y_size; i++) {
            for (int j = 0; j < x_size; j++) {
                MyThreadArray.add(new BeeThread(j, i));
                MyThreadArray.get(MyThreadArray.size()-1).start();
            }
        }

        bear = new JLabel(new ImageIcon("src/com/lab2/img/bear.png"));
        grid.add(bear);
        Random rand = new Random();
        int upperboundX = width-94;
        int upperboundY = height-89;
        bear.setBounds(rand.nextInt(upperboundX),  rand.nextInt(upperboundY), 94, 89);
        BearThread bear_thread = new BearThread();
        bear_thread.start();

        hive = new JLabel(new ImageIcon("src/com/lab2/img/hive.png"));
        grid.add(hive);
        hive.setBounds(width/2 - 109/2, height/2 - 197/2 - 50, 109, 197);


        ArrayList<JLabel> BackArray = new ArrayList<>();
        for (int i = 0; i < y_size; i++) {
            for (int j = 0; j < x_size; j++) {
                BackArray.add(new JLabel(new ImageIcon("src/com/lab2/img/back.png")));
                grid.add(BackArray.get(BackArray.size()-1));
                BackArray.get(BackArray.size()-1).setBounds(j*380, i*380, 380, 380);
            }
        }


        getContentPane().add(grid);
        setResizable(false);
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
                bear_normal = ImageIO.read(new File("src/com/lab2/img/bear.png"));
                bear_scared = ImageIO.read(new File("src/com/lab2/img/bear_scared.png"));
                bear_dead = ImageIO.read(new File("src/com/lab2/img/bear_dead.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            bear.setIcon(new ImageIcon(bear_normal));
        }
        public void run() {
            while (!dead_bear) {
                bear.setLocation(bear.getX() + speed_x, bear.getY() + speed_y);
                if (bear.getX() + bear.getWidth()+2 >= width || bear.getX()-2 <= 0) {
                    speed_x = -speed_x;
                }
                if (bear.getY() + bear.getHeight()*1.5-2 >= height || bear.getY() <= 0) {
                    speed_y = -speed_y;
                }
                try {
                    Thread.sleep(40);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            bear.setIcon(new ImageIcon(bear_scared));
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            bear.setIcon(new ImageIcon(bear_dead));
        }
    }


    public class BeeThread extends Thread {
        public boolean run_flag = true;
        Textures bee_textures;
        Textures textures_left;
        Textures textures_right;
        JLabel bee;
        boolean local_kill = false;

        Coords coords;
        public class Coords{
            public int x;
            public int y;
            public Coords(int x, int y){
                this.x=x*380;
                this.y=y*380;
            }
        }

        public class Textures{
            BufferedImage bee;
            BufferedImage bee_50opacity;
            BufferedImage bee_hit;
            public Textures(String direction) {
                try {
                    bee= ImageIO.read(new File("src/com/lab2/img/bee_"+direction+".png"));
                    bee_50opacity = ImageIO.read(new File("src/com/lab2/img/bee_"+direction+"_50opacity.png"));
                    bee_hit = ImageIO.read(new File("src/com/lab2/img/bee_"+direction+"_hit.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public BeeThread(int x, int y){
            coords = new Coords(x, y);
            bee = new JLabel();
            textures_left = new Textures("left");
            textures_right = new Textures("right");
            bee_textures = textures_right;
            bee.setIcon(new ImageIcon(bee_textures.bee));
            grid.add(bee);
            bee.setBounds(5+coords.x, 5+coords.y, 48, 45);

        }

        boolean killTheBear(){
            if (bear.getX() + bear.getWidth() >= bee.getX() && bear.getX() <= bee.getX() + bee.getWidth() &&
                    bear.getY() + bear.getHeight() >= bee.getY() && bear.getY() <= bee.getY() + bee.getHeight()) {
                dead_bear = true;
                local_kill = true;
                bee.setIcon(new ImageIcon(bee_textures.bee_hit));
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                bee.setIcon(new ImageIcon(bee_textures.bee));
                return true;
            }
            return false;
        }

        void setBee(){
            int start_coords_x = bee.getX();
            int start_coords_y = bee.getY();
            double speed_x = ((hive.getX()+hive.getWidth()/2) - bee.getX())/5;
            double speed_y = ((hive.getY()+hive.getHeight()/2) - bee.getY())/5;
            if (hive.getX() < bee.getX()){
                bee_textures = textures_right;
                bee.setIcon(new ImageIcon(bee_textures.bee));
            }
            else {
                bee_textures = textures_left;
                bee.setIcon(new ImageIcon(bee_textures.bee));
            }
            bee.setLocation(hive.getX() + hive.getWidth()/2, hive.getY()+hive.getHeight()/2);
            bee.setIcon(new ImageIcon(bee_textures.bee_50opacity));
            bee.setLocation((int)(bee.getX() - (speed_x/10)*5), (int)(bee.getY() - (speed_y/10)*5));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            bee.setIcon(new ImageIcon(bee_textures.bee));
            while (true) {
                bee.setLocation((int)(bee.getX() - speed_x/12), (int)(bee.getY() - speed_y/12));
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if ((bee.getX() >start_coords_x-70 && bee.getX() < start_coords_x+70) && (bee.getY() > start_coords_y-70 && bee.getY() < start_coords_y +70)){
                    bee_textures = textures_right;
                    bee.setIcon(new ImageIcon(bee_textures.bee));
                    break;
                }
            }
        }

        void returnBee(){
            int speed_x = ((hive.getX()+hive.getWidth()/2-50) - bee.getX())/10;
            int speed_y = ((hive.getY()+hive.getHeight()/2+50) - bee.getY())/10;
            if (hive.getX() < bee.getX()){
                bee_textures = textures_left;
                bee.setIcon(new ImageIcon(bee_textures.bee));
            }
            else {
                bee_textures = textures_right;
                bee.setIcon(new ImageIcon(bee_textures.bee));
            }
            while (true) {
                bee.setLocation(bee.getX() + speed_x/10, bee.getY() + speed_y/10);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if ((bee.getX() > hive.getX() && bee.getX() < hive.getX() + hive.getWidth()) && (bee.getY() > hive.getY() && bee.getY() < hive.getY() + hive.getHeight())){
                    bee.setIcon(new ImageIcon(bee_textures.bee_50opacity));
                    for (int i = 0; i < 5; i++) {
                        bee.setLocation(bee.getX() + speed_x/10, bee.getY() + speed_y/10);
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                }
            }
            bee.setVisible(false);
        }

        public void run() {
            int speed = 5;
            int delay = 10;
            try {
            Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            setBee();
            while (bee.getY() + bee.getHeight()*2 < 380+coords.y && !local_kill) {
                try {
                bee_textures = textures_right;
                bee.setIcon(new ImageIcon(bee_textures.bee));
                while (bee.getX() + bee.getWidth() < 380+coords.x) {
                    bee.setLocation(bee.getX() + speed, bee.getY());
                    if (killTheBear()) {
                        returnBee();
                        break;
                    }
                    Thread.sleep(delay);
                }
                for (int i = 0; i < 5; i++) {
                    bee.setLocation(bee.getX(), bee.getY() + speed);
                    if (killTheBear()) {
                        returnBee();
                        break;
                    }
                    Thread.sleep(delay);
                }
                bee_textures = textures_left;
                bee.setIcon(new ImageIcon(bee_textures.bee));
                while (bee.getX() > 0+coords.x) {
                    bee.setLocation(bee.getX() - speed, bee.getY());
                    if (killTheBear()) {
                        returnBee();
                        break;
                    }
                    Thread.sleep(delay);
                }
                for (int i = 0; i < 5; i++) {
                    bee.setLocation(bee.getX(), bee.getY() + speed);
                    if (killTheBear()) {
                        returnBee();
                        break;
                    }
                    Thread.sleep(delay);
                }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            returnBee();

        }
    }
}
