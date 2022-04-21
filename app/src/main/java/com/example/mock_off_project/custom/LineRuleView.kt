package com.example.mock_off_project.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class LineRuleView : View {

    private var paintline: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    constructor(context: Context) : super(context) {

    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {

    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        paintline.color = Color.WHITE
        paintline.strokeWidth = barWidth
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val nMiddle = height / 2
        for (i in 0..width) {
            val x = i * increment
            if (i % 4 == 0)
                canvas?.drawLine(x, nMiddle - barBig, x, nMiddle + barBig, paintline)
            else
                canvas?.drawLine(x, nMiddle - barSmall, x, nMiddle + barSmall, paintline)
        }
    }

    companion object {
        const val barWidth = 7.0f
        const val barBig = 12.0f
        const val barSmall = 5.0f
        const val increment = 50f
    }
}