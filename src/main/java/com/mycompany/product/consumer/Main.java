package com.mycompany.product.consumer;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;


/*
    Exercício: Utilizando a linguagem de programação de sua preferência, crie um programa que
    contenha duas tarefas (threads):
        • producer - incrementar o valor de uma variável partir de 0 a cada segundo;
        • consumer - imprimir o valor da variável na tela a cada segundo;
*/
public class Main {

    public static int valueToIncrement;
    public static Lock lock = new ReentrantLock();
    public static Semaphore semaphore = new Semaphore(1);
    public static boolean incremented;

    public static void main(String[] args) throws InterruptedException {
        valueToIncrement = -1;
        incremented = false;

        Runnable r1 = () -> {
            while (true) {
                try {
                    semaphore.acquire();
                    lock.lock();
                    if (!incremented) {
                        System.out.println("Incrementing value...");
                        valueToIncrement++;
                        incremented = true;
                    }
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    lock.unlock();
                    semaphore.release();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        };

        Runnable r2 = () -> {
            while (true) {
                try {
                    semaphore.acquire();
                    lock.lock();
                    if (incremented) {
                        System.out.println("Value: " + valueToIncrement);
                        incremented = false;
                    }
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    lock.unlock();
                    semaphore.release();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
        };

        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);

        t1.start();
        t2.start();
    }

}
