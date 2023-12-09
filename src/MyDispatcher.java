/* Implement this class. */

import java.util.List;

public class MyDispatcher extends Dispatcher {

    public MyDispatcher(SchedulingAlgorithm algorithm, List<Host> hosts) {
        super(algorithm, hosts);
    }

    @Override
    public void addTask(Task task) {
        System.out.println(algorithm);
        if(algorithm == SchedulingAlgorithm.SIZE_INTERVAL_TASK_ASSIGNMENT) {

        } else if(algorithm == SchedulingAlgorithm.SHORTEST_QUEUE) {

        } else if(algorithm == SchedulingAlgorithm.ROUND_ROBIN) {

        } else if(algorithm == SchedulingAlgorithm.LEAST_WORK_LEFT) {

        }
    }
}
