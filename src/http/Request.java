package http;

import java.util.HashMap;

public class Request {
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
            this.url = wholeUrl.indexOf("?") == -1 ? wholeUrl : wholeUrl.substring(0, wholeUrl.indexOf("?"));
            this.version = statusText[2];

            String httpBody = "";

            int headerOrBody = 0;
            for(int i=1; i<fullText.length; i++){
                if(headerOrBody == 0) {
                    int colonLocation = fullText[i].indexOf(":");
                    if (fullText[i].equals("\r\n")) {
                        headerOrBody = 1;
                        continue;
                    }
                    headerMap.put(fullText[i].substring(0, colonLocation).trim(), fullText[i].substring(colonLocation+1).trim());
                } else {
                    httpBody = fullText[i];
                }
            }

            if(this.method.equals("GET") && wholeUrl.indexOf("?") != -1){
                paramMap = parseParameter(wholeUrl.substring(wholeUrl.indexOf("?") + 1));
            } else if(this.method.equals("POST") && !httpBody.isEmpty()) {
                paramMap = parseParameter(httpBody);
            }

            stringStatus = "good";
        } catch(Exception e){
            stringStatus = "bad";
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

    public HashMap<String, String> getHeaderMap() {
        return headerMap;
    }

    public HashMap<String, String> getParamMap() {
        return paramMap;
    }
}