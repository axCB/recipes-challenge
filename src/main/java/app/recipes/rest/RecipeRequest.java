package app.recipes.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class RecipeRequest {
  private final String name;
  private final RecipeType type;
  private final Integer servings;
  private final Map<String, String> ingredients;
  private final List<String> instructions;

  @JsonCreator
  public RecipeRequest(
      @JsonProperty("name") String name,
      @JsonProperty("type") RecipeType type,
      @JsonProperty("servings") Integer servings,
      @JsonProperty("ingredients") Map<String, String> ingredients,
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

  public Map<String, String> getIngredients() {
    return ingredients;
  }

  public List<String> getInstructions() {
    return instructions;
  }
}
