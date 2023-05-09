package com.example.fitnessproject.widget

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView

class DividerItemDecoratorAll(private val divider: Drawable?) : RecyclerView.ItemDecoration() {

    private val mBounds = Rect()
    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        canvas.save()
        val left: Int
        val right: Int
        if (parent.clipToPadding) {
            left = parent.paddingLeft
            right = parent.width - parent.paddingRight
        } else {
            left = 0
            right = parent.width
        }
        val childCount = parent.childCount
        val heightDivider = divider?.intrinsicHeight ?: 0
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            if (i == 0) {
                divider?.setBounds(left, 0, right, heightDivider)
                divider?.draw(canvas)
                parent.getDecoratedBoundsWithMargins(child, mBounds)
                val bottom: Int = mBounds.bottom + Math.round(child.translationY)
                val top: Int = bottom - heightDivider
                divider?.setBounds(left, top, right, bottom)
                divider?.draw(canvas)
            } else {
                parent.getDecoratedBoundsWithMargins(child, mBounds)
                val bottom: Int = mBounds.bottom + Math.round(child.translationY)
                val top: Int = bottom - heightDivider
                divider?.setBounds(left, top, right, bottom)
                divider?.draw(canvas)
            }
        }
        canvas.restore()
    }
}