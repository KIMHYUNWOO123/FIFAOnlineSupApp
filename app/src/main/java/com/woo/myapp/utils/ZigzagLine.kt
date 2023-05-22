package com.woo.myapp.utils

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.woo.myapp.R

@Composable
fun ZigzagLine() {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.01f)
            .background(color = colorResource(id = R.color.app_color2))
    ) {
        val lineWidth = 2.dp.toPx()
        val lineColor = Color.White
        val lineSpacing = 8.dp.toPx()

        var startX = 0f
        var startY = 8.dp.toPx()
        var endX = 8.dp.toPx()
        var endY = 0f
        var temp = 0f

        while (startX < size.width) {
            drawLine(
                color = lineColor,
                strokeWidth = lineWidth,
                start = Offset(startX, startY),
                end = Offset(endX, endY)
            )

            startX = endX
            endX += lineSpacing
            temp = startY
            startY = endY
            endY = temp
        }
    }
}