package program.test;

import java.util.LinkedList;
import java.util.Queue;

class Producer implements Runnable {
    private Queue<Integer> queue;

    public Producer(Queue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        for (int i=0;i<10;++i) {
            synchronized (queue) {
                queue.offer(i);
                System.out.println("producer put:  " + i);
                queue.notify();
                try {
                    if (i!=9) {
                        queue.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
class Consumer implements Runnable {
    private Queue<Integer> queue;

    public Consumer(Queue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        for (int i=0;i<10;++i) {
            synchronized (queue) {
                while (queue.isEmpty()){
                    System.out.println("queue is null");
                    queue.notify();
                    try {
                        queue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("consumer get " + queue.poll());
                queue.notify();
                try {
                    if (i!=9) {
                        queue.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
public class MultiThread {
    private static Queue<Integer> queue = new LinkedList<>();
    static void name1(){
        new Thread(new Consumer(queue)).start();
        new Thread(new Producer(queue)).start();
    }
    public static void main(String[] args) {
        name1();
    }
}
