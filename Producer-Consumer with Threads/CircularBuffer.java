package prodcon;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CircularBuffer {
    private int[] buffer;
    private int readIndex;
    private int writeIndex;
    private int count;
    private boolean done;

    private Lock lock = new ReentrantLock();
    private Condition full = lock.newCondition();
    private Condition empty = lock.newCondition();

    public CircularBuffer(int capacity) {
        this.buffer = new int[capacity];
        this.readIndex = 0;
        this.writeIndex = 0;
        this.count = 0;
        this.done = false;
    }

    public boolean isFull() {
        return count == buffer.length;
    }

    public boolean isEmpty() {
        return count == 0;
    }

    public void add(int value) throws InterruptedException {
        lock.lock();
        try {
            while (isFull() && !done) {
                full.await();
            }
            if (done) {
                return;
            }
            buffer[writeIndex] = value;
            writeIndex = (writeIndex + 1) % buffer.length;
            count++;
            empty.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public int remove() throws InterruptedException {
        lock.lock();
        try {
            while (isEmpty() && !done) {
                empty.await();
            }
            if (done) {
                return 0;
            }
            int value = buffer[readIndex];
            readIndex = (readIndex + 1) % buffer.length;
            count--;
            full.signalAll();
            return value;
        } finally {
            lock.unlock();
        }
    }

    public void setDone() {
        lock.lock();
        try {
            done = true;
            empty.signalAll();
            full.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public boolean isDone() {
        return done;
    }
}
