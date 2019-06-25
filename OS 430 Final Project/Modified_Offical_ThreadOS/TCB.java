    /****************************************************
    *               Final Project: OS                   *
    *      Diego Guzman, Nathan Fixx, Misha Ward        *
    *                                                   *
    ****************************************************/
public class TCB {
    private int tid = 0;
    private int pid = 0;
    private Thread thread = null;
    private boolean terminate = false;
    public FileTableEntry[] ftEnt = null;
    public static int MAX_ENTRY = 32;
    // TCB constructor
    public TCB( Thread nThread, int myTid, int parentTid ) {
        thread = nThread;
        tid = myTid;
        pid = parentTid;
        terminate = false;
        ftEnt = new FileTableEntry[MAX_ENTRY];
    }
    // gets the current thread
    public synchronized Thread getThread(){
        return thread;
    }
    // gets Thread ID
    public synchronized int getTid(){
        return tid;
    }
    // gets Process ID
    public synchronized int getPid(){
        return pid;
    }
    // gets the terminate boolean
    public synchronized boolean getTerminated(){
        return terminate;
    }

    // gets the FD
    public synchronized int getFd(FileTableEntry entry){
        if (entry == null)
            return -1;
        for (int i = 3; i < MAX_ENTRY; i++){
            if (ftEnt[i] == null){
                ftEnt[i] = entry;
                return i;
            }
        }
        return -1;
    }
    // sets the boolean for terminate
    public synchronized boolean setTerminated(){
        terminate = true;
        return terminate;
    }
    // returns the FD
    public synchronized FileTableEntry returnFd(int fdNum){
        if (fdNum >= 3 && fdNum < MAX_ENTRY){
            FileTableEntry fte = ftEnt[fdNum];
            ftEnt[fdNum] = null;
            return fte;
        }
        return null;
    }
    // gets the file table entry
    public synchronized FileTableEntry getFtEnt(int fdNum){
        if (fdNum >= 3 && fdNum < MAX_ENTRY){
            return ftEnt[fdNum];
        }
        return null;
    }
}
