package http;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateResponse {
    public final static String HTMLPATH = "./myapp";
    public final static String FOOTER = ".html";

    public final static String FILEPATH = "./examplefiles";

    public static Response createResponse(String httpString){
        Request req = new Request(httpString);

        if (req.getUrl().equals("/")){
            String html = getHtml("/index");
            return new Response(200, "text/html", html);
        } else if(req.getUrl().equals("/downloadDesert")) {
            String file = getFile("/Desert.jpg");
            return new Response(200, "image/jpeg", file);
        } else if (Files.notExists(Paths.get(HTMLPATH + req.getUrl() + FOOTER))) {
            String html = getHtml("/errorPage/error404");
            return new Response(404, "text/html", html);
        } else {
            String html = getHtml(req.getUrl());
            return new Response(200, "text/html", changeHtml(req, html));
        }
    }

    public static String getHtml(String htmlName){
        try {
            InputStream htmlInputStream = new FileInputStream(HTMLPATH + htmlName + FOOTER);
            BufferedInputStream bufferedHtmlInputStream = new BufferedInputStream(htmlInputStream, 1024);
            byte[] htmlByte = new byte[4096];
            int htmlLen = bufferedHtmlInputStream.read(htmlByte);

            String returnHtml = new String(htmlByte, 0, htmlLen, StandardCharsets.UTF_8);
            bufferedHtmlInputStream.close();
            htmlInputStream.close();

            return returnHtml;
        } catch (Exception e){
            e.printStackTrace();
            return "error";
        }
    }

    private static String changeHtml(Request req, String htmlString){
        System.out.println("change start");
        Pattern myPattern = Pattern.compile("\\{- *[a-z0-9]+ *-\\}");
        Matcher myMatcher = myPattern.matcher(htmlString);

        String returnString = htmlString;
        while(myMatcher.find()){
            String key = myMatcher.group().replace("{-", "").replace("-}", "").trim();
            returnString = returnString.replace(myMatcher.group(), req.getParamMap(key) == null ? "" : req.getParamMap(key));
        }
        System.out.println("change end");
        return returnString;
    }

    public static String getFile(String fileName){
        try {
            InputStream fileInputStream = new FileInputStream(FILEPATH + fileName);
            BufferedInputStream bufferedFileInputStream = new BufferedInputStream(fileInputStream, 1024);
            byte[] fileByte = new byte[4096];
            int fileLen = bufferedFileInputStream.read(fileByte);

            byte[] fileEncode = Base64.getEncoder().encode(fileByte);

            String returnFile = new String(fileEncode);
            bufferedFileInputStream.close();
            fileInputStream.close();

            return returnFile;
        } catch (Exception e){
            e.printStackTrace();
            return "error";
        }
    }
}
