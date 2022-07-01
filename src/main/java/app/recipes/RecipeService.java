package app.recipes;

import app.recipes.converter.RecipeConverter;
import app.recipes.persistence.Recipe;
import app.recipes.persistence.RecipeRepository;
import app.recipes.rest.RecipeRequest;
import app.recipes.rest.RecipeResponse;
import org.springframework.stereotype.Service;

@Service
public class RecipeService {
  private final RecipeRepository recipeRepository;

  public RecipeService(RecipeRepository recipeRepository) {
    this.recipeRepository = recipeRepository;
  }

  public RecipeResponse createRecipe(RecipeRequest recipeRequest) {
    Recipe recipe = RecipeConverter.toRecipe(recipeRequest);
    Recipe createdRecipe = recipeRepository.insert(recipe);
    return RecipeConverter.toRecipeResponse(createdRecipe);
  }
}
