package app.recipes.rest;

import app.recipes.IntegrationTest;
import app.recipes.exceptions.Error;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static app.recipes.rest.RecipeType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class RecipeControllerTest extends IntegrationTest {
  private static final String RECIPES_BASE_URL = "/recipes";

  @Autowired private TestRestTemplate restTemplate;

  @DisplayName("Create new recipe succeeds")
  @Test
  void createRecipe_1() {
    RecipeRequest recipeRequest = buildBasicRecipeRequest();
    ResponseEntity<RecipeResponse> responseEntity = executeCreateNewRecipe(recipeRequest);

    assertResponse(recipeRequest, responseEntity, HttpStatus.CREATED);
  }

  static Stream<Arguments> createRecipeInvalidInputTestCases() {
    return Stream.of(
        arguments(
            new RecipeRequest(
                null,
                STANDARD,
                10,
                List.of(new MeasuredIngredient("apples", MeasurementUnitType.GRAMS, 1000d)),
                List.of("Cook!"))),
        arguments(
            new RecipeRequest(
                "Pie",
                STANDARD,
                -10,
                List.of(new MeasuredIngredient("apples", MeasurementUnitType.GRAMS, 1000d)),
                List.of("Cook!"))),
        arguments(
            new RecipeRequest(
                "Pie",
                STANDARD,
                3,
                List.of(new MeasuredIngredient("apples", MeasurementUnitType.GRAMS, 1000d)),
                List.of())),
        arguments(new RecipeRequest("Pie", STANDARD, 1, List.of(), List.of("Cook!"))),
        arguments(new RecipeRequest("Pie", STANDARD, 10, null, null)));
  }

  @DisplayName("Create new recipe fails when invalid input provided")
  @ParameterizedTest
  @MethodSource("createRecipeInvalidInputTestCases")
  void createRecipe_2(RecipeRequest recipeRequest) {
    ResponseEntity<RecipeResponse> responseEntity = executeCreateNewRecipe(recipeRequest);

    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(responseEntity.getBody()).isNotNull();
  }

  @DisplayName("Find by id returns recipe matching id when found")
  @Test
  void findByIdRecipe_1() {
    RecipeRequest recipeRequest = buildBasicRecipeRequest();
    ResponseEntity<RecipeResponse> responseEntity = executeCreateNewRecipe(recipeRequest);

    ResponseEntity<RecipeResponse> findByIdResponse =
        restTemplate.getForEntity(
            RECIPES_BASE_URL + "/{recipeId}",
            RecipeResponse.class,
            Objects.requireNonNull(responseEntity.getBody()).getId());

    assertResponse(recipeRequest, findByIdResponse, HttpStatus.OK);
  }

  @DisplayName("Find by id fails with 404 when recipe is not found")
  @Test
  void findByIdRecipe_2() {
    ResponseEntity<RecipeResponse> findByIdResponse =
        restTemplate.getForEntity(RECIPES_BASE_URL + "/{recipeId}", RecipeResponse.class, "22");

    assertThat(findByIdResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @DisplayName("Update existing recipe succeeds")
  @Test
  void updateRecipe_1() {
    RecipeRequest createRecipeRequest = buildBasicRecipeRequest();

    ResponseEntity<RecipeResponse> createRecipeResponse =
        executeCreateNewRecipe(createRecipeRequest);

    RecipeResponse createRecipeResponseBody = createRecipeResponse.getBody();
    assertThat(createRecipeResponseBody).isNotNull();

    RecipeRequest updateRequest =
        new RecipeRequest(
            "Cherry Pie",
            VEGAN,
            2,
            List.of(new MeasuredIngredient("cherry", MeasurementUnitType.GRAMS, 1000d)),
            List.of("Eat the cherries as a pie."));

    ResponseEntity<RecipeResponse> updateRecipeResponse =
        restTemplate.exchange(
            RECIPES_BASE_URL + "/{recipeId}",
            HttpMethod.PUT,
            new HttpEntity<>(updateRequest),
            RecipeResponse.class,
            createRecipeResponseBody.getId());

    assertResponse(updateRequest, updateRecipeResponse, HttpStatus.OK);
  }

  @DisplayName("Updating recipe fails with 404 status when recipe does not exist")
  @Test
  void updateRecipe_2() {
    RecipeRequest updateRecipeRequest = buildBasicRecipeRequest();

    ResponseEntity<RecipeResponse> responseEntity =
        restTemplate.exchange(
            RECIPES_BASE_URL + "/{recipeId}",
            HttpMethod.PUT,
            new HttpEntity<>(updateRecipeRequest),
            RecipeResponse.class,
            "333");

    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @DisplayName("Delete existing recipe succeeds")
  @Test
  void deleteRecipe_1() {
    RecipeRequest createRecipeRequest = buildBasicRecipeRequest();
    ResponseEntity<RecipeResponse> createRecipeResponse =
        executeCreateNewRecipe(createRecipeRequest);

    RecipeResponse createRecipeResponseBody = createRecipeResponse.getBody();
    assertThat(createRecipeResponseBody).isNotNull();

    Assertions.assertThatNoException()
        .isThrownBy(
            () ->
                restTemplate.delete(
                    RECIPES_BASE_URL + "/{recipeId}", createRecipeResponseBody.getId()));
  }

  @DisplayName("Delete recipe fails with 404 status when recipe does not exist")
  @Test
  void deleteRecipe_2() {
    ResponseEntity<Error> responseEntity =
        restTemplate.exchange(
            RECIPES_BASE_URL + "/{recipeId}", HttpMethod.DELETE, null, Error.class, 404);
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @DisplayName("Find all returns recipes matching provided filters")
  @Test
  void findAll() {
    RecipeRequest createRecipeRequest = buildBasicRecipeRequest();
    ResponseEntity<RecipeResponse> createdRequest = executeCreateNewRecipe(createRecipeRequest);

    MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
    requestParams.put("servings", List.of("1"));
    requestParams.put("exclude-ingredients", List.of("sugar"));
    requestParams.put("type", List.of("VEGAN"));

    ResponseEntity<List> findAllResponse = executeGetWithQueryParams(requestParams);

    // nothing should be returned - we only have a vegetarian recipe
    assertThat(findAllResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    List<RecipeResponse> recipes = (List<RecipeResponse>) findAllResponse.getBody();
    assertThat(recipes).isEmpty();

    // on second request - recipe will match filters and will be returned
    MultiValueMap<String, String> requestParamsRequest2 = new LinkedMultiValueMap<>();
    requestParamsRequest2.put("servings", List.of("1"));
    requestParamsRequest2.put("exclude-ingredients", List.of("sugar"));
    requestParamsRequest2.put("type", List.of("VEGETARIAN"));
    ResponseEntity<List> findAllResponse2 = executeGetWithQueryParams(requestParamsRequest2);

    assertThat(findAllResponse2.getStatusCode()).isEqualTo(HttpStatus.OK);
    List<RecipeResponse> recipes2 = (List<RecipeResponse>) findAllResponse2.getBody();
    assertThat(recipes2).isNotEmpty();
    assertThat(recipes2.size()).isEqualTo(1);
  }

  private RecipeRequest buildBasicRecipeRequest() {
    return new RecipeRequest(
        "Apple pie",
        VEGETARIAN,
        1,
        List.of(
            new MeasuredIngredient("apples", MeasurementUnitType.GRAMS, 1000d),
            new MeasuredIngredient("eggs", MeasurementUnitType.PIECES, 10d)),
        List.of(
            "Mix the apples with eggs", "Bake at 220C for 10 min", "Decorate with apples. Enjoy!"));
  }

  private ResponseEntity<RecipeResponse> executeCreateNewRecipe(RecipeRequest recipeRequest) {
    return restTemplate.postForEntity(RECIPES_BASE_URL, recipeRequest, RecipeResponse.class);
  }

  private ResponseEntity<List> executeGetWithQueryParams(
      MultiValueMap<String, String> requestParams) {
    HttpEntity<String> requestEntity = new HttpEntity<>(null, null);

    return restTemplate.exchange(
        UriComponentsBuilder.fromUriString(RECIPES_BASE_URL)
            .queryParams(requestParams)
            .encode()
            .toUriString(),
        HttpMethod.GET,
        requestEntity,
        List.class,
        requestParams);
  }

  private void assertResponse(
      RecipeRequest request,
      ResponseEntity<RecipeResponse> response,
      HttpStatus expectedStatusCode) {
    assertThat(response.getStatusCode()).isEqualTo(expectedStatusCode);
    RecipeResponse responseBody = response.getBody();
    assertThat(responseBody).isNotNull();
    assertThat(responseBody.getId()).isNotNull();
    assertThat(responseBody.getName()).isEqualTo(request.getName());
    assertThat(responseBody.getType().name()).isEqualTo(request.getType().name());
    assertThat(responseBody.getServings()).isEqualTo(request.getServings());
    assertThat(responseBody.getInstructions()).isEqualTo(request.getInstructions());
    assertThat(responseBody.getIngredients()).isEqualTo(request.getIngredients());
  }
}
