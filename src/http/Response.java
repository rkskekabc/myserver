package http;

public class Response {
    private String httpResponse;

    public Response(int httpStatus, String contentType, String body){
        StringBuilder responseBuilder = new StringBuilder();
        setHeader(httpStatus, contentType, responseBuilder);
        responseBuilder.append(body);
        httpResponse = responseBuilder.toString();
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

    public String getHttpResponse() {
        return httpResponse;
    }
}
