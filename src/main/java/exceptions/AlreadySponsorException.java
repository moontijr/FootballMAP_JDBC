package exceptions;

public class AlreadySponsorException extends Exception{
    public AlreadySponsorException()
    {
        super("There is already a sponsor with this abbreviation! Please choose another one. Thank you!");
    }
}
