package com.siewtheng.littlelemon.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.siewtheng.littlelemon.R

val karla = FontFamily(Font(R.font.karla_regular))
val markazi = FontFamily(Font(R.font.markazi_medium))

val customTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = markazi,
        fontWeight = FontWeight.Bold,
        fontSize = 48.sp
    ),

    displayMedium = TextStyle(
        fontFamily = markazi,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp
    ),

    headlineSmall = TextStyle(
        fontFamily = karla,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 20.sp
    ),

    bodyMedium = TextStyle(
        fontFamily = karla,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),

)