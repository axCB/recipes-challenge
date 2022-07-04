package app.recipes.exceptions.custom;

import app.recipes.exceptions.ErrorCode;

public class RecipeException extends RuntimeException {
  private final String errorMessage;
  private final ErrorCode errorCode;

  public RecipeException(String errorMessage, ErrorCode errorCode) {
    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public ErrorCode getErrorCode() {
    return errorCode;
  }
}
