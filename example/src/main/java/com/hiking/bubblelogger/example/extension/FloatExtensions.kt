package com.hiking.bubblelogger.example.extension

import android.content.Context
import kotlin.math.roundToInt

/**
 * Convert dp to pixels (float value).
 */
fun Float.dpToPx(context: Context) = this * context.resources.displayMetrics.density

/**
 * Convert dp to pixels and round to integer.
 */
fun Float.dpToPxSize(context: Context) = dpToPx(context).roundToInt()