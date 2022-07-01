package app.recipes.rest;

import app.recipes.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/recipes")
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
    return recipesService.createRecipe(recipeRequest);
  }
}
