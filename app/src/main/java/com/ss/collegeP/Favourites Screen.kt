package com.ss.collegeP

import StorageForRecipes.RecipeViewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun FavouritesScreen(
    recipeViewModel: RecipeViewModel,
    navigateToDetailScreen : (Recipe) -> Unit) {

//    val favList = favViewModel.favourites.value
      val favList by recipeViewModel.favourites.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Favourites",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(12.dp))

        when {

            favList == null -> {
//                show loading -> show nothing or loader
            }

            favList!!.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    Text("No favourite recipes yet")
                }
            }

            else -> {

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    val list = favList!!
                    val rows = list.chunked(2)

                    rows.forEachIndexed { rowIndex, rowItems ->

                        // show 2 recipe cards
                        items(rowItems) { recipe ->

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { navigateToDetailScreen(recipe) }
                            ) {

                                AsyncImage(
                                    model = recipe.image,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(1f)
                                        .clip(RoundedCornerShape(18.dp)),
                                    contentScale = ContentScale.Crop
                                )

                                Text(
                                    text = recipe.name,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(top = 6.dp)
                                )

                                Text(
                                    text = "⏱ ${recipe.readyInMinutes} min",
                                    fontSize = 13.sp,
                                    color = Color.Gray
                                )
                            }
                        }

                        // 👇 full width divider AFTER each row (not last)
                        if (rowIndex != rows.lastIndex) {
                            item(span = { GridItemSpan(2) }) {
                                HorizontalDivider(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 14.dp),
                                    thickness = 1.dp,
                                    color = Color.Gray.copy(alpha = 0.35f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}