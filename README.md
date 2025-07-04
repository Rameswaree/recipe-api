# recipe-api

## Overview
Create a standalone java application which allows users to manage their favourite recipes. It should
allow adding, updating, removing and fetching recipes. Additionally, users should be able to filter
available recipes based on one or more of the following criteria:
1. Whether the dish is vegetarian or not
2. The number of servings
3. Specific ingredients (either include or exclude)
4. Text search within the instructions.
   For example, the API should be able to handle the following search requests:
   • All vegetarian recipes
   • Recipes that can serve 4 persons and have “potatoes” as an ingredient
   • Recipes without “salmon” as an ingredient that has “oven” in the instructions.

## How to run the project
1. Clone the repository:
   ```bash
   git clone https://github.com/Rameswaree/recipe-api.git
    ```
2. Run the command to build the project:
   ```bash
   mvn clean install
   ```
3. Run the below command to start the instances of the application and the database:
   ```bash
    docker-compose up --build
    ```
4. Test the APIs given in the Postman collection within the project using Postman.

## Additional validations built into the APIs
- The APIs are designed to handle invalid input, returning appropriate error messages.
- The APIs ensure that the number of servings is a positive integer and has a minimum value of 1.
- When filtering recipes based on ingredients, the API checks for valid ingredient names and returns an error if any invalid or duplicate ingredient is provided.
- The text search within instructions is case-insensitive.
- Postman collection is provided to test the APIs.

## To-do (Beyond MVP functionality)
- Implement caching for frequently accessed recipes to improve performance.
- Include OpenAPI documentation for better API discoverability.
- Provide pagination for the list of recipes to handle large datasets.