package app.recipes.converter;

import app.recipes.persistence.Recipe;
import app.recipes.rest.RecipeRequest;
import app.recipes.rest.RecipeResponse;
import app.recipes.rest.RecipeType;

public class RecipeConverter {
  private RecipeConverter() {}

  public static Recipe toRecipe(RecipeRequest request) {
    Recipe recipe = new Recipe();

    recipe.setName(request.getName());
    recipe.setType(Recipe.RecipeType.valueOf(request.getType().name()));
    recipe.setServings(request.getServings());
    recipe.setIngredients(request.getIngredients());
    recipe.setInstructions(request.getInstructions());
    return recipe;
  }

  public static RecipeResponse toRecipeResponse(Recipe recipe) {
    return new RecipeResponse(
        recipe.getId(),
        recipe.getName(),
        RecipeType.valueOf(recipe.getType().name()),
        recipe.getServings(),
        recipe.getIngredients(),
        recipe.getInstructions());
  }
}
