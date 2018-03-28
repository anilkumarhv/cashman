package in.anil.services.exception;

public class ATMInsufficientDenominationsException extends RuntimeException {

    public ATMInsufficientDenominationsException(final String message) {
        super(message);
    }

}
