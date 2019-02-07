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

    public final static String ERROR400 = "/errorPage/error400";
    public final static String ERROR404 = "/errorPage/error404";
    public final static String ERROR500 = "/errorPage/error500";

    public final static String INDEX_PAGE = "/index";

    public static Response createResponse(String httpString){
        Request req = new Request(httpString);

        if(req.getStringStatus().equals(Request.BAD_REQUEST)){
            byte[] body = getFile(fullHtmlPath(ERROR400));
            return new Response(Response.BAD_REQUEST, "text/html", body);
        } else if (Files.notExists(Paths.get(fullHtmlPath(req.getUrl()))) && Files.notExists(Paths.get(fullFilePath(req.getUrl())))) {
            byte[] body = getFile(fullHtmlPath(ERROR404));
            return new Response(Response.NOT_FOUND, "text/html", body);
        } else if(req.getUrl().endsWith(".jpg")) {
            byte[] body = getFile(fullFilePath(req.getUrl()));
            return new Response(Response.OK, "image/jpg", body);
        } else if (req.getUrl().equals("/")){
            byte[] body = getFile(fullHtmlPath(INDEX_PAGE));
            return new Response(Response.OK, "text/html", body);
        } else {
            byte[] body = getFile(fullHtmlPath(req.getUrl()));
            return new Response(Response.OK, "text/html", changeHtml(req, new String(body, StandardCharsets.UTF_8)).getBytes(StandardCharsets.UTF_8));
        }
    }

    public static Response createServerErrorResponse(){
        byte[] body = getFile(fullHtmlPath(ERROR500));
        return new Response(Response.INTERNAL_SERVER_ERROR, "text/html", body);
    }

    private static String fullHtmlPath(String url){
        return HTMLPATH + url + FOOTER;
    }

    private static String fullFilePath(String fileName){
        return FILEPATH + fileName;
    }

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
