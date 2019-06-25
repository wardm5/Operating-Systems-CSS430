import java.util.*;

public class Scheduler extends Thread
{
    // 1) It has three queues, numbered from 0 to 2.
    private Vector q0;  // create queue 0
    private Vector q1;  // create queue 1
    private Vector q2;  // create queue 2
    private int timeSlice;
    private static final int DEFAULT_TIME_SLICE = 1000;
    private static final int DEFAULT_MAX_THREADS = 10000;
    // New data added to p161
    private boolean[] tids; // Indicate which ids have been used
    // A new feature added to p161
    // Allocate an ID array, each element indicating if that id has been used
    private int nextId = 0;

    // SCHEDULER CONSTRUCTOR
    public Scheduler( ) {
        timeSlice = DEFAULT_TIME_SLICE;
        q0 = new Vector();
        q1 = new Vector();
        q2 = new Vector();
        initTid( DEFAULT_MAX_THREADS );
    }

    public Scheduler( int quantum ) {
        timeSlice = quantum;
        this();
        // q0 = new Vector();
        // q1 = new Vector();
        // q2 = new Vector();
        // initTid( DEFAULT_MAX_THREADS );
    }

    // A new feature added to p161
    // A constructor to receive the max number of threads to be spawned
    public Scheduler( int quantum, int maxThreads ) {
        timeSlice = quantum;
        q0 = new Vector();
        q1 = new Vector();
        q2 = new Vector();
        initTid( maxThreads );
    }

    private void schedulerSleep(  ) {
        try {
            // Thread.sleep( timeSlice );
            sleep(timeSlice);
        } catch ( InterruptedException e ) {}
    }

    private void initTid( int maxThreads ) {
        tids = new boolean[maxThreads];
        for ( int i = 0; i < maxThreads; i++ )
            tids[i] = false;
    }

    // A new feature added to p161
    // Search an available thread ID and provide a new thread with this ID
    private int getNewTid( ) {
        for ( int i = 0; i < tids.length; i++ ) {
            int tentative = ( nextId + i ) % tids.length;
            if ( tids[tentative] == false ) {
                tids[tentative] = true;
                nextId = ( tentative + 1 ) % tids.length;
                return tentative;
            }
        }
        return -1;
    }

    // A new feature added to p161
    // Return the thread ID and set the corresponding tids element to be unused
    private boolean returnTid( int tid ) {
        if ( tid >= 0 && tid < tids.length && tids[tid] == true ) {
            tids[tid] = false;
            return true;
        }
        return false;
    }

    // A new feature added to p161
    // Retrieve the current thread's TCB from the queue
    public TCB getMyTcb() {
        Thread myThread = Thread.currentThread(); // Get my thread object
        synchronized(q0) {
            for (int i = 0; i < q0.size(); i++) {
                TCB tcb = (TCB) q0.elementAt(i);
                Thread thread = tcb.getThread( );
                if (thread == myThread) // if this is my TCB, return it
                    return tcb;
            }
        }
        synchronized(q1) {
            for ( int i = 0; i < q1.size( ); i++ ) {
                TCB tcb = (TCB) q1.elementAt( i );
                Thread thread = tcb.getThread( );
                if ( thread == myThread ) // if this is my TCB, return it
                return tcb;
            }
        }
        synchronized(q2) {
            for ( int i = 0; i < q2.size( ); i++ ) {
                TCB tcb = (TCB) q2.elementAt( i );
                Thread thread = tcb.getThread( );
                if ( thread == myThread ) // if this is my TCB, return it
                return tcb;
            }
        }
        return null;
    }

    // A new feature added to p161
    // Return the maximal number of threads to be spawned in the system
    public int getMaxThreads() { return tids.length; }

    // A modified addThread of p161 example
    public TCB addThread(Thread t) {
        TCB parentTcb = getMyTcb( ); // get my TCB and find my TID
        int pid = ( parentTcb != null ) ? parentTcb.getTid( ) : -1;
        int tid = getNewTid( ); // get a new TID
        if ( tid == -1)
            return null;
        TCB tcb = new TCB( t, tid, pid ); // create a new TCB
        // 2) A new thread's TCB is always enqueued into Queue0.
        q0.add( tcb );
        return tcb;
    }

    // A new feature added to p161
    // Removing the TCB of a terminating thread
    public boolean deleteThread() {
        TCB tcb = getMyTcb();
        if (tcb!= null)
            return tcb.setTerminated();
        else
            return false;
    }

