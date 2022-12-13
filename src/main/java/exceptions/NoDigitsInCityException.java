package exceptions;

/***
 * Checks if a string from input contains digits
 * */
public class NoDigitsInCityException extends Exception {
    public NoDigitsInCityException(){
        super("Please provide a valid town, without any other characters. Press anything to go back");
    }
}