import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DateServerPool implements Runnable {
    Socket socket;
    DateServerPool(Socket csocket) {
        this.socket = csocket;
    }
    public static void main(String args[]) throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        ServerSocket sock = new ServerSocket(6013);
        System.out.println("Listening");

        while (true) {
            Socket client = sock.accept();
            System.out.println("Connected");
            Future future = executorService.submit(() -> (new DateServer(client)).run());

            // some operations
            //String result =
            future.get();
        }
    }
    public void run() {
        try {
            PrintStream pout = new PrintStream(socket.getOutputStream());
            pout.println(new java.util.Date().toString());
            pout.close();
            socket.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
