# Recipes

This java application allows users to manage their favourite recipes by adding, updating, removing and fetching recipes.
Available recipes can be filtered based on one or more of the following criteria:

1. Whether or not the dish is vegetarian
2. The number of servings
3. Specific ingredients (either include or exclude)
4. Text search within the instructions.

Example of search request:

* All vegetarian recipes
* Recipes that can serve 4 persons and have “potatoes” as an ingredient
* Recipes without “salmon” as an ingredient that has “oven” in the instructions.

## Project structure

The project uses: Spring Boot( to ease up configuration set up), embedded in-memory mongo( for storing resources)
Project has the following structure:

* package `app.recipes` - base package for recipes functionality; contains also RecipeConverter for mapping back and
  forward between RecipeRequest, Recipe and RecipeResponse and RecipeService for accessing repository methods.
* package `app.recipes.persistence` - package with Recipe document definition and RecipeRepository for accessing
  database
* package `app.recipes.rest` - package with RecipeController class and definitions of request and response objects

#### Run application

1. Run **./mvnw clean package**. This will create the executable .jar inside the target folder.
2. Run **java -jar target/recipes-challenge-1.0.0.jar** to start app the application.
3. Once the application is up, the API will be accessible under http://localhost:8080/recipes

#### Endpoint design specifications

* Create recipe: ;
  `POST 'http://localhost:8080/recipes'` with body of type RecipeRequest
* Update recipe
  `PUT 'http://localhost:8080/recipes/{recipeId}'` with body of type RecipeRequest
* Delete recipe
  `DELETE 'http://localhost:8080/recipes/{recipeId}`
* Get by recipeId
  `GET 'http://localhost:8080/recipes/{recipeId}`
* Get all
  `GET 'http://localhost:8080/recipes`