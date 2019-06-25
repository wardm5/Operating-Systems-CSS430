// Misha Ward
// CSS430

public class SyncQueue {
    private QueueNode[] queue;  // required
    private static int DEFAULT_TID = 0;  // required
    private static int DEFAULT_COND = 10;  // required

    public SyncQueue() {  // required method
        queue = new QueueNode[DEFAULT_COND];  // create new queueNode array
        for(int i = 0; i < DEFAULT_COND; i++) {  // loop through 10 times
            queue[i] = new QueueNode();  // add into queue array new QueueNodes
        }
    }
    public SyncQueue(int condMax) {  // required method
        queue = new QueueNode[condMax];  // create new queueNode array with passed in amount of nodes
        for(int i = 0; i < condMax; i++) {  //loop through x times
            queue[i] = new QueueNode();  // put QueueNodes into array
        }
    }
    public int enqueueAndSleep(int condition) {  // required method
        if(condition >= 0 && condition < queue.length) { // if condition is less than length and greater than 0...
            return queue[condition].sleep();  // return the
        }
        return -1; // return error code -1
    }
    public void dequeueAndWakeup(int condition) {  // required method
        if(condition >= 0 && condition < queue.length) {  // if condition is greater or equal to 0, and less than length...
            queue[condition].wake(DEFAULT_TID);  // wake the thread
        }
    }
    public void dequeueAndWakeup(int condition, int tid) {  // required method
        if (condition >= 0 && condition < queue.length) { // if condition is greater than or equal to 0, and less than length...
            queue[condition].wake(tid);  // wake the specific thread
        }
    }
}
