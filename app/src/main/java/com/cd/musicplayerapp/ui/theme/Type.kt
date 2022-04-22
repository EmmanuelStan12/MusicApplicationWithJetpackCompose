package com.cd.musicplayerapp.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.cd.musicplayerapp.R

val BBFontFamily = FontFamily(
    Font(
        R.font.sourcesanspro_regular,
        FontWeight.Normal,
        FontStyle.Normal
    ),
    Font(
        R.font.sourcesanspro_bold,
        FontWeight.Bold,
        FontStyle.Normal
    ),
    Font(
        R.font.sourcesanspro_semibold,
        FontWeight.SemiBold,
        FontStyle.Normal
    ),
    Font(
        R.font.sourcesanspro_italic,
        FontWeight.Normal,
        FontStyle.Italic
    ),
)

// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = BBFontFamily,
        fontSize = 18.sp,
        fontWeight = FontWeight.Normal,
        color = Light
    ),
    h3 = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 48.sp,
        color = Light,
        fontFamily = BBFontFamily
    ),
    h6 = TextStyle(
        fontFamily = BBFontFamily,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Light
    ),
    h5 = TextStyle(
        fontFamily = BBFontFamily,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = Light
    ),
    h4 = TextStyle(
        fontFamily = BBFontFamily,
        fontSize = 28.sp,
        fontWeight = FontWeight.SemiBold,
        color = Light
    ),
    h2 = TextStyle(
        fontFamily = BBFontFamily,
        fontSize = 35.sp,
        fontWeight = FontWeight.ExtraBold,
        color = Light
    ),
    button = TextStyle(
        fontFamily = BBFontFamily,
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold,
        color = Light
    ),
)