package exceptions;

public class ImproperIntegerException extends Exception{
    public ImproperIntegerException(){
        super("Please provide a positive Integer. Any strings or negative numbers are not accepted! Press anything to go back");
    }

    public ImproperIntegerException(String message){
        super(message);
    }
}
