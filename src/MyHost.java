/* Implement this class. */

import java.util.Comparator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class MyHost extends Host {
    public PriorityBlockingQueue<Task> queue = new PriorityBlockingQueue<>(10, new Comparator<Task>() {
        @Override
        public int compare(Task t1, Task t2) {
            if(t1.getPriority() > t2.getPriority()) {
                return -1;
            } else if(t1.getPriority() < t2.getPriority()) {
                return 1;
            } else {
                if(t1.getStart() > t2.getStart()) {
                    return 1;
                } else if(t1.getStart() < t2.getStart()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        }
    });

    public PriorityBlockingQueue<Task> preemptedTasks = new PriorityBlockingQueue<>(10, new Comparator<Task>() {
        @Override
        public int compare(Task t1, Task t2) {
            if(t1.getPriority() > t2.getPriority()) {
                return -1;
            } else if(t1.getPriority() < t2.getPriority()) {
                return 1;
            } else {
                if(t1.getStart() > t2.getStart()) {
                    return 1;
                } else if(t1.getStart() < t2.getStart()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        }
    });
    public Task runningTask = null;
    long timeMoment;
    long runningTaskTime;
    boolean shutdown = false;
    boolean a = false;
    long workLeft = 0;

    @Override
    public void run() {
        timeMoment = 0;
        while(!shutdown) {
            if(runningTask == null) {
                if(preemptedTasks.size() != 0) {
                    runningTask = preemptedTasks.poll();
                    runningTaskTime = timeMoment;
                }
                else if(queue.size() != 0) {
                    runningTask = queue.poll();
                    runningTaskTime = timeMoment;
                }
            } else {
                workLeft -= (timeMoment - runningTaskTime);

                if(runningTask.getLeft() <= 0) {
                    runningTask.finish();
                    runningTask = null;
                } else {
                    runningTask.setLeft(runningTask.getLeft() - (timeMoment - runningTaskTime));
                    runningTaskTime = timeMoment;

                    if(runningTask.isPreemptible() && queue.size() != 0 && queue.peek().getPriority() > runningTask.getPriority()) {
                        preemptedTasks.add(runningTask);
                        runningTask = queue.poll();
                        runningTaskTime = timeMoment;
                    }
                }
            }
            timeMoment = System.currentTimeMillis();
        }
    }

    @Override
    public void addTask(Task task) {
        workLeft += task.getDuration();
        queue.add(task);
    }

    @Override
    public int getQueueSize() {
        return runningTask == null ? queue.size() + preemptedTasks.size() : queue.size() + preemptedTasks.size() + 1;
    }

    @Override
    public long getWorkLeft() {
        return workLeft;
    }

    @Override
    public void shutdown() {
        shutdown = true;
    }

    public Task getRunningTask() {
        return runningTask;
    }
}
