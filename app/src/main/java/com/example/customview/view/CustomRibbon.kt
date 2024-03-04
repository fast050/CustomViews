package com.example.customview.view

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import com.example.customview.R
import kotlin.math.sqrt

class CustomRibbon(context: Context, attributeSet: AttributeSet?) : View(context, attributeSet) {
    private var widthOfScreen = 0f
    private var heightOfScreen = 0f
    private var originX = 0f
    private var originY = 0f
    private var ribbonText = ""
    private var backgroundColor = 0
    private var ribbonTextSize = 0f


    init {

        val attributeSet = context.theme.obtainStyledAttributes(
            attributeSet, R.styleable.CustomRibbon, 0, 0
        )

        try {
            ribbonText = attributeSet.getString(R.styleable.CustomRibbon_ribbonText) ?: ""
            backgroundColor = attributeSet.getColor(
                R.styleable.CustomRibbon_backgroundColor,
                Color.parseColor("#FFD600")
            )
            ribbonTextSize = attributeSet.getDimension(R.styleable.CustomRibbon_ribbonTextSize, 0f)
        } finally {
            attributeSet.recycle()
        }

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        widthOfScreen = width.toFloat()
        heightOfScreen = height.toFloat()

        //define the shape of background
        val backgroundShapePath = Path().apply {
            moveTo(originX, originY)
            lineTo(widthOfScreen / 2, originY)
            lineTo(widthOfScreen, heightOfScreen / 2)
            lineTo(widthOfScreen, heightOfScreen)

            close()
        }

        //define the paint of the shape of the background
        val backgroundPaint = Paint().apply {
            color = backgroundColor
            style = Paint.Style.FILL
        }

        //define the path of where the text should be draw
        val textPath = Path().apply {
            moveTo(widthOfScreen / 6, originY)
            lineTo(widthOfScreen, heightOfScreen * 5 / 6)

            close()
        }
        //define the paint of the ribbon text
        val textPaint = Paint().apply {
            color = Color.BLACK
            style = Paint.Style.FILL
            typeface = Typeface.DEFAULT_BOLD
            textSize = if (ribbonTextSize == 0f) textRatio() else ribbonTextSize
            isSubpixelText = true
        }

        //get the text width
        val textWidth = textPaint.measureText(ribbonText)

        // Calculate the horizontal offset to center the text to draw the text in middle of the path
        val hOffset = (textPathLength() - textWidth) / 2

        //draw the path on canvas
        canvas.drawPath(backgroundShapePath, backgroundPaint)

        // draw the text
        canvas.drawTextOnPath(
            ribbonText,
            textPath,
            hOffset.dp().px(),
            0f,
            textPaint
        )
    }

    //this default value for text size
    private fun textRatio(): Float {
        val lengthOfDiagonal = textPathLength()
        return lengthOfDiagonal / (10.dp().px())
    }

    //to get the length of the diagonal text path
    private fun textPathLength(): Float {
        val widthOfTriangle = widthOfScreen - (widthOfScreen / 6)
        val heightOfTriangle = heightOfScreen - (heightOfScreen / 6)

        return sqrt(
            (widthOfTriangle * widthOfTriangle) +
                    (heightOfTriangle * heightOfTriangle)
        )
    }


    private fun Float.dp(): Float = this / Resources.getSystem().displayMetrics.density
    private fun Float.px(): Float = this * Resources.getSystem().displayMetrics.density
    private fun Int.dp(): Float = this / Resources.getSystem().displayMetrics.density
}