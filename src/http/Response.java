package http;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class Response {
    public static final int OK = 200;
    public static final int BAD_REQUEST = 400;
    public static final int NOT_FOUND = 404;
    public static final int INTERNAL_SERVER_ERROR = 500;

    private String version;
    private int status;
    private String statusString;

    private HashMap<String, String> headerMap;

    private String headerString;
    private byte[] body;

    public Response(int httpStatus, String contentType, byte[] body){
        headerMap = new HashMap<>();
        setStatus(httpStatus, contentType);
        headerString = createHeaderString();
        this.body = body;
    }

    private void setStatus(int httpStatus, String contentType){
        switch(httpStatus){
            case OK:
                this.version = "HTTP/1.1";
                this.status = OK;
                this.statusString = "OK";
                this.headerMap.put("Content-Type", contentType);
                break;
            case BAD_REQUEST:
                this.version = "HTTP/1.1";
                this.status = BAD_REQUEST;
                this.statusString = "Bad Request";
                this.headerMap.put("Content-Type", contentType);
                break;
            case NOT_FOUND:
                this.version = "HTTP/1.1";
                this.status = NOT_FOUND;
                this.statusString = "Not Found";
                this.headerMap.put("Content-Type", contentType);
                break;
            case INTERNAL_SERVER_ERROR:
                this.version = "HTTP/1.1";
                this.status = INTERNAL_SERVER_ERROR;
                this.statusString = "Internal Server Error";
                this.headerMap.put("Content-Type", contentType);
                break;
        }
    }

    private String createHeaderString(){
        StringBuilder header = new StringBuilder();
        header.append(this.version + " " + this.status + " " + this.statusString + "\r\n");
        for(String key : headerMap.keySet()){
            header.append(key + ": " + headerMap.get(key) + "\r\n");
        }
        header.append("\r\n");

        return header.toString();
    }

    public void send(OutputStream outputStream) throws Exception {
        outputStream.write(this.headerString.getBytes(StandardCharsets.UTF_8));
        outputStream.write(body);
    }
}
