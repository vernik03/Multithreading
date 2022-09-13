package com.lab2;

public class Main {

    public static void main(String[] args) {

        int warehouse_size = 100;
        Munition warehouse=new Munition(warehouse_size);
        Ivanov ivanov = new Ivanov(warehouse);
        Petrov petrov = new Petrov(warehouse);
        Nechiporchuk nechiporchuk = new Nechiporchuk(warehouse);
        warehouse.info();
        new Thread(ivanov).start();
        new Thread(petrov).start();
        new Thread(nechiporchuk).start();
    }
}
// Класс Магазин, хранящий произведенные товары
class Munition{
    public int warehouse;
    private int pile_near_warehouse=0;
    private int truck=0;
    private int check_list=0;

    public Munition(int warehouse){
        this.warehouse=warehouse;
    }

    public synchronized void info() {
        System.out.println("На складі: " + warehouse + " штук");
        System.out.println("Вкрали зі складу: " + pile_near_warehouse + " штук");
        System.out.println("У вантажівці: " + truck + " штук");
        System.out.println("Порахували: " + check_list + " штук");
        System.out.println();
    }

    public synchronized void getFromWarehouse() {
        while (pile_near_warehouse>=10) {
            try {
                wait();
            }
            catch (InterruptedException e) {
            }
        }
        for (int i=0;i<10;i++) {
            if (warehouse>0) {
                warehouse--;
                pile_near_warehouse++;
            }
        }
        info();
        notify();
    }
    public synchronized void getFromPile() {
        while (truck>=10) {
            try {
                wait();
            }
            catch (InterruptedException e) {
            }
        }
        for (int i=0;i<10;i++) {
            if (pile_near_warehouse>0) {
                pile_near_warehouse--;
                truck++;
            }
        }
        info();
        notify();
    }

    public synchronized void getFromTruck() {
        while (truck<1) {
            try {
                wait();
            }
            catch (InterruptedException e) {
            }
        }
        for (int i=0;i<10;i++) {
            if (truck>0) {
                truck--;
                check_list++;
            }
        }
        info();
        notify();
    }
}

class Ivanov implements Runnable{

    Munition store;
    int warehouse_size;
    Ivanov(Munition store){
        this.store=store;
        warehouse_size = store.warehouse;
    }
    public void run(){
        for (int i = 1; i <= warehouse_size/10; i++) {
            store.getFromWarehouse();
        }
    }
}

class Petrov implements Runnable{

    Munition store;
    int warehouse_size;
    Petrov(Munition store){
        this.store=store;
        warehouse_size = store.warehouse;
    }
    public void run(){
        for (int i = 1; i <= warehouse_size/10; i++) {
            store.getFromPile();
        }
    }
}

class Nechiporchuk implements Runnable{

    Munition store;
    int warehouse_size;
    Nechiporchuk(Munition store){
        this.store=store;
        warehouse_size = store.warehouse;
    }
    public void run(){
        for (int i = 1; i <= warehouse_size/10; i++) {
            store.getFromTruck();
        }
    }
}