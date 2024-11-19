# Data Center Task Planner

## MyHost

I have created two **PriorityBlockingQueue** queues: one for the main task queue and the other for preempted tasks. Both queues use a custom comparator that sorts tasks primarily by their priority in descending order, and if priorities are equal, it sorts by the start time of the task in ascending order. Additionally, I have created variables to track the currently running task, the current time in milliseconds, and the time from the running task to help calculate how much time has passed to update the remaining execution time of the task. I also created a variable for `workLeft`, which is used in the LWL algorithm, and a volatile boolean variable indicating whether the `shutdown` method has been called on the host, signaling that execution should stop.

### Method Implementations:

#### `run` method:

This method contains a while loop that stops when the `shutdown` method is called on the host. Initially, it updates the current time. Then, it checks if there is a running task. If no task is running, it will attempt to extract a task from the preempted task queue, and if that is not empty, it updates the time variable for the task with the current time. If the preempted task queue is empty, it extracts the first task from the main task queue and updates the time variable for the task similarly.

If the current task has no more work to do (its `left` value is less than or equal to 0), the `finish` method is called on the task, and the running task variable is set to `null`. If the task still has work left, the `left` value is updated by subtracting the elapsed time from the previous value, and the task’s time variable is updated with the new time.

After updating, the method checks if the running task is preemptable and if the highest priority task in the queue (if any) has a higher priority. If these conditions are met, the currently running task is inserted into the preempted task queue, and the higher priority task is extracted from the queue and becomes the new running task.

#### `addTask` method:

This method adds a new task to the queue.

#### `getQueueSize` method:

This method returns the number of tasks in the host's queue, including the currently running task if applicable.

#### `getWorkLeft` method:

This method returns the sum of the remaining execution times (`left` values) of all tasks in the host, including those in both the task queue, preempted tasks, and the currently running task if there is one.

#### `shutdown` method:

When this method is called, it sets the `shutdown` variable to `true`, allowing the host to stop execution.

---

## MyDispatcher

In the dispatcher, I added `n`, which stores the number of hosts in the list passed to the constructor, `previous_node_index`, which helps in the **ROUND_ROBIN** algorithm to track the last node assigned a task, and a lock for the `addTask` function to synchronize access to avoid concurrent task assignments.

In the `addTask` function, I handle each possible algorithm for assigning tasks to hosts. Tasks are assigned to hosts by calling the `addTask` method of the host, which inserts the new task into the host's queue.

### Task Assignment Algorithms:

#### **SIZE_INTERVAL_TASK_ASSIGNMENT:**

In this case, we always know there are exactly three hosts, so I assign tasks based on their type. If it is a **SHORT** task, it will be assigned to the first host, if it is **MEDIUM**, it will go to the second, and if it is **LONG**, it will go to the third.

#### **SHORTEST_QUEUE:**

In this case, I find which host has the fewest tasks in its queue at the time the new task arrives in the dispatcher (by calling the `getQueueSize()` method) and assign the task to that host.

#### **ROUND_ROBIN:**

In this case, I assign the task to the host with the index equal to the value of `previous_node_index`. This variable starts at 0, as the algorithm assigns the first task to the host at index 0, and after each task assignment, it is recalculated as `(previous_node_index + 1) % n`.

#### **LEAST_WORK_LEFT:**

In this case, I calculate, similarly to the **SHORTEST_QUEUE** algorithm, which host has the least work left to do and assign the new task to it. The calculation is based on the value returned by the `getWorkLeft()` function of each host, which I divide by 1000.0 and round to the nearest second to avoid discrepancies like 2.9 seconds vs 2.95 seconds. Therefore, the comparison is done at the second level.
