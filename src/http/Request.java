package http;

import java.util.HashMap;

public class Request {
    public static final String OK = "OK";
    public static final String BAD_REQUEST = "BAD REQUEST";

    private String stringStatus;

    private String method;
    private String url;
    private String version;

    private HashMap<String, String> headerMap;
    private HashMap<String, String> paramMap;

    public Request(String httpString){
        headerMap = new HashMap<>();
        paramMap = new HashMap<>();
        requestParse(httpString);
    }

    private void requestParse(String httpString){
        try{
            String[] fullText = httpString.split("\r\n");
            String[] statusText = fullText[0].split(" ");

            String wholeUrl = statusText[1];

            this.method = statusText[0];
            this.url = wholeUrl.contains("?") ? wholeUrl.substring(0, wholeUrl.indexOf("?")) : wholeUrl;
            this.version = statusText[2];

            String httpBody = "";

            int headerOrBody = 0;
            for(int i=1; i<fullText.length; i++){
                if(headerOrBody == 0) {
                    int colonLocation = fullText[i].indexOf(":");
                    if (fullText[i].equals("")) {
                        headerOrBody = 1;
                    } else {
                        headerMap.put(fullText[i].substring(0, colonLocation).trim(), fullText[i].substring(colonLocation+1).trim());
                    }
                } else {
                    httpBody = fullText[i];
                }
            }

            if(wholeUrl.contains("?")){
                paramMap.putAll(parseParameter(wholeUrl.substring(wholeUrl.indexOf("?") + 1)));
            } else if(!httpBody.isEmpty()) {
                paramMap.putAll(parseParameter(httpBody));
            }

            stringStatus = OK;
        } catch(Exception e){
            e.printStackTrace();
            stringStatus = BAD_REQUEST;
        }
    }

    private HashMap<String, String> parseParameter(String param){
        HashMap<String, String> returnMap = new HashMap<>();

        String[] params = param.split("&");
        for(String item : params){
            String[] itemSplit = item.split("=");
            returnMap.put(itemSplit[0], itemSplit[1]);
        }

        return returnMap;
    }

    public String getStringStatus() {
        return stringStatus;
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getVersion() {
        return version;
    }

    public String getHeaderMap(String key) {
        return headerMap.get(key);
    }

    public String getParamMap(String key) {
        return paramMap.get(key);
    }
}
