package com.example.project1.models

import android.content.Context
import android.util.AttributeSet
import android.widget.Button

class SquareButton:androidx.appcompat.widget.AppCompatButton {

    constructor(context: Context?):super(context)
    constructor(context: Context?, attributeSet: AttributeSet?):super(context,attributeSet)
    constructor(context: Context?, attributeSet: AttributeSet?, defAttr:Int):super(context,attributeSet,defAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measuredWidth, measuredWidth)
    }
}