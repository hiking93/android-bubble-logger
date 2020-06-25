package com.hiking.bubblelogger.extension

import android.content.Context
import kotlin.math.roundToInt

internal fun Float.dpToPx(context: Context) = this * context.resources.displayMetrics.density

internal fun Float.dpToPxSize(context: Context) = dpToPx(context).roundToInt()