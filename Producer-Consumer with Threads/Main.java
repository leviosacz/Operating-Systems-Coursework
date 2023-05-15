package prodcon;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static void main(String[] args) {
        // Parse command line arguments
        int numConsumers = args.length > 0 ? Integer.parseInt(args[0]) : 2;

        // Create shared circular buffer
        CircularBuffer buffer = new CircularBuffer(5);

        // Create lock and conditions for synchronization
        Lock lock = new ReentrantLock();
        Condition full = lock.newCondition();
        Condition empty = lock.newCondition();

        // Create producer thread
        Producer producer = new Producer(buffer, lock, full, empty, 10);
        Thread producerThread = new Thread(producer);
        producerThread.start();

        // Create consumer threads
        List<Consumer> consumers = new ArrayList<>();
        List<Thread> consumerThreads = new ArrayList<>();
        for (int i = 0; i < numConsumers; i++) {
            Consumer consumer = new Consumer(buffer, lock, full, empty);
            consumers.add(consumer);
            Thread consumerThread = new Thread(consumer);
            consumerThreads.add(consumerThread);
            consumerThread.start();
        }

        // Wait for producer to finish
        try {
            producerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Notify consumers that producer is done
        buffer.setDone();

        // Wait for consumers to finish
        for (Thread consumerThread : consumerThreads) {
            try {
                consumerThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
