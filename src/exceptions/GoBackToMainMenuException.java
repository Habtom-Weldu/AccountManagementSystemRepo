package exceptions;

public class GoBackToMainMenuException extends Exception {
    // This class will be used whenever the user enters 'back' text
    public GoBackToMainMenuException() {
        super("User chose to go back.");
    }
}
