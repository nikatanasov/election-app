package app.exception;

public class PartyDeleteDeniedException extends RuntimeException{
    public PartyDeleteDeniedException(String message) {
        super(message);
    }

    public PartyDeleteDeniedException() {
    }
}
