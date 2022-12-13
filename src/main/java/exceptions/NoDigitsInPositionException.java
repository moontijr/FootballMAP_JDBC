package exceptions;

/***
 * Checks if a string from input contains digits
 * */
public class NoDigitsInPositionException extends Exception{
    public NoDigitsInPositionException(){
        super("Please provide a valid position, without any other characters. Press anything to go back");
    }
}
