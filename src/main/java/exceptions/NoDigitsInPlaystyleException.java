package exceptions;

/***
 * Checks if a string from input contains digits
 * */
public class NoDigitsInPlaystyleException extends Exception {
    public NoDigitsInPlaystyleException(){
        super("Please provide a valid playstyle, without any other characters. Press anything to go back");
    }
}
