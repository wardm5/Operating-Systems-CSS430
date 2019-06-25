import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class DateServer implements Runnable {
    Socket socket;
    DateServer(Socket csocket) {
        this.socket = csocket;
    }
    public static void main(String args[]) throws Exception {
        ServerSocket sock = new ServerSocket(6013);
        System.out.println("Listening");

        while (true) {
            Socket client = sock.accept();
            System.out.println("Connected");
            new Thread(new DateServer(client)).start();
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
