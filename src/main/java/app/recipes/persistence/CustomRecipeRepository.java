package app.recipes.persistence;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CustomRecipeRepository {
  private final MongoTemplate mongoTemplate;

  public CustomRecipeRepository(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  public List<Recipe> findByFilter(
      Integer servings,
      Recipe.RecipeType recipeType,
      List<String> includedIngredients,
      List<String> excludedIngredients,
      String instructionSearchKey) {
    Optional<Integer> optionalServings = Optional.ofNullable(servings);
    Optional<Recipe.RecipeType> optionalRecipeType = Optional.ofNullable(recipeType);
    Optional<List<String>> optionalIncludedIngredients = Optional.ofNullable(includedIngredients);
    Optional<List<String>> optionalExcludedIngredients = Optional.ofNullable(excludedIngredients);
    Optional<String> optionalInstructionSearchKey = Optional.ofNullable(instructionSearchKey);

    Query query = new Query();
    optionalServings.ifPresent(s -> query.addCriteria(Criteria.where("servings").is(s)));
    optionalRecipeType.ifPresent(t -> query.addCriteria(Criteria.where("type").is(t)));

    if (optionalIncludedIngredients.isPresent() || optionalExcludedIngredients.isPresent()) {
      Criteria filterCriteria = Criteria.where("ingredients.name");
      optionalIncludedIngredients.ifPresent(filterCriteria::all);
      optionalExcludedIngredients.ifPresent(filterCriteria::nin);
      query.addCriteria(filterCriteria);
    }

    optionalInstructionSearchKey.ifPresent(
        k -> query.addCriteria(Criteria.where("instructions").regex(k)));

    return mongoTemplate.find(query, Recipe.class);
  }
}
