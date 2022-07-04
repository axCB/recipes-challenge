package app.recipes;

import app.recipes.persistence.CustomRecipeRepository;
import app.recipes.persistence.Recipe;
import app.recipes.persistence.RecipeRepository;
import app.recipes.rest.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RecipeServiceTest {
  private final RecipeRepository recipeRepository = mock(RecipeRepository.class);
  private final CustomRecipeRepository customRecipeRepository = mock(CustomRecipeRepository.class);
  private final RecipeService recipeService =
      new RecipeService(recipeRepository, customRecipeRepository);

  @Test
  void createRecipe() {
    RecipeRequest request =
        new RecipeRequest(
            "Apple pie",
            RecipeType.VEGETARIAN,
            2,
            List.of(
                new MeasuredIngredient("apples", MeasurementUnitType.GRAMS, 1000d),
                new MeasuredIngredient("eggs", MeasurementUnitType.PIECES, 10d)),
            List.of(
                "Mix the apples with eggs",
                "Bake at 220C for 10 min",
                "Decorate with apples. Enjoy"));

    Recipe recipe = RecipeConverter.toRecipe(request);
    recipe.setId("328382808047hd32");
    when(recipeRepository.insert(any(Recipe.class))).thenReturn(recipe);

    RecipeResponse recipeResponse = recipeService.create(request);
    assertThat(recipeResponse.getId()).isNotNull();
    assertThat(recipeResponse.getName()).isEqualTo(request.getName());
    assertThat(recipeResponse.getServings()).isEqualTo(request.getServings());
    assertThat(recipeResponse.getType()).isEqualTo(request.getType());
    assertThat(recipeResponse.getInstructions()).isEqualTo(request.getInstructions());
  }

  @Test
  void updateRecipe() {
    RecipeRequest request =
        new RecipeRequest(
            "Apple pie",
            RecipeType.VEGETARIAN,
            2,
            List.of(
                new MeasuredIngredient("apples", MeasurementUnitType.GRAMS, 1000d),
                new MeasuredIngredient("eggs", MeasurementUnitType.PIECES, 10d)),
            List.of(
                "Mix the apples with eggs",
                "Bake at 220C for 10 min",
                "Decorate with apples. Enjoy"));

    String existingRecipeId = "328382808047hd32";
    Recipe recipe = new Recipe();
    recipe.setId(existingRecipeId);
    recipe.setName("PIE");
    recipe.setServings(5);
    recipe.setType(Recipe.RecipeType.VEGETARIAN);
    recipe.setInstructions(List.of("To be added"));
    recipe.setIngredients(
        List.of(new Recipe.MeasuredIngredient("flour", Recipe.MeasurementUnitType.GRAMS, 1000d)));

    when(recipeRepository.findById(existingRecipeId)).thenReturn(Optional.of(recipe));
    when(recipeRepository.save(recipe)).thenReturn(recipe);

    RecipeResponse recipeResponse = recipeService.update(request, existingRecipeId);
    assertThat(recipeResponse.getId()).isNotNull();
    assertThat(recipeResponse.getIngredients()).isEqualTo(request.getIngredients());
    assertThat(recipeResponse.getName()).isEqualTo(request.getName());
    assertThat(recipeResponse.getServings()).isEqualTo(request.getServings());
    assertThat(recipeResponse.getType()).isEqualTo(request.getType());
    assertThat(recipeResponse.getInstructions()).isEqualTo(request.getInstructions());
  }
}
