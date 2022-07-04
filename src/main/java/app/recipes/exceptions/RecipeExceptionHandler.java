package app.recipes.exceptions;

import app.recipes.exceptions.custom.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class RecipeExceptionHandler {
  private static final Logger LOGGER = LoggerFactory.getLogger(RecipeExceptionHandler.class);

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(NotFoundException.class)
  ResponseEntity<Error> handleNotFoundException(NotFoundException notFoundException) {
    LOGGER.info(
        "Returning error response: [message: {}, status: {} ]",
        notFoundException.getErrorMessage(),
        notFoundException.getErrorCode());

    Error error = new Error(notFoundException.getErrorMessage(), notFoundException.getErrorCode());
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }
}
