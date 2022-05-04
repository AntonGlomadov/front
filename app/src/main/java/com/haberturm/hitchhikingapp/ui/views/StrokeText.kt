package com.haberturm.hitchhikingapp.ui.views

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun StrokeText(
    text:String = "Выбирите начальную точку"
){
    val sizeOfText = 32f
    val textPaintStroke = Paint().asFrameworkPaint().apply {
        isAntiAlias = true
        style = android.graphics.Paint.Style.STROKE
        textSize = sizeOfText
        color = android.graphics.Color.BLACK
        strokeWidth = 6f
        strokeMiter= 5f
        strokeJoin = android.graphics.Paint.Join.ROUND
        textAlign = android.graphics.Paint.Align.CENTER
    }

    val textPaint = Paint().asFrameworkPaint().apply {
        isAntiAlias = true
        style = android.graphics.Paint.Style.FILL
        textSize = sizeOfText
        color = android.graphics.Color.WHITE
        textAlign = android.graphics.Paint.Align.CENTER
    }
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
        ,

    ){
        drawIntoCanvas {
            it.nativeCanvas.drawText(
                text,
                size.width/2f,
                20.dp.toPx(),
                textPaintStroke
            )
            it.nativeCanvas.drawText(
                text,
                size.width/2f,
                20.dp.toPx(),
                textPaint,
            )
        }
    }

}

@Composable
@Preview
fun StrokeTextPrev(){
    Row(
    ) {
        StrokeText()
    }
}

