/* Implement this class. */

import java.util.List;

public class MyDispatcher extends Dispatcher {

    public MyDispatcher(SchedulingAlgorithm algorithm, List<Host> hosts) {
        super(algorithm, hosts);
    }

    int n = hosts.size();
    int previous_node_index = 0;
    @Override
    public void addTask(Task task) {
        if(algorithm == SchedulingAlgorithm.SIZE_INTERVAL_TASK_ASSIGNMENT) {

        } else if(algorithm == SchedulingAlgorithm.SHORTEST_QUEUE) {

        } else if(algorithm == SchedulingAlgorithm.ROUND_ROBIN) {
            hosts.get(previous_node_index).addTask(task);
            previous_node_index = (previous_node_index + 1) % n;
            System.out.println("Task " + task.getId() + " is assigned to host " + previous_node_index);
            System.out.println(hosts.get(previous_node_index).getQueueSize());

        } else if(algorithm == SchedulingAlgorithm.LEAST_WORK_LEFT) {

        }
    }
}
