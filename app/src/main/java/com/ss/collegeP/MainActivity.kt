package com.ss.collegeP

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.ss.collegeP.ui.theme.RecipeBookTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val view = LocalView.current
            val window = (view.context as Activity).window
            val dark = isSystemInDarkTheme()

            val navController = rememberNavController()
            RecipeBookTheme{

//                Enabled edge to edge mode for the window
                WindowCompat.setDecorFitsSystemWindows(window,false)

//                Bar background color
//                window.statusBarColor = Color.Transparent.toArgb()
//                window.navigationBarColor = Color.Transparent.toArgb()


//                System Icon color
                WindowCompat.getInsetsController(window,view).apply {
                    isAppearanceLightStatusBars = !dark
                    isAppearanceLightNavigationBars = !dark
                }

                Surface(
                    modifier = Modifier.fillMaxSize()
                        .background(if(dark)Color.Black else Color.White )
                        .statusBarsPadding()
                        .navigationBarsPadding()
                ){
                    recipeApp(
                        isDark = dark,
                        navController = navController
                    )
                }
            }
        }
    }
}
