package app.recipes;

import app.recipes.exceptions.custom.NotFoundException;
import app.recipes.persistence.CustomRecipeRepository;
import app.recipes.persistence.Recipe;
import app.recipes.persistence.RecipeRepository;
import app.recipes.rest.RecipeRequest;
import app.recipes.rest.RecipeResponse;
import app.recipes.rest.RecipeType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** Service class for Recipes operations such as create, update, delete, findById, findAll. */
@Service
public class RecipeService {
  private final RecipeRepository recipeRepository;
  private final CustomRecipeRepository customRecipeRepository;

  public RecipeService(
      RecipeRepository recipeRepository, CustomRecipeRepository customRecipeRepository) {
    this.recipeRepository = recipeRepository;
    this.customRecipeRepository = customRecipeRepository;
  }

  public RecipeResponse create(RecipeRequest recipeRequest) {
    Recipe recipe = RecipeConverter.toRecipe(recipeRequest);
    Recipe createdRecipe = recipeRepository.insert(recipe);
    return RecipeConverter.toRecipeResponse(createdRecipe);
  }

  public RecipeResponse update(RecipeRequest recipeRequest, String recipeId) {
    Recipe recipeToBeUpdated = getRecipe(recipeId);
    recipeToBeUpdated.setName(recipeRequest.getName());
    recipeToBeUpdated.setType(Recipe.RecipeType.valueOf(recipeRequest.getType().name()));
    recipeToBeUpdated.setServings(recipeRequest.getServings());
    recipeToBeUpdated.setIngredients(
        recipeRequest.getIngredients().stream()
            .map(
                i ->
                    new Recipe.MeasuredIngredient(
                        i.getName(),
                        Recipe.MeasurementUnitType.valueOf(i.getUnitType().name()),
                        i.getValue()))
            .collect(Collectors.toList()));
    recipeToBeUpdated.setInstructions(recipeRequest.getInstructions());
    Recipe createdRecipe = recipeRepository.save(recipeToBeUpdated);
    return RecipeConverter.toRecipeResponse(createdRecipe);
  }

  public void delete(String recipeId) {
    Recipe recipe = getRecipe(recipeId);
    recipeRepository.delete(recipe);
  }

  public RecipeResponse findById(String id) {
    Recipe createdRecipe = getRecipe(id);
    return RecipeConverter.toRecipeResponse(createdRecipe);
  }

  public List<RecipeResponse> findAllByFilters(
      RecipeType recipeType,
      Integer servings,
      List<String> includedIngredients,
      List<String> excludedIngredients,
      String instructionsSearchKey) {
    Recipe.RecipeType type =
        recipeType != null ? Recipe.RecipeType.valueOf(recipeType.name()) : null;
    return customRecipeRepository
        .findByFilter(
            servings, type, includedIngredients, excludedIngredients, instructionsSearchKey)
        .stream()
        .map(RecipeConverter::toRecipeResponse)
        .collect(Collectors.toList());
  }

  private Recipe getRecipe(String id) {
    Optional<Recipe> recipe = recipeRepository.findById(id);
    if (recipe.isEmpty()) {
      String notFoundErrorMessage = String.format("Recipe with id %s was not found", id);
      throw new NotFoundException(notFoundErrorMessage);
    }
    return recipe.get();
  }
}
