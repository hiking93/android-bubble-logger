package com.hiking.bubblelogger.example

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.hiking.bubblelogger.BubbleLogger
import com.hiking.bubblelogger.example.extension.applyEdgeToEdge
import com.hiking.bubblelogger.example.extension.dpToPxSize
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Homepage of the example app.
 */
class MainActivity : AppCompatActivity() {

    companion object {
        const val CHANNEL_ID_BUBBLE_LOGGER = "CHANNEL_ID_BUBBLE_LOGGER"
        const val NOTIFICATION_ID = 8000
    }

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupWindow()
        setupViews()
        setupViewModel()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            createNotificationChannel()
        }
    }

    override fun onDestroy() {
        viewModel.demoLogListener = null
        super.onDestroy()
    }

    private fun setupWindow() {
        applyEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(rootLayout) { v, insets ->
            rootLayout.updatePadding(
                left = insets.systemWindowInsetLeft,
                right = insets.systemWindowInsetRight
            )
            contentLayout.updatePadding(
                bottom = insets.systemWindowInsetBottom + 16f.dpToPxSize(v.context)
            )
            WindowInsetsCompat.Builder(insets)
                .setSystemWindowInsets(Insets.of(0, insets.systemWindowInsetTop, 0, 0))
                .build()
        }
    }

    private fun setupViews() {
        demoSwitch.setOnCheckedChangeListener { v, isChecked ->
            viewModel.updateDemoMode(v.context, isChecked)
        }
        sendButton.setOnClickListener { logMessageFromEditTexts() }
        messageEditText.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEND -> {
                    logMessageFromEditTexts()
                    true
                }
                else -> false
            }
        }
    }

    private fun setupViewModel() {
        viewModel.demoLogListener = { title, message -> logMessage(title, message) }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun createNotificationChannel() {
        NotificationManagerCompat.from(this).createNotificationChannel(
            NotificationChannel(
                CHANNEL_ID_BUBBLE_LOGGER,
                getString(R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                setAllowBubbles(true)
                setSound(null, null)
                enableLights(false)
                enableVibration(false)
                description = getString(R.string.notification_channel_description)
            }
        )
    }

    private fun logMessageFromEditTexts() {
        val title = titleEditText.text?.toString()
        val message = messageEditText.text?.toString() ?: return
        logMessage(title, message)
    }

    private fun logMessage(title: CharSequence?, message: CharSequence) {
        if (customizedStyleSwitch.isChecked) {
            BubbleLogger.log(
                context = this,
                title = title,
                message = message,
                notificationId = NOTIFICATION_ID,
                notificationChannelId = CHANNEL_ID_BUBBLE_LOGGER,
                desiredHeight = 360,
                adaptiveBubbleIconResource = R.drawable.ic_bubble_adaptive_customized,
                legacyBubbleIconResource = R.drawable.ic_bubble_legacy_customized,
                notificationIconResource = R.drawable.ic_notification_customized
            )
        } else {
            BubbleLogger.log(
                context = this,
                title = title,
                message = message,
                notificationId = NOTIFICATION_ID,
                notificationChannelId = CHANNEL_ID_BUBBLE_LOGGER
            )
        }
    }
}
