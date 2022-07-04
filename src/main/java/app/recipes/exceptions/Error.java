package app.recipes.exceptions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Error {
  private final String message;
  private final ErrorCode errorCode;

  @JsonCreator
  public Error(
      @JsonProperty("message") String message, @JsonProperty("errorCode") ErrorCode errorCode) {
    this.message = message;
    this.errorCode = errorCode;
  }

  public String getMessage() {
    return message;
  }

  public ErrorCode getErrorCode() {
    return errorCode;
  }
}
