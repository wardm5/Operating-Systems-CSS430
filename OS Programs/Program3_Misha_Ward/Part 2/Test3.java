// Misha Ward
// CSS430

import java.util.Date;
import java.util.*;
class Test3 extends Thread {
	private int input;  // input number of thread pairs
    private long start; // start time (prior to processing)
    private long end;   // end time (after processing)
	public Test3(String[] args){
		input = Integer.parseInt(args[0]);  // parse integer from argument submitted
	}

    public void run() {
		start = new Date().getTime();				//get time prior to processing
		String[] argA = SysLib.stringToArgs( "TestThread3a" );  // convert string file name to "argument"
		String[] argB = SysLib.stringToArgs( "TestThread3b" );  // convert string file name to "argument"

		for(int i = 0; i < input; i++) {
			SysLib.exec(argA);  // execture TestThread3a
		}
		for(int i = 0; i < input; i++) {
			SysLib.exec(argB);  // execute TestThread3b
		}
		for (int i = 0; i < input; i++ ){
		   	SysLib.join( );	// hvae the CPU wait for the work being done...
		}
		for (int i = 0; i < input; i++ ){
			SysLib.join( );	 // have the computer wait for READ/WRITE to complete
		}
		end = new Date().getTime( );  // get time at end of processing

		SysLib.cout("Total processing time = " + (end - start) + " milliseconds.\n");  // print out total time
		SysLib.exit();  // exit method
    }
}
