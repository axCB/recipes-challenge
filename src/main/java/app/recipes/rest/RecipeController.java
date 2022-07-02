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

@RestController
@RequestMapping("/recipes")
@Validated
public class RecipeController {
  private final RecipeService recipesService;

  public RecipeController(RecipeService recipesService) {
    this.recipesService = recipesService;
  }

  @PostMapping(
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public RecipeResponse createRecipe(@Valid @RequestBody RecipeRequest recipeRequest) {
    return recipesService.create(recipeRequest);
  }

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

  @DeleteMapping(value = "/{recipeId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public void deleteRecipe(@PathVariable @NotNull @NotBlank String recipeId) {
    recipesService.delete(recipeId);
  }

  @GetMapping(value = "/{recipeId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public RecipeResponse findById(@PathVariable @NotNull @NotBlank String recipeId) {
    return recipesService.findById(recipeId);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public List<RecipeResponse> findAll() {
    return recipesService.findAll();
  }
}
