package exceptions;

public class AlreadyCoachException extends Exception{
    public AlreadyCoachException (){
        super("There is already a coach with this ID! Please choose another ID. Thank you!");
    }
}
