package validation;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ControllerAdvice
public class ValidationAdvice {

    @ResponseBody
    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ValidationErrors handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception) {

        List<FieldError> errors = exception.getBindingResult().getFieldErrors();

        System.out.println(errors);

        ValidationErrors ve = new ValidationErrors();

        for (FieldError error : errors) {
            ve.addError(error);
        }

        // create ValidationErrors object (container for individual errors).

        // add errors from errors list to ValidationErrors object.

        // return created object. Spring converts it to Json and returns it to the client.

        return ve;
    }
}