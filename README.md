# Recipes

This is standalone java application which allows users to manage their favourite recipes.
It allows adding, updating, removing and fetching recipes.
Available recipes can be filtered based on one or more of the following criteria:

1. Whether or not the dish is vegetarian
2. The number of servings
3. Specific ingredients (either include or exclude)
4. Text search within the instructions.

Example of search request:

* All vegetarian recipes
* Recipes that can serve 4 persons and have “potatoes” as an ingredient
* Recipes without “salmon” as an ingredient that has “oven” in the instructions.

### Build

1. Run **./mvnw clean package**. This will create the executable .jar inside the target folder.
2. Run **java -jar target/recipes-challenge-1.0.0.jar** to start app the application.
3. Once the application is up, the API will be accessible under http://localhost:8080/recipes

**TBC**