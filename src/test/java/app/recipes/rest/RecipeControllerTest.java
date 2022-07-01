package app.recipes.rest;

import app.recipes.persistence.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static app.recipes.rest.RecipeType.STANDARD;
import static app.recipes.rest.RecipeType.VEGETARIAN;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RecipeControllerTest {
  private static final String RECIPES_BASE_URL = "/recipes";

  @Autowired private RecipeRepository recipeRepository;
  @Autowired private TestRestTemplate restTemplate;

  @Test
  void createRecipe() {
    RecipeRequest recipeRequest =
        new RecipeRequest(
            "Apple pie",
            VEGETARIAN,
            2,
            Map.of("apples", "1kg", "eggs", "10"),
            List.of(
                "Mix the apples with eggs",
                "Bake at 220C for 10 min",
                "Decorate with apples. Enjoy!"));

    ResponseEntity<RecipeResponse> responseEntity =
        restTemplate.postForEntity(RECIPES_BASE_URL, recipeRequest, RecipeResponse.class);

    RecipeResponse recipeResponse = responseEntity.getBody();

    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(recipeResponse).isNotNull();
    assertThat(recipeResponse.getId()).isNotNull();
    assertThat(recipeResponse.getName()).isEqualTo(recipeRequest.getName());
    assertThat(recipeResponse.getType().name()).isEqualTo(recipeRequest.getType().name());
    assertThat(recipeResponse.getServings()).isEqualTo(recipeRequest.getServings());
    assertThat(recipeResponse.getInstructions()).isEqualTo(recipeRequest.getInstructions());
    assertThat(recipeResponse.getIngredients()).isEqualTo(recipeRequest.getIngredients());

    assertThat(recipeRepository.findById(recipeResponse.getId())).isPresent();
  }

  @ParameterizedTest
  @MethodSource("createRecipeFailsInputValidationTestCases")
  void createRecipeFailsInputValidation(RecipeRequest recipeRequest) {
    ResponseEntity<RecipeResponse> responseEntity =
        restTemplate.postForEntity(RECIPES_BASE_URL, recipeRequest, RecipeResponse.class);

    RecipeResponse recipeResponse = responseEntity.getBody();
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(recipeResponse).isNotNull();
  }

  static Stream<Arguments> createRecipeFailsInputValidationTestCases() {
    return Stream.of(
        arguments(new RecipeRequest(null, STANDARD, 10, Map.of("apples", "1kg"), List.of("Cook!"))),
        arguments(
            new RecipeRequest("Pie", STANDARD, -10, Map.of("apples", "1kg"), List.of("Cook!"))),
        arguments(new RecipeRequest("Pie", STANDARD, 1, Map.of(), List.of("Cook!"))),
        arguments(new RecipeRequest("Pie", STANDARD, 3, Map.of("apples", "1kg"), List.of())),
        arguments(new RecipeRequest("Pie", STANDARD, 10, null, null)));
  }
}
