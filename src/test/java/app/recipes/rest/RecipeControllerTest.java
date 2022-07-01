package app.recipes.rest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Profile("test")
public class RecipeControllerTest {
  @Autowired private TestRestTemplate restTemplate;

  @Test
  void createRecipe() {
    RecipeRequest recipeRequest =
        new RecipeRequest(
            "Apple pie",
            RecipeType.VEGETARIAN,
            2,
            Map.of("apples", "1kg", "eggs", "10"),
            List.of(
                "Mix the apples with eggs",
                "Bake at 220C for 10 min",
                "Decorate with apples. Enjoy!"));

    ResponseEntity<RecipeResponse> responseEntity =
        restTemplate.postForEntity(
            "http://localhost:8080/recipes", recipeRequest, RecipeResponse.class);

    RecipeResponse recipeResponse = responseEntity.getBody();

    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(recipeResponse).isNotNull();
    assertThat(recipeResponse.getId()).isNotNull();
    assertThat(recipeResponse.getName()).isEqualTo(recipeRequest.getName());
    assertThat(recipeResponse.getType().name()).isEqualTo(recipeRequest.getType().name());
    assertThat(recipeResponse.getServings()).isEqualTo(recipeRequest.getServings());
    assertThat(recipeResponse.getInstructions()).isEqualTo(recipeRequest.getInstructions());
    assertThat(recipeResponse.getIngredients()).isEqualTo(recipeRequest.getIngredients());
  }
}
