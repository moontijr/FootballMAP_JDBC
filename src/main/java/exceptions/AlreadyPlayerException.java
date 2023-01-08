package exceptions;

public class AlreadyPlayerException  extends Exception{
    public AlreadyPlayerException(){
        super("There is already a player with this ID. Please choose another ID and press anything to get back!");
    }
}

