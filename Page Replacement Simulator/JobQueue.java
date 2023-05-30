import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class JobQueue {
    private Queue<Process> queue;

    public JobQueue() {
        this.queue = new LinkedList<>();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public void addAll(List<Process> processes) {
        queue.addAll(processes);
    }

    public Process poll() {
        return queue.poll();
    }

    public Process peek() {
        return queue.peek();
    }
}