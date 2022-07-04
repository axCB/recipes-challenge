package app.recipes.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class RecipeResponse {
  private final String id;
  private final String name;
  private final RecipeType type;
  private final Integer servings;
  private final List<MeasuredIngredient> ingredients;
  private final List<String> instructions;

  @JsonCreator
  public RecipeResponse(
      @JsonProperty("id") String id,
      @JsonProperty("name") String name,
      @JsonProperty("type") RecipeType type,
      @JsonProperty("servings") Integer servings,
      @JsonProperty("ingredients") List<MeasuredIngredient> ingredients,
      @JsonProperty("instructions") List<String> instructions) {
    this.id = id;
    this.name = name;
    this.type = type;
    this.servings = servings;
    this.ingredients = ingredients;
    this.instructions = instructions;
  }

  public String getId() {
    return id;
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