    public void sleepThread(int milliseconds) {
        try {
            // sleep( milliseconds );
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {}
    }

    private void q0execute() {
        Thread current = null;
        // 3) MFQS scheduler first executes all threads in Queue0...
        while (q0.size() > 0) {
            TCB currentTCB = (TCB) q0.firstElement();
            if (currentTCB.getTerminated()) {
                q0.remove(currentTCB);
                returnTid( currentTCB.getTid( ) );
                continue;
            }
            current = currentTCB.getThread( );
            if (current != null) {
                if ( current.isAlive() ) {
                    // current.resume();
                // } else {
                    current.start();
                }
            } else {
                continue;
            }
            // 3) whose time quantum Q0 is half of the time quantum in
            // Part 1's round-robin scheduler, (i.e. Q0=500ms).
            sleepThread(timeSlice / 2);  // time = 500ms
            // 4) If a thread in the Queue0 does not complete its execution for
            // Queue0's time quantum, (Q0), then scheduler moves the corresponding
            // TCB to Queue1.
            synchronized (q0) {
                if ( current != null && current.isAlive( ) )
                    current.suspend();
                q0.remove( currentTCB ); // rotate this TCB to the end
                q1.add( currentTCB );
            }
        }
    }

    // 5) If Queue0 is empty, it will execute threads in Queue1
    private void q1execute() {
        Thread current = null;
        while (q1.size() > 0) {
            TCB currentTCB = (TCB) q1.firstElement();
            if (currentTCB.getTerminated()) {
                q1.remove(currentTCB);
                returnTid( currentTCB.getTid( ) );
                continue;
            }
            current = currentTCB.getThread( );
            if ( current != null ) {
                if ( current.isAlive( ) )
                    current.resume();
                else {
                    current.start();
                }
            } else {
                continue;
            }
            // 5) However, in order to react new threads in Queue0 , your
            // MFQS scheduler should execute a thread in queue 1 for only
            // Q0=500ms one-half the Queue1 time quantum and then check if
            // Queue0 has new TCBs pending.
            sleepThread(timeSlice / 2);  // time = 500ms

            // 5) If so, it will execute all threads in Queue0 first. The
            // interrrupted thread will be resumed when all of the Queue0
            // threads are handled.
            if (q1.size() > 0) {
                current.suspend();
                q0execute();
            }
            current.resume();
            // 5) The resumed thread will only be given the remaining part of its time quantum.
            sleepThread(timeSlice / 2);  // time = 500ms

            // 6) If a thread in Queue1 does not complete its execution and
            // was given a full Queue1's time quantum, (i.e., Q1 ), the
            // scheduler then moves the TCB to Queue2 .
            synchronized (q1) {
                if ( current != null && current.isAlive( ) )
                    current.suspend();
                q1.remove( currentTCB ); // rotate this TCB to the end
                q2.add( currentTCB );
            }
        }
    }

    // If a thread in Queue1 does not complete its execution and was given a
    // full Queue1's time quantum, (i.e., Q1 ), the scheduler then moves the
    // TCB to Queue2 .
    private void q2execute() {
        Thread current = null;
        // 7) If both Queue0 and Queue1 are empty, the MFQS schedulers
        // will execute threads in Queue2
        while (q2.size() > 0) {
            TCB currentTCB = (TCB) q2.firstElement();
            if (currentTCB.getTerminated()) {
                q2.remove(currentTCB);
                returnTid( currentTCB.getTid( ) );
                continue;
            }
            current = currentTCB.getThread( );
            if ( current != null ) {
                if ( current.isAlive( ) )
                    current.resume();
                else {
                    current.start();
                }
            } else {
                continue;
            }
            // 7) However, in order to react to threads with higher priority
            // in Queue0 and Queue1, your scheduler should execute a thread in
            // Queue2 for Q0 increments and check if Queue0 and Queue1 have new
            // TCBs. The rest of the behavior is the same as that for Queue1.
            for (int i = 0; i < 4; i++) {
                sleepThread(timeSlice / 2);
                if (q0.size() > 0 || q1.size() >0) {
                    current.suspend();
                    q0execute();
                    q1execute();
                    current.resume();
                }
            }

            // 8) If a thread in Queue2 does not complete its execution
            // for Queue2's time slice, (i.e., Q2 ), the scheduler puts it
            // back to the tail of Queue2.
            synchronized (q2) {
                if ( current != null && current.isAlive( ) )
                    current.suspend();
                q2.remove( currentTCB );
                q2.add( currentTCB );
            }
        }
    }

    public void run( ) {
        while ( true ) {
            try {
                q0execute();
                q1execute();
                q2execute();
            } catch ( NullPointerException e3 ) {};
        }
    }
}
