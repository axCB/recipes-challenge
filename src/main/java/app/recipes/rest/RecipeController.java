package app.recipes.rest;

import app.recipes.RecipeService;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/recipes")
public class RecipeController {
  private final RecipeService recipesService;

  public RecipeController(RecipeService recipesService) {
    this.recipesService = recipesService;
  }

  @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
  @ResponseStatus(CREATED)
  public RecipeResponse createRecipe(@RequestBody RecipeRequest recipeRequest) {
    return recipesService.createRecipe(recipeRequest);
  }
}
