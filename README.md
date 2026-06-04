# RecipeBook 🍽️

RecipeBook is a modern Android recipe application built using Kotlin and Jetpack Compose.
The app allows users to discover recipes, browse categories, search recipes, view detailed cooking instructions, nutritional information, and save favorite recipes for offline access.

## Features

### Home Screen
- Browse recipes from Spoonacular API
- Beautiful recipe cards with images
- Loading states

### Search
- Search recipes by name
- Instant filtering results

### Categories
- Browse recipes by category
- Dedicated category recipe screen

### Recipe Details
- Large recipe image
- Ingredients list
- Cooking instructions
- Preparation time
- Meal type information
- Nutrition information

### Nutrition Information
- Calories
- Protein
- Carbohydrates
- Fat

### Favorites
- Save recipes locally
- Remove recipes from favorites
- Offline access using Room Database

### Data Handling
- Fetches recipes from Spoonacular API
- Handles missing or invalid recipe images gracefully
- Displays loading states during network requests
- Processes API responses before displaying data

## Tech Stack

### Language
- Kotlin

### UI
- Jetpack Compose
- Material Design

### Architecture
- MVVM Architecture

### Networking
- Retrofit
- Gson

### Database
- Room Database

### Image Loading
- Coil

### API
- Spoonacular API

## Screenshots

### Home Screen
![Home Screen](RecipeBook_Home_Screen.jpeg)

### Loading State
![Loading State](RecipeBook_Loading_State.jpeg)

### Categories Screen
![Categories Screen](RecipeBook_Categories_Screen.jpeg)

### Category Recipes Screen
![Category Recipes](RecipeBook_Category_Detail_Screen.jpeg)

### Search Feature
![Search Feature](RecipeBook_Search_State.jpeg)

### Recipe Detail Screen
![Recipe Detail](RecipeBook_Detailed_Screen.jpeg)

### Recipe Instructions
![Instructions](RecipeBook_Detailed_Screen_Instructions.jpeg)

### Favorites Screen
![Favorites](RecipeBook_Favourite.jpeg)

## Project Structure

- UI Layer
- ViewModel Layer
- Repository Layer
- API Layer
- Database Layer

## Getting Started

1. Clone the repository

```bash
git clone https://github.com/SS007008/RecipeBook.git
```

2. Add your Spoonacular API key in local.properties

```properties
SPOONACULAR_API_KEY=YOUR_API_KEY
```

3. Sync Gradle

4. Run the application

## Future Improvements

- Dark/Light theme switch
- User accounts
- Recipe sharing
- Meal planner
- Offline recipe caching

## Author

Satyam Solanki

## License

Educational and portfolio project.
