/* Implement this class. */

import java.util.List;
import java.util.Objects;

public class MyDispatcher extends Dispatcher {

    public MyDispatcher(SchedulingAlgorithm algorithm, List<Host> hosts) {
        super(algorithm, hosts);
    }
    Object lock = new Object();
    int n = hosts.size();
    int previous_node_index = 0;
    @Override
    public void addTask(Task task) {
        synchronized (lock) {
            if (algorithm == SchedulingAlgorithm.SIZE_INTERVAL_TASK_ASSIGNMENT) {
                if (task.getType() == TaskType.SHORT) {
                    hosts.get(0).addTask(task);
                } else if (task.getType() == TaskType.MEDIUM) {
                    hosts.get(1).addTask(task);
                } else if (task.getType() == TaskType.LONG) {
                    hosts.get(2).addTask(task);
                }

            } else if (algorithm == SchedulingAlgorithm.SHORTEST_QUEUE) {
                int min = hosts.get(0).getQueueSize();
                int min_index = 0;
                for (int i = 1; i < n; i++) {
                    if (hosts.get(i).getQueueSize() < min) {
                        min = hosts.get(i).getQueueSize();
                        min_index = i;
                    }
                }
                hosts.get(min_index).addTask(task);
            } else if (algorithm == SchedulingAlgorithm.ROUND_ROBIN) {
                hosts.get(previous_node_index).addTask(task);
                previous_node_index = (previous_node_index + 1) % n;

            } else if (algorithm == SchedulingAlgorithm.LEAST_WORK_LEFT) {
                int min_index = 0;
                for (int i = 1; i < n; i++) {
                    if (Math.round(hosts.get(i).getWorkLeft() / 1000.0) < Math.round(hosts.get(min_index).getWorkLeft() / 1000.0)) {
                        min_index = i;
                    }
                }

                hosts.get(min_index).addTask(task);
            }
        }
    }

}
