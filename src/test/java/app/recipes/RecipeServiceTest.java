package app.recipes;

import app.recipes.persistence.Recipe;
import app.recipes.persistence.RecipeRepository;
import app.recipes.rest.RecipeRequest;
import app.recipes.rest.RecipeResponse;
import app.recipes.rest.RecipeType;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RecipeServiceTest {
  private final RecipeRepository recipeRepository = mock(RecipeRepository.class);
  private final RecipeService recipeService = new RecipeService(recipeRepository);

  @Test
  void createRecipe() {
    RecipeRequest request =
        new RecipeRequest(
            "Apple pie",
            RecipeType.VEGETARIAN,
            2,
            Map.of("apples", "1kg", "eggs", "10"),
            List.of(
                "Mix the masshed apples with eggs",
                "Bake at 220C for 10 min",
                "Decorate with apples. Enjoy"));

    Recipe recipe = RecipeConverter.toRecipe(request);
    recipe.setId("328382808047hd32");
    when(recipeRepository.insert(any(Recipe.class))).thenReturn(recipe);

    RecipeResponse recipeResponse = recipeService.create(request);
    assertThat(recipeResponse.getId()).isNotNull();
    assertThat(recipeResponse.getIngredients()).isEqualTo(request.getIngredients());
    assertThat(recipeResponse.getName()).isEqualTo(request.getName());
    assertThat(recipeResponse.getServings()).isEqualTo(request.getServings());
    assertThat(recipeResponse.getType()).isEqualTo(request.getType());
    assertThat(recipeResponse.getInstructions()).isEqualTo(request.getInstructions());
  }
}
