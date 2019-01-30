import http.Request;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String args[]) throws Exception{
            ServerSocket serverSocket = new ServerSocket(7777);
            while(true) {
                System.out.println("Listen");
                Socket socket = serverSocket.accept();
                System.out.println("Connect");

                InputStream socketInputStream = socket.getInputStream();
                byte[] socketInputByte = new byte[2048];
                int inputLen = socketInputStream.read(socketInputByte);
                String httpString = new String(socketInputByte, 0, inputLen,"UTF-8");
                System.out.println(httpString);

                System.out.println("---------------------------");
                Request req = new Request(httpString);
                System.out.println("Method : " + req.getMethod());
                System.out.println("Url : " + req.getUrl());
                System.out.println("Version : " + req.getVersion());
                System.out.println("Status : " + req.getStringStatus());
                System.out.println("nameParam : " + req.getParamMap().get("name"));
                System.out.println("Host : " + req.getHeaderMap().get("Host"));
                System.out.println("Agent : " + req.getHeaderMap().get("User-Agent"));
                System.out.println("---------------------------");

                OutputStream socketOutputStream = socket.getOutputStream();

                socketOutputStream.write("HTTP/1.1 200 OK\r\n".getBytes("UTF-8"));
                socketOutputStream.write("Content-Type: text/html; charset=UTF-8\r\n".getBytes("UTF-8"));
                socketOutputStream.write("Server: MyServer\r\n".getBytes("UTF-8"));

                socketOutputStream.write("\r\n".getBytes("UTF-8"));

                socketOutputStream.write("<html>".getBytes("UTF-8"));
                socketOutputStream.write("<head>".getBytes("UTF-8"));
                socketOutputStream.write("<title>Title</title>".getBytes("UTF-8"));
                socketOutputStream.write("</head>".getBytes("UTF-8"));
                socketOutputStream.write("<body>".getBytes("UTF-8"));
                socketOutputStream.write("<h1>hello</h1>".getBytes("UTF-8"));
                socketOutputStream.write("</body>".getBytes("UTF-8"));
                socketOutputStream.write("</html>".getBytes("UTF-8"));
                socketOutputStream.flush();

                socketOutputStream.close();
                socketInputStream.close();
                socket.close();
            }
    }
}
