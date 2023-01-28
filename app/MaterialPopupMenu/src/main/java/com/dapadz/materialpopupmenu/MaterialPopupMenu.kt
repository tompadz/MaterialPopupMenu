package com.dapadz.materialpopupmenu

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.*
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.*
import android.widget.LinearLayout.TEXT_ALIGNMENT_CENTER
import android.widget.LinearLayout.VERTICAL
import androidx.annotation.MenuRes
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.children
import androidx.core.view.setPadding
import androidx.core.view.updateLayoutParams
import com.dapadz.chatnote.ui.views.popup_menu.OnMenuClickListener
import com.dapadz.materialpopupmenu.Utils.Companion.dp
import com.dapadz.materialpopupmenu.Utils.Companion.dpf
import com.dapadz.materialpopupmenu.Utils.Companion.getHeightOfView
import com.dapadz.materialpopupmenu.Utils.Companion.getTypeColor
import com.dapadz.materialpopupmenu.Utils.Companion.getWidthOfView
import com.dapadz.materialpopupmenu.Utils.Companion.setCornerRadiusOfView
import com.github.mmin18.widget.RealtimeBlurView
import com.google.android.material.divider.MaterialDivider
import java.util.*

@Suppress("PrivatePropertyName")
class MaterialPopupMenu(
    val context : Context,
    val view : View,
    private val config : MaterialPopupConfiguration = MaterialPopupConfiguration.Builder().build(),
) : PopupWindow(context) {

    private val TAG = "MaterialPopupMenu"

    init {
        Utils.checkDisplaySize(context)
    }

    //view const
    private val MAX_WIDTH = 220f.dp()
    private val MIN_WIDTH = 112f.dp()
    private val ROOT_CORNER_RADIUS = 8f.dpf()

    //config const
    private val BACKGROUND_COLOR = config.backgroundColor
        ?: context.getTypeColor(com.google.android.material.R.attr.colorSurface)
    private val ITEMS_TEXT_COLOR = config.itemTextColor
        ?: context.getTypeColor(com.google.android.material.R.attr.colorOnSurface)
    private val BLUR_RADIUS = config.blurRadius
    private val DIM_AMONG = config.behindDimAmong
    private val BLUR_ENABLE = config.useBlur

    //views
    private lateinit var rootLayout : FrameLayout
    private lateinit var linearLayout : LinearLayout
    private lateinit var blurView : RealtimeBlurView
    private lateinit var titleView : TextView

    private var menuItemsMaxWidth = 0
    private var listener : OnMenuClickListener? = null

    init {
        initRootView()
        initRootLayout()
        initLinearLayout()
        initBlurView()
    }

    /**
     * Initializing the main views
     */

    private fun initRootView() {
        isFocusable = true
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.RECTANGLE
        shape.setColor(BACKGROUND_COLOR)
        val outerRadii = FloatArray(8)
        Arrays.fill(outerRadii, ROOT_CORNER_RADIUS)
        shape.cornerRadii = outerRadii
        setBackgroundDrawable(shape)
        isAttachedInDecor = true
        elevation = 5f
    }

    private fun initRootLayout() {
        rootLayout = FrameLayout(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                WRAP_CONTENT,
                WRAP_CONTENT
            )
            setCornerRadiusOfView(ROOT_CORNER_RADIUS)
        }
    }

    private fun initLinearLayout() {
        linearLayout = LinearLayout(context).apply {
            layoutParams = FrameLayout.LayoutParams(
                WRAP_CONTENT,
                WRAP_CONTENT
            )
            orientation = VERTICAL
            if (! BLUR_ENABLE) {
                background = BACKGROUND_COLOR.toDrawable()
            }
        }
        rootLayout.addView(linearLayout)
    }

    private fun initBlurView() {
        if (! BLUR_ENABLE) return
        val blurColor = BACKGROUND_COLOR.toDrawable()
        blurColor.alpha = 210
        blurView = RealtimeBlurView(context, null).apply {
            setBlurRadius(BLUR_RADIUS)
            setOverlayColor(blurColor.color)
        }
        rootLayout.addView(blurView, 0)
    }

    private fun initTitleView() {
        titleView = TextView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                MATCH_PARENT,
                WRAP_CONTENT,
            )
            alpha = 0.6f
            text = config.title !!
            textAlignment = TEXT_ALIGNMENT_CENTER
            setTextColor(ITEMS_TEXT_COLOR)
            setPadding(16f.dp())
        }
    }

    /**
     * Public functions
     */

    /**
     * Creates a menu and immediately shows
     * @param menuRes xml resource of menu
     */
    fun addMenuAndShow(@MenuRes menuRes : Int) {
        addMenu(menuRes)
        show()
    }

    /**
     * Creates a menu
     * @param menuRes xml resource of menu
     */

    fun addMenu(@MenuRes menuRes : Int) {
        createMenuItems(menuRes)
        if (config.title != null) createTitleView()
        updateBlurSize()
        contentView = rootLayout
    }

    fun show() {
        showAsDropDown(view)
        dimBehind()
    }

    fun setOnMenuItemClickListener(listener : OnMenuClickListener) {
        this.listener = listener
    }

    fun setOnMenuItemClickListener(click : (item : MenuItem) -> Unit) {
        this.listener = object : OnMenuClickListener {
            override fun onClick(item : MenuItem) {
                click(item)
            }
        }
    }

    /**
     * Private functions
     */

    private fun createMenuItems(@MenuRes menuRes : Int) {
        val menuItems = getMenuItems(menuRes)
        val menuSize = menuItems.size
        //create menu items
        menuItems.forEachIndexed { index, item ->
            val menuItem = createMenuItem(item)
            calculateSize(menuItem)
            linearLayout.addView(menuItem)
            if (index != menuSize - 1) {
                linearLayout.addView(
                    createDivider()
                )
            }
        }
        //update dividers width
        linearLayout.children.forEach {
            if (it is MaterialDivider) {
                it.updateLayoutParams {
                    width = menuItemsMaxWidth
                }
            }
        }
    }

    private fun createDivider() : MaterialDivider {
        return MaterialDivider(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                WRAP_CONTENT,
                WRAP_CONTENT
            ).apply {
                val startMargin = (16f.dp() * 2) + 24f.dp() //def padding * 2 + menu item icon size
                setMargins(startMargin, 0, 0, 0)
            }
        }
    }

    private fun createMenuItem(item : MenuItem) : MaterialPopupMenuItem {
        return MaterialPopupMenuItem(context, ITEMS_TEXT_COLOR).apply {
            title = item.title.toString()
            icon = item.icon
            itemId = item.itemId
            setOnClickListener {
                listener?.onClick(item)
                dismiss()
            }
        }
    }

    private fun createTitleView() {
        linearLayout.apply {
            initTitleView()
            addView(MaterialDivider(context), 0)
            addView(titleView, 0)
        }
    }

    private fun calculateSize(item : MaterialPopupMenuItem) {
        // Not a perfect solution,
        // needed for the correct size of MaterialDividers,
        // otherwise they will be the full width of the page
        val itemWidth = item.getWidthOfView()
        if (itemWidth > menuItemsMaxWidth) {
            menuItemsMaxWidth = when {
                itemWidth > MAX_WIDTH -> MAX_WIDTH
                itemWidth < MIN_WIDTH -> MIN_WIDTH
                else -> itemWidth
            }
        }
    }

    private fun getMenuItems(@MenuRes menuRes : Int) : List<MenuItem> {
        val menuItems = mutableListOf<MenuItem>()
        val menu = PopupMenu(context, null).menu
        MenuInflater(context).inflate(menuRes, menu)
        for (i in 0 until menu.size()) {
            val item : MenuItem = menu.getItem(i)
            Log.e(TAG, item.groupId.toString())
            menuItems.add(item)
        }
        return menuItems
    }

    private fun updateBlurSize() {
        if (! BLUR_ENABLE) return
        blurView.updateLayoutParams {
            width = linearLayout.getWidthOfView()
            height = linearLayout.getHeightOfView()
        }
    }

    private fun dimBehind() {
        val container = contentView.rootView
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val p = container.layoutParams as WindowManager.LayoutParams
        p.flags = p.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
        p.dimAmount = DIM_AMONG
        wm.updateViewLayout(container, p)
    }

    /**
     * Override functions
     */

    override fun showAsDropDown(anchor : View?) {
        if (anchor == null) return
        val anchorLeft = anchor.left
        val width = - (contentView.getWidthOfView() / 2) + (anchor.width / 2)
        if (width < anchorLeft) {
            super.showAsDropDown(anchor, width, 10f.dp())
        } else {
            super.showAsDropDown(anchor, 0, 10f.dp())
        }
    }
}
