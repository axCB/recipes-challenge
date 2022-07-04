package app.recipes.exceptions.custom;

import app.recipes.exceptions.ErrorCode;

public class NotFoundException extends RecipeException {
  public NotFoundException(String errorMessage) {
    super(errorMessage, ErrorCode.NOT_FOUND);
  }
}
