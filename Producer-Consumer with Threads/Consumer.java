package prodcon;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Consumer implements Runnable {
    private final CircularBuffer buffer;
    private final Lock lock;
    private final Condition full;
    private final Condition empty;

    public Consumer(CircularBuffer buffer, Lock lock, Condition full, Condition empty) {
        this.buffer = buffer;
        this.lock = lock;
        this.full = full;
        this.empty = empty;
    }

    @Override
    public void run() {
        while (!buffer.isEmpty() || !buffer.isDone()) {
            lock.lock();
            try {
                while (buffer.isEmpty() && !buffer.isDone()) {
                    System.out.println(Thread.currentThread().getName() + " consumer is waiting");
                    empty.await();
                }
                if (!buffer.isEmpty()) {
                    int item = buffer.remove();
                    System.out.println(Thread.currentThread().getName() + " consumed " + item);
                    full.signalAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }
}
