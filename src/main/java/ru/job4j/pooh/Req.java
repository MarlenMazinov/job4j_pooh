package ru.job4j.pooh;

public class Req {

    private final String httpRequestType;
    private final String poohMode;
    private final String sourceName;
    private final String param;

    public Req(String httpRequestType, String poohMode, String sourceName, String param) {
        this.httpRequestType = httpRequestType;
        this.poohMode = poohMode;
        this.sourceName = sourceName;
        this.param = param;
    }

    public static Req of(String content) {
        String ls = System.lineSeparator();
        String[] contentToArray = content.split(ls);
        String[] firstLineToArray = contentToArray[0].split("/");
        String reqType = firstLineToArray[0].trim();
        String mode = "queue".equals(firstLineToArray[1]) ? "queue" : "topic";
        String source = firstLineToArray[2].split(" ")[0];
        String parameter;
        if ("GET".equals(reqType)) {
            parameter = firstLineToArray.length > 4 ? firstLineToArray[3].split(" ")[0] : "";
        } else {
            parameter = contentToArray[contentToArray.length - 1];
        }
        return new Req(reqType, mode, source, parameter);
    }

    public String httpRequestType() {
        return httpRequestType;
    }

    public String getPoohMode() {
        return poohMode;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getParam() {
        return param;
    }
}