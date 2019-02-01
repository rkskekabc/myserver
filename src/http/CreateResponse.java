package http;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateResponse {
    public final static String HTMLPATH = "./myapp";
    public final static String FOOTER = ".html";

    public final static String FILEPATH = "./examplefiles";

    public static Response createResponse(String httpString){
        Request req = new Request(httpString);

        if (req.getUrl().equals("/")){
            byte[] html = getFile(fullHtmlPath("/index"));
            return new Response(200, "text/html", html);
        } else if(req.getUrl().equals("/downloadDesert")) {
            byte[] file = getFile(fullFilePath("/Desert.jpg"));
            return new Response(200, "image/jpeg", file);
        } else if (Files.notExists(Paths.get(fullHtmlPath(req.getUrl())))) {
            byte[] html = getFile(fullHtmlPath("/errorPage/error404"));
            return new Response(404, "text/html", html);
        } else {
            byte[] html = getFile(fullHtmlPath(req.getUrl()));
            return new Response(200, "text/html", changeHtml(req, new String(html, StandardCharsets.UTF_8)).getBytes(StandardCharsets.UTF_8));
        }
    }

    private static String fullHtmlPath(String url){
        return HTMLPATH + url + FOOTER;
    }

    private static String fullFilePath(String fileName){
        return FILEPATH + fileName;
    }

//    public static String getHtml(String htmlName){
//        try {
//            InputStream htmlInputStream = new FileInputStream(HTMLPATH + htmlName + FOOTER);
//            BufferedInputStream bufferedHtmlInputStream = new BufferedInputStream(htmlInputStream, 1024);
//            byte[] htmlByte = new byte[4096];
//            int htmlLen = bufferedHtmlInputStream.read(htmlByte);
//
//            String returnHtml = new String(htmlByte, 0, htmlLen, StandardCharsets.UTF_8);
//            bufferedHtmlInputStream.close();
//            htmlInputStream.close();
//
//            return returnHtml;
//        } catch (Exception e){
//            e.printStackTrace();
//            return "error";
//        }
//    }

    public static byte[] getFile(String fileName){
        try {
            File file = new File(fileName);
            long fileSize = file.length();

            InputStream fileInputStream = new FileInputStream(fileName);
            BufferedInputStream bufferedFileInputStream = new BufferedInputStream(fileInputStream, 1024);
            byte[] fileByte = new byte[(int)fileSize];

            bufferedFileInputStream.read(fileByte);

            bufferedFileInputStream.close();
            fileInputStream.close();

            return fileByte;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private static String changeHtml(Request req, String htmlString){
        Pattern myPattern = Pattern.compile("\\{- *[a-z0-9]+ *-}");
        Matcher myMatcher = myPattern.matcher(htmlString);

        String returnString = htmlString;
        while(myMatcher.find()){
            String key = myMatcher.group().replace("{-", "").replace("-}", "").trim();
            returnString = returnString.replace(myMatcher.group(), req.getParamMap(key) == null ? "" : req.getParamMap(key));
        }
        return returnString;
    }
}
