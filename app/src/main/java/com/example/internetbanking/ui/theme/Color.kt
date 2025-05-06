package com.example.internetbanking.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Default
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

// Application
val custom_light_green1 = Color(0xFF5CA347)
val custom_light_green2 = Color(0xFF61C41D)
val custom_dark_green = Color(0xFF017145)
val custom_mint_green = Color(0XFFD7F9F1)
val custom_light_blue = Color(0xFF4D9DE0)
val custom_dark_blue = Color(0xFF3581B8)
val custom_light_yellow = Color(0xFFF5CB5C)
val custom_dark_yellow = Color(0xFFD5A021)
val custom_cream_white = Color(0xFFF1FFFA)
val custom_light_red = Color(0xFFBA274A)
val custom_dark_red = Color(0xFF841C26)

// Custom Gradient Color
object GradientColors {
    val GreenDarkToLight = Brush.linearGradient(
        colors = listOf(custom_dark_green, custom_light_green1, custom_light_green2)
    )
    val GreenLightToDark = Brush.linearGradient(
        colors = listOf(custom_light_green2, custom_light_green1, custom_dark_green)
    )
    val GreenRipple = Brush.linearGradient(
        colors = listOf(
            custom_dark_green, custom_dark_green, custom_light_green1,
            custom_dark_green, custom_dark_green, custom_light_green1
        )
    )
}
