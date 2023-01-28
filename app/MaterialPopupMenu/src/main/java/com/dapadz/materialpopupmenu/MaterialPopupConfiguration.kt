package com.dapadz.materialpopupmenu


class MaterialPopupConfiguration private constructor(
    val useBlur : Boolean,
    val title : String?,
    val backgroundColor : Int?,
    var itemTextColor : Int? = null,
    val blurRadius : Float,
    val behindDimAmong : Float,
) {

    data class Builder(
        private var useBlur : Boolean = true,
        private var title : String? = null,
        private var backgroundColor : Int? = null,
        private var itemTextColor : Int? = null,
        private var blurRadius : Float = 50f,
        private var behindDimAmong : Float = 0.3f
    ) {

        fun blurEnable(isEnable : Boolean) = apply { useBlur = isEnable }
        fun setTitle(title : String) = apply { this.title = title }
        fun setBackgroundColor(color : Int) = apply { backgroundColor = color }
        fun setItemTextColor(color : Int) = apply { itemTextColor = color }
        fun setBlurRadius(radius : Float) = apply { blurRadius = radius }
        fun setBehindDimAmong(dim : Float) = apply { behindDimAmong = dim }

        fun build() = MaterialPopupConfiguration(
            useBlur,
            title,
            backgroundColor,
            itemTextColor,
            blurRadius,
            behindDimAmong
        )
    }
}