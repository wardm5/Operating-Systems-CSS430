// Misha Ward
// CSS430

import java.util.*;
import java.lang.*;

class TestThread3a extends Thread {

    public TestThread3a () {}

    public void run() {
    	for(int j = 0; j < 10000; j++){
    		for(int i = 0; i < 10000; i++){
    			Math.log(Math.sin(Math.sqrt(Math.sinh(39148.493))));
    		}
    	}
		SysLib.exit( );
    }
}
