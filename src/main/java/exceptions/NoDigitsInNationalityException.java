package exceptions;

/***
 * Checks if a string from input contains digits
 * */
public class NoDigitsInNationalityException extends Exception{
    public NoDigitsInNationalityException(){
        super("Please provide a valid nationality, without any other characters. Press anything to go back");
    }
}
