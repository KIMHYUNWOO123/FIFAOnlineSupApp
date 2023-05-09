package com.example.myapp.utils

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Percentage(percentage: Int, color: Color) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(100.dp)
    ) {
        Canvas(modifier = Modifier.size(80.dp)) {
            val strokeWidth = 10f
            val strokeColor = Color.Gray
            val radius = size / 2f

            drawCircle(
                color = strokeColor,
                radius = radius.minDimension,
                style = Stroke(width = strokeWidth)
            )

            val sweep = percentage * 3.6f
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = sweep,
                useCenter = false,
                style = Stroke(width = strokeWidth)
            )
        }

        Text(text = "${(percentage)}%", fontSize = 16.sp)
    }
}