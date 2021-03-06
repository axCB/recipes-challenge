package app.recipes.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class RecipeRequest {
  private final @NotBlank String name;
  private final @NotNull RecipeType type;
  private final @NotNull @Min(value = 1) Integer servings;
  private final @NotEmpty List<MeasuredIngredient> ingredients;
  private final @NotEmpty List<String> instructions;

  @JsonCreator
  public RecipeRequest(
      @JsonProperty("name") String name,
      @JsonProperty("type") RecipeType type,
      @JsonProperty("servings") Integer servings,
      @JsonProperty("ingredients") List<MeasuredIngredient> ingredients,
      @JsonProperty("instructions") List<String> instructions) {
    this.name = name;
    this.type = type;
    this.servings = servings;
    this.ingredients = ingredients;
    this.instructions = instructions;
  }

  public String getName() {
    return name;
  }

  public RecipeType getType() {
    return type;
  }

  public Integer getServings() {
    return servings;
  }

  public List<MeasuredIngredient> getIngredients() {
    return ingredients;
  }

  public List<String> getInstructions() {
    return instructions;
  }
}
