package prodcon;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Producer implements Runnable {
    private CircularBuffer buffer;
    private Lock lock;
    private Condition full;
    private Condition empty;
    private int max;

    private int currentValue = 0;

    public Producer(CircularBuffer buffer, Lock lock, Condition full, Condition empty, int max) {
        this.buffer = buffer;
        this.lock = lock;
        this.full = full;
        this.empty = empty;
        this.max = max;
    }

    @Override
    public void run() {
        try {
            while (true) {
                lock.lock();
                while (buffer.isFull() && !buffer.isDone()) {
                    System.out.println(Thread.currentThread().getName() + " producer is waiting");
                    full.await();
                }

                if (buffer.isDone() || currentValue >= max) {
                    empty.signalAll();
                    lock.unlock();
                    break;
                }

                buffer.add(currentValue);
                System.out.println(Thread.currentThread().getName() + " produced " + currentValue);
                currentValue++;

                empty.signalAll();
                lock.unlock();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
    }
}
