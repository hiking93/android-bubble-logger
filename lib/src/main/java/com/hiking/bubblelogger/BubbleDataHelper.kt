package com.hiking.bubblelogger

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

internal object BubbleDataHelper {

    data class Log(
        val id: Long,
        val title: CharSequence?,
        val message: CharSequence,
        val timestamp: Long
    )

    val logs = MutableLiveData<List<Log>?>()

    fun log(title: CharSequence?, message: CharSequence) {
        GlobalScope.launch(Dispatchers.Main) {
            val log = Log(
                id = System.nanoTime(),
                title = title,
                message = message,
                timestamp = System.currentTimeMillis()
            )
            logs.apply { value = (value?.let { it + log } ?: listOf(log)) }
        }
    }

    fun clearLogs() {
        logs.value = null
    }
}