package com.ss.collegeP

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SearchBar(
    query : String,
    onQueryChange : (String) -> Unit,
    onClear : () -> Unit,
    isDark : Boolean,
    placeholder : String,
    modifier : Modifier = Modifier
){
    val focusManager = LocalFocusManager.current

    val green = Color(0xFF4CAF50)

    val borderColor = if(isDark) Color.Gray.copy(alpha = 0.4f) else Color.Gray
    val textColor = if(isDark) Color.White else Color.Black
    val containerColor = if(isDark) Color.White.copy(alpha = 0.05f) else Color.Black.copy(alpha = 0.03f)
    val selectionColors = TextSelectionColors(
        handleColor = green,
        backgroundColor = green.copy(alpha = 0.4f)
    )

    CompositionLocalProvider(
        LocalTextSelectionColors provides selectionColors
    ){
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = {
                Text(
                    text = placeholder,
                    color = Color.Gray,
                    fontSize = 14.sp//
                )
            },
            textStyle = LocalTextStyle.current.copy(fontSize = 14.sp),//
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = if(query.isNotEmpty()) green else Color.Gray
                )
            },
            trailingIcon = {
                if(query.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            onClear()
                            focusManager.clearFocus()
                        }
                    ){
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(28.dp),
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = 44.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = green,
                unfocusedBorderColor = borderColor,
                cursorColor = green,
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                focusedContainerColor = containerColor,
                unfocusedContainerColor = containerColor,
            )
        )
    }
}