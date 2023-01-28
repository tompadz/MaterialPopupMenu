package com.dapadz.materialpopupmenu

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Outline
import android.util.TypedValue
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.ImageView
import androidx.core.widget.ImageViewCompat

class Utils {
    companion object {

        private var density = 1f

        fun checkDisplaySize(context : Context) {
            density = context.resources.displayMetrics.density
        }

        /**
         * Primitive extensions
         */

        fun Float.dp() : Int {
            return (this * density).toInt()
        }

        fun Float.dpf() : Float {
            return this * density
        }

        /**
         * View extensions
         */

        fun View.setCornerRadiusOfView(radius:Float = 30f) {
            outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view : View, outline : Outline) {
                    outline.setRoundRect(0, 0, view.width, view.height, radius)
                }
            }
            clipToOutline = true
        }

        fun View.getWidthOfView() : Int {
            measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            return measuredWidth
        }

        fun View.getHeightOfView() : Int {
            measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            return measuredHeight
        }

        /**
         * Context extensions
         */

        fun Context.getTypeColor(colorResId : Int) : Int {
            val typedValue = TypedValue()
            val typedArray = obtainStyledAttributes(typedValue.data, intArrayOf(colorResId))
            val color = typedArray.getColor(0, 0)
            typedArray.recycle()
            return color
        }

        /**
         * AnyView extensions
         */

        fun ImageView.setTint(colors: ColorStateList) {
            ImageViewCompat.setImageTintList(this, colors)
        }
    }
}