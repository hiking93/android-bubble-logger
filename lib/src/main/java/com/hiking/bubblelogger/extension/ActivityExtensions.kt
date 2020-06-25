package com.hiking.bubblelogger.extension

import android.app.Activity
import android.view.View

fun Activity.applyEdgeToEdge() {
    window.decorView.apply {
        systemUiVisibility = systemUiVisibility or
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    }
}