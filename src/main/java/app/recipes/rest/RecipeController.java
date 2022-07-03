package app.recipes.rest;

import app.recipes.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/** Controller class for Recipe resource defining rest endpoints available. */
@RestController
@RequestMapping("/recipes")
@Validated
public class RecipeController {
  private final RecipeService recipesService;

  public RecipeController(RecipeService recipesService) {
    this.recipesService = recipesService;
  }

  /**
   * Creates a new recipe and returns it.
   *
   * @param recipeRequest - recipe to be created
   * @return the created recipe.
   */
  @PostMapping(
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public RecipeResponse createRecipe(@Valid @RequestBody RecipeRequest recipeRequest) {
    return recipesService.create(recipeRequest);
  }

  /**
   * Updates an existing recipe and returns it.
   *
   * @param recipeId - id of the recipe to be updated.
   * @param recipeRequest - recipe with new details that will be updated.
   * @return the updated recipe if it exists or HttpStatus.NOT_FOUND when recipe not found.
   */
  @PutMapping(
      value = "/{recipeId}",
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public RecipeResponse updateRecipe(
      @PathVariable @NotNull @NotBlank String recipeId,
      @RequestBody @Valid RecipeRequest recipeRequest) {
    return recipesService.update(recipeRequest, recipeId);
  }

  /**
   * Deletes a recipe and returns HttpStatus.OK or returns HttpStatus.NOT_FOUND when recipe was not
   * found.
   *
   * @param recipeId - id of the recipe to be deleted.
   */
  @DeleteMapping(value = "/{recipeId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public void deleteRecipe(@PathVariable @NotNull @NotBlank String recipeId) {
    recipesService.delete(recipeId);
  }

  /**
   * Returns recipe by recipeId
   *
   * @param recipeId - id of the recipe to be returned.
   * @return recipe identified by the specified recipeId when found or HttpStatus.NOT_FOUND when
   *     recipe was not found.
   */
  @GetMapping(value = "/{recipeId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public RecipeResponse findById(@PathVariable @NotNull @NotBlank String recipeId) {
    return recipesService.findById(recipeId);
  }

  /**
   * Returns all recipes that exists.
   *
   * @return a list of recipes that exists.
   */
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public List<RecipeResponse> findAll() {
    return recipesService.findAll();
  }
}
