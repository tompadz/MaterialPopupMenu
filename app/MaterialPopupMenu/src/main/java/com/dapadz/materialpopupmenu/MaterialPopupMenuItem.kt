package com.dapadz.materialpopupmenu

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import com.dapadz.materialpopupmenu.Utils.Companion.dp
import com.dapadz.materialpopupmenu.Utils.Companion.setTint

@Suppress("PrivatePropertyName")
@SuppressLint("ViewConstructor")
class MaterialPopupMenuItem(context:Context, color:Int) : LinearLayout(context, null) {

    private val VERTICAL_PADDING = 12f.dp()
    private val HORIZONTAL_PADDING = 18f.dp()

    private val ALPHA = 0.7f
    private val ICON_SIZE = 24f.dp()
    private val COLOR = color

    private lateinit var titleView : TextView
    private lateinit var iconView : ImageView

    init {
        initRootView()
        createTitleView()
        createIconView()
    }

    private fun initRootView() {
        setPadding(
            HORIZONTAL_PADDING,
            VERTICAL_PADDING,
            HORIZONTAL_PADDING,
            VERTICAL_PADDING
        )
        gravity = Gravity.CENTER_VERTICAL
        val outValue = TypedValue()
        context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
        setBackgroundResource(outValue.resourceId)
    }

    private fun createTitleView() {
        titleView = TextView(context).apply {
            layoutParams = LayoutParams(
                WRAP_CONTENT,
                WRAP_CONTENT
            )
            setTextColor(COLOR)
            textSize = 18f
            alpha = ALPHA
            maxLines = 1
        }
        addView(titleView)
    }

    private fun createIconView() {
        iconView = ImageView(context).apply {
            layoutParams = LayoutParams(
                ICON_SIZE,ICON_SIZE
            ).apply {
                setMargins(0,0, 16f.dp(), 0)
            }
            setTint(titleView.textColors)
            isVisible = false
            alpha = ALPHA
        }
        addView(iconView, 0)
    }

    var itemId = -1

    var title = "item"
        set(value) {
            field = value
            titleView.text = title
        }

    var icon : Drawable? = null
        set(value) {
            field = value
            iconView.isVisible = value != null
            iconView.setImageDrawable(value)
        }

}