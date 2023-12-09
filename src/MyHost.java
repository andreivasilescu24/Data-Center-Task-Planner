/* Implement this class. */

import java.util.concurrent.PriorityBlockingQueue;

public class MyHost extends Host {
    PriorityBlockingQueue<Task> queue = new PriorityBlockingQueue<>();
    public int compare(Task t1, Task t2) {
        if(t1.getPriority() > t2.getPriority()) {
            return 1;
        } else if(t1.getPriority() < t2.getPriority()) {
            return -1;
        } else {
            if(t1.getId() > t2.getId()) {
                return 1;
            } else if(t1.getId() < t2.getId()) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    @Override
    public void run() {
    }

    @Override
    public void addTask(Task task) {
        queue.add(task);
    }

    @Override
    public int getQueueSize() {
        return queue.size();
    }

    @Override
    public long getWorkLeft() {
        return 0;
    }

    @Override
    public void shutdown() {
    }
}
