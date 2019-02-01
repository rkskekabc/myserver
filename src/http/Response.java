package http;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class Response {
    private String httpHeader;
    private byte[] body;

    public Response(int httpStatus, String contentType, byte[] body){
        StringBuilder responseBuilder = new StringBuilder();
        setHeader(httpStatus, contentType, responseBuilder);
        httpHeader = responseBuilder.toString();
        this.body = body;
    }

    private void setHeader(int httpStatus, String contentType, StringBuilder responseBuilder){
        switch(httpStatus){
            case 200:
                responseBuilder.append("HTTP/1.1 200 OK\r\n");
                responseBuilder.append("Content-Type: " + contentType + "\r\n");
                break;
            case 400:
                responseBuilder.append("HTTP/1.1 400 Bad Request\r\n");
                responseBuilder.append("Content-Type: text/html; charset=UTF-8\r\n");
                break;
            case 404:
                responseBuilder.append("HTTP/1.1 404 Not Found\r\n");
                responseBuilder.append("Content-Type: text/html; charset=UTF-8\r\n");
                break;
            case 500:
                responseBuilder.append("HTTP/1.1 500 Internal Server Error\r\n");
                responseBuilder.append("Content-Type: text/html; charset=UTF-8\r\n");
                break;
        }
        responseBuilder.append("\r\n");
    }

    public void send(OutputStream outputStream) throws Exception {
        outputStream.write(this.httpHeader.getBytes(StandardCharsets.UTF_8));
        outputStream.write(body);
    }

    public String getHttpHeader() {
        return httpHeader;
    }

    public byte[] getBody() {
        return body;
    }
}
