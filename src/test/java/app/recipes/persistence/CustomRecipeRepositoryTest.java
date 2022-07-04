package app.recipes.persistence;

import app.recipes.persistence.Recipe.MeasuredIngredient;
import app.recipes.persistence.Recipe.MeasurementUnitType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CustomRecipeRepositoryTest {
  @Autowired private MongoTemplate mongoTemplate;

  private CustomRecipeRepository customRecipeRepository;

  private Recipe applePie;
  private Recipe peachPie;
  private Recipe strawberryPie;
  private Recipe cherryPie;

  @BeforeEach
  void setUp() {
    customRecipeRepository = new CustomRecipeRepository(mongoTemplate);
    mongoTemplate.dropCollection(Recipe.class);
    List<String> pieInstructions =
        List.of(
            "Preheat the oven at 210C",
            "Mix all ingredients",
            "Turn composition in a tray",
            "Bake for 30 min",
            "Take out, cut and enjoy");

    applePie =
        addPie(
            "Apple Pie",
            5,
            Recipe.RecipeType.VEGETARIAN,
            pieInstructions,
            new MeasuredIngredient("Apple", MeasurementUnitType.GRAMS, 800d),
            new MeasuredIngredient("Milk", MeasurementUnitType.MILLILITERS, 60d));

    peachPie =
        addPie(
            "Peach Pie",
            2,
            Recipe.RecipeType.VEGETARIAN,
            pieInstructions,
            new MeasuredIngredient("Peach", MeasurementUnitType.GRAMS, 800d),
            new MeasuredIngredient("Milk", MeasurementUnitType.MILLILITERS, 60d));

    strawberryPie =
        addPie(
            "Strawberry Pie",
            6,
            Recipe.RecipeType.VEGAN,
            pieInstructions,
            new MeasuredIngredient("Strawberry", MeasurementUnitType.GRAMS, 800d));

    cherryPie =
        addPie(
            "Cherry Pie",
            3,
            Recipe.RecipeType.VEGETARIAN,
            pieInstructions,
            new MeasuredIngredient("Cherry", MeasurementUnitType.GRAMS, 800d),
            new MeasuredIngredient("Milk", MeasurementUnitType.MILLILITERS, 60d));
  }

  @DisplayName("Return all pies when `filter={}`")
  @Test
  void findByFilter_1() {
    List<Recipe> foundRecipes = customRecipeRepository.findByFilter(null, null, null, null, null);

    List<String> expectedRecipeIds =
        List.of(applePie.getId(), cherryPie.getId(), peachPie.getId(), strawberryPie.getId());

    assertThat(foundRecipes.size()).isEqualTo(expectedRecipeIds.size());
    assertThat(foundRecipes.stream().allMatch(r -> expectedRecipeIds.contains(r.getId()))).isTrue();
  }

  @DisplayName("Return no pies when `filter={servings:1}` but no recipes matches")
  @Test
  void findByFilter_2() {
    List<Recipe> foundRecipes = customRecipeRepository.findByFilter(1, null, null, null, null);

    assertThat(foundRecipes).isEmpty();
  }

  @DisplayName("Return strawberry pie when `filter={servings:6}`")
  @Test
  void findByFilter_3() {
    List<Recipe> foundRecipes = customRecipeRepository.findByFilter(6, null, null, null, null);

    List<String> expectedRecipeIds = List.of(strawberryPie.getId());

    assertThat(foundRecipes.size()).isEqualTo(expectedRecipeIds.size());
    assertThat(foundRecipes.stream().allMatch(r -> expectedRecipeIds.contains(r.getId()))).isTrue();
  }

  @DisplayName("Return all vegetarian pies when `filter={type:VEGETARIAN}`")
  @Test
  void findByFilter_4() {
    List<Recipe> foundRecipes =
        customRecipeRepository.findByFilter(null, Recipe.RecipeType.VEGETARIAN, null, null, null);

    List<String> expectedRecipeIds = List.of(applePie.getId(), cherryPie.getId(), peachPie.getId());

    assertThat(foundRecipes.size()).isEqualTo(expectedRecipeIds.size());
    assertThat(foundRecipes.stream().allMatch(r -> expectedRecipeIds.contains(r.getId()))).isTrue();
  }

  @DisplayName("Return apple pie when `filter={type:VEGETARIAN, ingredients:['Apple', 'Flour']}`")
  @Test
  void findByFilter_5() {
    List<Recipe> foundRecipes =
        customRecipeRepository.findByFilter(
            null, Recipe.RecipeType.VEGETARIAN, List.of("Apple", "Flour"), null, null);

    List<String> expectedRecipeIds = List.of(applePie.getId());

    assertThat(foundRecipes.size()).isEqualTo(expectedRecipeIds.size());
    assertThat(foundRecipes.stream().allMatch(r -> expectedRecipeIds.contains(r.getId()))).isTrue();
  }

  @DisplayName(
      "Return [cherry, peach] pies when `filter={type:VEGETARIAN, ingredients:[!'Apple', 'Flour']}`")
  @Test
  void findByFilter_6() {
    List<Recipe> foundRecipes =
        customRecipeRepository.findByFilter(
            null, Recipe.RecipeType.VEGETARIAN, List.of("Flour"), List.of("Apple"), null);

    List<String> expectedRecipeIds = List.of(cherryPie.getId(), peachPie.getId());

    assertThat(foundRecipes.size()).isEqualTo(expectedRecipeIds.size());
    assertThat(foundRecipes.stream().allMatch(r -> expectedRecipeIds.contains(r.getId()))).isTrue();
  }

  @DisplayName(
      "Return [cherry, peach, strawberry] pies when `filter={ingredients:[!'Apple', 'Flour']}`")
  @Test
  void findByFilter_7() {
    List<Recipe> foundRecipes =
        customRecipeRepository.findByFilter(null, null, List.of("Flour"), List.of("Apple"), null);

    List<String> expectedRecipeIds =
        List.of(cherryPie.getId(), peachPie.getId(), strawberryPie.getId());

    assertThat(foundRecipes.size()).isEqualTo(expectedRecipeIds.size());
    assertThat(foundRecipes.stream().allMatch(r -> expectedRecipeIds.contains(r.getId()))).isTrue();
  }

  @DisplayName(
      "Return [cherry,apple, strawberry] pies when `filter={ingredients:[!'Peach'], instructions: contains 'oven'}`")
  @Test
  void findByFilter_8() {
    List<Recipe> foundRecipes =
        customRecipeRepository.findByFilter(null, null, null, List.of("Peach"), "oven");

    List<String> expectedRecipeIds =
        List.of(cherryPie.getId(), strawberryPie.getId(), applePie.getId());

    assertThat(foundRecipes.size()).isEqualTo(expectedRecipeIds.size());
    assertThat(foundRecipes.stream().allMatch(r -> expectedRecipeIds.contains(r.getId()))).isTrue();
  }

  @DisplayName("Return no pies when `filter={ingredients:[!'Peach'], instructions: has 'freezer'}`")
  @Test
  void findByFilter_9() {
    List<Recipe> foundRecipes =
        customRecipeRepository.findByFilter(null, null, null, List.of("Peach"), "freezer");

    assertThat(foundRecipes).isEmpty();
  }

  private Recipe addPie(
      String name,
      Integer servings,
      Recipe.RecipeType recipeType,
      List<String> instructions,
      MeasuredIngredient... extra) {
    List<MeasuredIngredient> ingredients =
        new ArrayList<>(
            List.of(
                new MeasuredIngredient("Flour", MeasurementUnitType.GRAMS, 500d),
                new MeasuredIngredient("Eggs", MeasurementUnitType.PIECES, 5d),
                new MeasuredIngredient("Sugar", MeasurementUnitType.GRAMS, 55d)));
    ingredients.addAll(Arrays.asList(extra));

    Recipe recipe = new Recipe();
    recipe.setName(name);
    recipe.setServings(servings);
    recipe.setType(recipeType);
    recipe.setIngredients(ingredients);
    recipe.setInstructions(instructions);

    return mongoTemplate.insert(recipe);
  }
}
