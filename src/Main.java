import webserver.ServerThread;

import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String args[]) throws Exception{
        ServerSocket serverSocket = new ServerSocket(7777);
        System.out.println("Server start!");
        while(true) {
            System.out.println("Listen..");
            Socket socket = serverSocket.accept();
            System.out.println("Connect!");

            ServerThread newThread = new ServerThread(socket);
            newThread.start();
        }
    }
}
