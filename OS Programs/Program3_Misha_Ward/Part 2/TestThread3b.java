// Misha Ward
// CSS430

import java.util.*;

class TestThread3b extends Thread {

	private byte[] myBlock;								//init a byte array

    public TestThread3b () {}							//constructor

    public void run() {
		myBlock = new byte[512];						//initialize a block
		for (int i = 0; i < 600; i++){
			SysLib.rawwrite(i, myBlock);				//write to disk
			SysLib.rawread(i, myBlock);					//read from disk
		}
		SysLib.exit();
    }
}
