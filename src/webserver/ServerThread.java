package webserver;

import http.CreateResponse;
import http.Response;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ServerThread extends Thread {
    private Socket socket;

    public ServerThread(Socket socket){
        this.socket = socket;
    }


    @Override
    public void run() {
        try(InputStream socketInputStream = socket.getInputStream();
            OutputStream socketOutputStream = socket.getOutputStream()) {

            byte[] inputBytes = new byte[2048];

            int inputLen = socketInputStream.read(inputBytes);

            if(inputLen == -1){
                socketOutputStream.write("error".getBytes(StandardCharsets.UTF_8));
                socketOutputStream.flush();
            } else {
                Response response = CreateResponse.createResponse(new String(inputBytes, 0, inputLen, StandardCharsets.UTF_8));
                response.send(socketOutputStream);
                socketOutputStream.flush();
            }

        } catch(Exception e){
            try(OutputStream socketOutputStream = socket.getOutputStream()) {
                Response response = CreateResponse.createServerErrorResponse();
                response.send(socketOutputStream);
                socketOutputStream.flush();
                e.printStackTrace();
            } catch(Exception e2){
                e2.printStackTrace();
            }

            try {
                socket.close();
            } catch (Exception e3) {
                e.printStackTrace();
            }
        }
    }
}
