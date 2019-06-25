import java.util.*;

/*
Test Cases:
PingPong abc 100  ; PingPong xyz 50  ; PingPong 123 100
PingPong abc 50   ; PingPong xyz 100 & PingPong 123 100
PingPong abc 100  & PingPong xyz 100 ; PingPong 123 50
PingPong abc 50   & PingPong xyz 50  & PingPong 123 100
*/

		/************************************************************************
		*                                                                       *
		*   Code by: Misha Ward                                                 *
		*   CSS 430: Program 1 - Practicing with ThreadOS                       *
		*   This code is ment to replicate the shell for a kernel: shell[1]%    *
		*   The code below prints out the shell script and recieves input       *
		*   from the user which could be concurrent by using '&' or sequential  *
		*   using ';'. When the user enters 'exit', the program will close.     *
		*   NOTE: code was written in Atom, formatting might not transfer.      *
		*                                                                       *
		************************************************************************/

public class Shell extends Thread{
		Set<Integer> set;
    public Shell() {   // constructor
				set = new HashSet<>(); // initlize set
		}

    public void run() {
        int num = 1;  // first line, start at 1
        while (true) {
            System.out.print("shell[" + num + "]%");  // print the shell script with line number
            StringBuffer buff = new StringBuffer();   // create a new StringBuffer
            SysLib.cin(buff);  	// input from user
						num++;  						// increases the line number
            String line = new String(buff);  // initlize string that contains user input
            if (line.contains("exit")) {  // checks if that line contains "exit"
              	break;  				// break from function if "exit" exists
            }
						String[] arr = line.split (";");   // break string apart based on ;
						for (int i = 0; i < arr.length; i++) {  // for each of the seperate commands...
								runCommands(arr[i]);  // run the command helper function (runCommands)
						}
        }
				SysLib.cout("You have exited the shell, goodbye! \n");  	// provide exit message
				SysLib.exit();																						// exit SysLib
    }

		public void runCommands (String command) {
				String[] conArr = command.split("&");				// split the command into an array of
																										// conncurent commands (based on '&')
				for (int i = 0; i < conArr.length; i++) {   // for each command in the concurrent array...
						String temp = conArr[i];             // set temp string as the command
						int id = SysLib.exec(SysLib.stringToArgs(temp));  // have the kernel execute the command
																															// creates new thread...
						set.add(id);  // add the thread id to the set
				}

				while (!set.isEmpty()) {
						int id = SysLib.join();  //  sets id to the thead id
						if (id < 0) {  			// if the id is less than 0...
								set.clear();		// clear the set
								return;					// return from method
						}
						if (set.contains(id)) {  // if the set contains the thread id...
								set.remove(id);      // remove the thread
						}
				}
				return;  // exists helper method
		}
}
