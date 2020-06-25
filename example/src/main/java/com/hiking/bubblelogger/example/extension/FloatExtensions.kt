package com.hiking.bubblelogger.example.extension

import android.content.Context
import kotlin.math.roundToInt

fun Float.dpToPx(context: Context) = this * context.resources.displayMetrics.density

fun Float.dpToPxSize(context: Context) = dpToPx(context).roundToInt()