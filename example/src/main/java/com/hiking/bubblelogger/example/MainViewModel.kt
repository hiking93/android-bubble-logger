package com.hiking.bubblelogger.example

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * [ViewModel] for [MainActivity].
 */
class MainViewModel : ViewModel() {

    var demoLogListener: ((title: String, message: String) -> Unit)? = null

    private var demoJob: Job? = null

    /**
     * Update demo mode state.
     *
     * @param context Context for resources.
     * @param isEnabled Set demo mode enabled or not.
     */
    fun updateDemoMode(context: Context, isEnabled: Boolean) {
        demoJob?.cancel()
        if (!isEnabled) return
        val loremText = context.getString(R.string.lorem_paragraph)
        demoJob = viewModelScope.launch {
            while (true) {
                demoLogListener?.invoke(
                    generateLoremTextClip(loremText, maxLength = 10),
                    generateLoremTextClip(loremText)
                )
                delay((300L..1000L).random())
            }
        }
    }

    private fun generateLoremTextClip(
        loremText: String,
        maxLength: Int? = null
    ): String {
        val startIndex = (0..loremText.length / 2).random()
        val randomRange =
            (startIndex + 10)..(maxLength?.let { startIndex + 10 + it } ?: loremText.length)
        val endIndex = randomRange.random()
        return loremText.substring(startIndex, endIndex).trim()
    }
}