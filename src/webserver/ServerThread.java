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
        try {
            InputStream socketInputStream = socket.getInputStream();
            OutputStream socketOutputStream = socket.getOutputStream();
            byte[] inputBytes = new byte[2048];
//            while (true) {
                int inputLen = socketInputStream.read(inputBytes);
                if(inputLen == -1){     // 자꾸 어디선가 요청 오는데 대체 어디냐
                    socketOutputStream.write("error".getBytes(StandardCharsets.UTF_8));
                    socketOutputStream.flush();
//                    break;
                } else {
                    Response response = CreateResponse.createResponse(new String(inputBytes, 0, inputLen, StandardCharsets.UTF_8));
                    response.send(socketOutputStream);
                    socketOutputStream.flush();
//                    break;      // 클라이언트 종료 신호 ?
                }
//            }

            socketInputStream.close();
            socketOutputStream.close();
        } catch(Exception e){
            try {
                OutputStream socketOutputStream = socket.getOutputStream();
                Response response = new Response(500, "text/html", CreateResponse.getFile(CreateResponse.HTMLPATH + "/errorPage/error500" + CreateResponse.FOOTER));
                response.send(socketOutputStream);
                socketOutputStream.flush();

                socketOutputStream.close();
            } catch(Exception e2){
                e.printStackTrace();
            }
            e.printStackTrace();
            try {
                socket.close();
            } catch (Exception e3) {
                e.printStackTrace();
            }
        }
    }
}
