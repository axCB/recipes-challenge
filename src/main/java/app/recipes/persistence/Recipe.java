package app.recipes.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document("recipes")
public class Recipe {
  @Id private String id;
  private String name;
  private RecipeType type;
  private Integer servings;
  private Map<String, String> ingredients;
  private List<String> instructions;

  public Recipe() {}

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public RecipeType getType() {
    return type;
  }

  public void setType(RecipeType type) {
    this.type = type;
  }

  public Integer getServings() {
    return servings;
  }

  public void setServings(Integer servings) {
    this.servings = servings;
  }

  public Map<String, String> getIngredients() {
    return ingredients;
  }

  public void setIngredients(Map<String, String> ingredients) {
    this.ingredients = ingredients;
  }

  public List<String> getInstructions() {
    return instructions;
  }

  public void setInstructions(List<String> instructions) {
    this.instructions = instructions;
  }

  public enum RecipeType {
    STANDARD,
    PESCETERIAN,
    LACTO_VEGETARIAN,
    OVO_VEGETARIAN,
    VEGETARIAN,
    VEGAN
  }
}
