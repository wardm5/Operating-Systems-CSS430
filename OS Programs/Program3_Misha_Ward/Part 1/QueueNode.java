// Misha Ward
// CSS430

import java.util.*;
public class QueueNode {
    private List<Integer> arr;
    public QueueNode(){
        arr = new ArrayList<Integer>();
    }
    public synchronized int sleep( ) {  // sleep
        if(arr.size() == 0){
            try {
                wait();  // have the process wait
            } catch ( InterruptedException e ) {  // when process gets interrupted
                SysLib.cerr(e.toString() + "\n");   // an interuption occurred, print it
            }
            return arr.remove(0); // remove first item in list and return it
        }
        return -1; // return -1 to alert the user that there is more in the queue
    }
    public synchronized void wake(int tid){  // wake
        arr.add(tid);  // add to end of list
        notify();  // notify the process
    }
}
