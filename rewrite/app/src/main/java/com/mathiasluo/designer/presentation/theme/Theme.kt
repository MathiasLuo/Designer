package com.mathiasluo.designer.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val BrandBlue = Color(0xFF0027FF)
val BrandPink = Color(0xFFFF4081)
val LinkBlue = Color(0xFF3A8BBB)
val IconGray = Color(0xFFA4A4A4)
val PageBackground = Color(0xFFF3F3F3)
val DividerColor = Color(0xFFEDEDED)

private val LightColors = lightColorScheme(
    primary = BrandBlue,
    onPrimary = Color.White,
    secondary = BrandPink,
    background = PageBackground,
    onBackground = Color(0xFF333333),
    surface = Color.White,
    onSurface = Color(0xFF333333),
)

@Composable
fun DesignerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        content = content,
    )
}
