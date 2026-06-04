package com.ss.collegeP

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun FloatingBottomBar(
    isDark : Boolean,
    navController : NavHostController,
    currentRoute : String?,
    enabled : Boolean
){

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter
    ){
        Row(
            modifier = Modifier
                .padding(bottom = 20.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(
                    color =
                        if(isDark) Color(0xFF111318).copy(alpha = 0.95f)
                        else Color(0xFFFFFFFF)
                )
                .padding(horizontal = 18.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            items.forEach { item ->

                val selected = currentRoute == item.route
                val activeColor = Color(0xFF4CAF50)

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable(enabled = enabled){
                            if(currentRoute != item.route){
                                navController.navigate(item.route){
                                    popUpTo(BottomNavItem.Home.route)
                                    launchSingleTop = true
                                }
                            }
                        }
                        .padding(horizontal = 18.dp, vertical = 6.dp)
                ){
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = null,
                        tint = if(selected) activeColor else Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )

                    Text(
                        text = item.title,
                        fontSize = 11.sp,
                        color = if(selected) activeColor else Color.Gray
                    )
                }
            }
        }
    }
}