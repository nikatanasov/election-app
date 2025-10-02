package app.exception;

public class PartyCreationDeniedException extends RuntimeException{

    public PartyCreationDeniedException(String message){
        super(message);
    }

    public PartyCreationDeniedException() {
    }
}
