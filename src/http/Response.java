package http;

public class Response {
    private String statusString;
    private String htmlFile;

    private String httpResponse;

    public Response(int httpStatus, String url){
        StringBuilder responseBuilder = new StringBuilder();
    }

    private void setHeader(int httpStatus, StringBuilder responseBuilder){
        switch(httpStatus){
            case 200:
                responseBuilder.append("HTTP/1.1 200 OK\r\n");
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
}
