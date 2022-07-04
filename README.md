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

#### Run application steps

1. Run **./mvnw clean package**. This will create the executable .jar inside the target folder.
2. Run **java -jar target/recipes-challenge-1.0.0.jar** to start app the application.
3. Once the application is up, the API will be accessible under http://localhost:8080/recipes

#### APIs design specifications

Current APIs does not accept pagination or sorting, and they don't return the hypermedia links that will inform what
operations are available for each resource and how to access that information. (further improvements + cleanup).
<p>
Example of postman collection can be found in `src/main/resources/Recipes.postman_collection.json`

* **Create recipe**: ;
  `POST 'http://localhost:8080/recipes'` api also requires RecipeRequest in the body
* **Update recipe**
  `PUT 'http://localhost:8080/recipes/{recipeId}'` where recipeId is the id of an existing recipe; api also requires
  RecipeRequest in the body
* **Delete recipe**
  `DELETE 'http://localhost:8080/recipes/{recipeId}` where recipeId is the id of an existing recipe
* **Get by recipeId**
  `GET 'http://localhost:8080/recipes/{recipeId}` where recipeId is the id of an existing recipe
* **Get all**
  `GET 'http://localhost:8080/recipes` accepts following optional filters:
    * type: one of: STANDARD, PESCETERIAN, LACTO_VEGETARIAN, OVO_VEGETARIAN, VEGETARIAN, VEGAN
    * servings: of type int
    * include-ingredients: list of strings
    * exclude-ingredients: list of strings
    * instruction-words: string

**RecipeRequest** has the following structure:

|      Field      |        Type        |     Description      |
|:---------------:|:------------------:|:--------------------:|
|     `name`      |       String       |     Recipe name      |
|     `type`      |     RecipeType     |    Type of recipe    |
|   `servings`    |      Integer       |  Number of servings  |
|  `ingredients`  | MeasuredIngredient | List of ingredients  |
| `instructions ` |    List<String>    | List of instructions |

**RecipeType** is one of:   STANDARD, PESCETERIAN, LACTO_VEGETARIAN, OVO_VEGETARIAN, VEGETARIAN, VEGAN

**MeasuredIngredient** has the following structure:

|      Field      |             Type              |       Description       |
|:---------------:|:-----------------------------:|:-----------------------:|
|     `name`      |            String             |  Ingredient name        |
|     `unit`      |      MeasurementUnitType      |        Unit type        |
|    `value`      |            Integer            |      Double value       |

**RecipeType** is one of:  GRAMS, MILLILITERS, PIECES

**RecipeResponse** follows same structure as the request but it also contains the id of the returned request