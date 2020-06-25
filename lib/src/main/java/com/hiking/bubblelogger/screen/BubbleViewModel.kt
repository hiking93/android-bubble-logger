package com.hiking.bubblelogger.screen

import androidx.lifecycle.ViewModel
import com.hiking.bubblelogger.BubbleDataHelper

internal class BubbleViewModel : ViewModel() {

    val logs = BubbleDataHelper.logs
}