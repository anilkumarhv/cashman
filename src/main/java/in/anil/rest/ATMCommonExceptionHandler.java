package in.anil.rest;

import in.anil.services.dto.ATMDto;
import in.anil.services.exception.ATMInsufficientDenominationsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class ATMCommonExceptionHandler {

    @ExceptionHandler({ATMInsufficientDenominationsException.class})
    public ResponseEntity<ATMDto> handleAllConflicts(final Exception ex) {
        ATMDto error = new ATMDto();
        error.setMessage(ex.getLocalizedMessage());
        return new ResponseEntity<>(error, HttpStatus.PRECONDITION_FAILED);
    }

}
