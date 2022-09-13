package com.lab2;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Main {

    int x_size = 4;
    int y_size = 3;
    int width = 380*x_size;
    int height = 380*y_size;
    boolean dead_bear = false;


    public static void main(String[] args) {


        ArrayList<GUI.BeeThread> MyThreadArray = new ArrayList<>();
        for (int i = 0; i < y_size; i++) {
            for (int j = 0; j < x_size; j++) {
                MyThreadArray.add(new GUI.BeeThread(j, i));
            }
        }




        ArrayList<JLabel> BackArray = new ArrayList<>();
        for (int i = 0; i < y_size; i++) {
            for (int j = 0; j < x_size; j++) {
                BackArray.add(new JLabel(new ImageIcon("src/com/lab2/img/back.png")));
                grid.add(BackArray.get(BackArray.size()-1));
                BackArray.get(BackArray.size()-1).setBounds(j*380, i*380, 380, 380);
            }
        }


        for (GUI.BeeThread thread: MyThreadArray) {
            thread.start();
        }


    }

    public class Mutex {
        private boolean flag = false;
        public void mute() {
            flag = true;
        }
        public void unmute() {
            flag = false;
        }
        public boolean isMuted() {
            return flag;
        }
    }

    public class BeeThread extends Thread {
        public boolean run_flag = true;
        GUI.BeeThread.Textures bee_textures;
        GUI.BeeThread.Textures textures_left;
        GUI.BeeThread.Textures textures_right;
        JLabel bee;
        boolean local_kill = false;

        GUI.BeeThread.Coords coords;
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

            bee_textures = textures_right;
            bee.setIcon(new ImageIcon(bee_textures.bee));

            bee.setBounds(5+coords.x, 5+coords.y, 48, 45);

            bee.setVisible(false);
        }

        public void run() {
            while (mutex.isMuted()) {
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (!mutex.isMuted()&&!dead_bear){
                mutex.mute();
                int speed = 5;
                int delay = 7;
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
                mutex.unmute();
                returnBee();
            }
        }
    }
}
