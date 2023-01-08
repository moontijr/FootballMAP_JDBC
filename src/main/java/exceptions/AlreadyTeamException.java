package exceptions;

public class AlreadyTeamException extends Exception{
    public AlreadyTeamException()
    {
        super("There is already a team with this abbreviation! Please choose another one. Thank you!");
    }
}
