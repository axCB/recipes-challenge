package app.recipes;

import app.recipes.persistence.Recipe;
import app.recipes.rest.*;

import java.util.stream.Collectors;

/**
 * Class for converting between RecipeRequest and Recipe document and Recipe document and
 * RecipeResponse.
 */
public class RecipeConverter {
  private RecipeConverter() {}

  public static Recipe toRecipe(RecipeRequest request) {
    Recipe recipe = new Recipe();

    recipe.setName(request.getName());
    recipe.setType(Recipe.RecipeType.valueOf(request.getType().name()));
    recipe.setServings(request.getServings());
    recipe.setIngredients(
        request.getIngredients().stream()
            .map(
                i ->
                    new Recipe.MeasuredIngredient(
                        i.getName(),
                        Recipe.MeasurementUnitType.valueOf(i.getUnitType().name()),
                        i.getValue()))
            .collect(Collectors.toList()));
    recipe.setInstructions(request.getInstructions());
    return recipe;
  }

  public static RecipeResponse toRecipeResponse(Recipe recipe) {
    return new RecipeResponse(
        recipe.getId(),
        recipe.getName(),
        RecipeType.valueOf(recipe.getType().name()),
        recipe.getServings(),
        recipe.getIngredients().stream()
            .map(
                i ->
                    new MeasuredIngredient(
                        i.getName(),
                        MeasurementUnitType.valueOf(i.getUnitType().name()),
                        i.getValue()))
            .collect(Collectors.toList()),
        recipe.getInstructions());
  }
}
