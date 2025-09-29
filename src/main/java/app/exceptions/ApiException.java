package app.exceptions;

public class ApiException extends Exception {
    private final int statusCode;

    public ApiException(int statusCode, String msg){
        super(msg);
        this.statusCode = statusCode;
    }
    public int getStatusCode(){
        return statusCode;
    }
}
