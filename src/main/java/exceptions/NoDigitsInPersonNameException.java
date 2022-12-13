package exceptions;

/***
 * Checks if a string from input contains digits
 * */
public class NoDigitsInPersonNameException extends Exception{
    public NoDigitsInPersonNameException(){
        super("Please provide a valid name, without any other characters. Press anything to go back");
    }
}
