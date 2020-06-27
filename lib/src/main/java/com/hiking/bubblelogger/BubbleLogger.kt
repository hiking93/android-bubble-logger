package com.hiking.bubblelogger

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.Dimension
import androidx.annotation.DrawableRes
import androidx.annotation.Keep
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.Person
import androidx.core.graphics.drawable.IconCompat
import com.hiking.bubblelogger.screen.BubbleActivity

/**
 * Main library class for bubble logger.
 */
@Keep
object BubbleLogger {

    /**
     * Add a log to bubble.
     *
     * @param context Context for the bubble notification.
     * @param title The title text of the og.
     * @param message The message text of the log.
     * @param notificationId The ID for the bubble notification.
     * @param notificationChannelId The channel ID for the bubble notification.
     * @param adaptiveBubbleIconResource
     * Adaptive icon resource for Android 11 (R, API 30) or later.
     * @param legacyBubbleIconResource Legacy icon resource for Android 10 (Q, API 29).
     * @param notificationIconResource Icon resource for bubble notification.
     */
    fun log(
        context: Context,
        title: CharSequence?,
        message: CharSequence,
        notificationId: Int,
        notificationChannelId: String,
        @Dimension(unit = Dimension.DP) desiredHeight: Int = 640,
        @DrawableRes adaptiveBubbleIconResource: Int = R.drawable.ic_bubble_adaptive,
        @DrawableRes legacyBubbleIconResource: Int = R.drawable.ic_bubble_legacy,
        @DrawableRes notificationIconResource: Int = R.drawable.ic_notification
    ) {
        BubbleDataHelper.log(title, message)
        val target = Intent(context, BubbleActivity::class.java)
        val bubbleIntent =
            PendingIntent.getActivity(context, 0, target, PendingIntent.FLAG_UPDATE_CURRENT)
        val bubbleIcon = IconCompat.createWithResource(
            context,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) adaptiveBubbleIconResource
            else legacyBubbleIconResource
        )
        val bubbleMetadata =
            NotificationCompat.BubbleMetadata.Builder()
                .setIcon(bubbleIcon)
                .setIntent(bubbleIntent)
                .setDesiredHeight(desiredHeight)
                .build()
        val notificationTitle = title ?: context.getString(R.string.bubble_logger)
        val bot = Person.Builder()
            .setIcon(bubbleIcon)
            .setBot(true)
            .setName(notificationTitle)
            .setImportant(true)
            .build()
        val builder = NotificationCompat.Builder(context, notificationChannelId)
            .setStyle(
                NotificationCompat.MessagingStyle(bot)
                    .setConversationTitle(notificationTitle)
                    .addMessage(
                        NotificationCompat.MessagingStyle.Message(
                            message, System.currentTimeMillis(), bot
                        )
                    )
            )
            .setContentTitle(notificationTitle)
            .setContentText(message)
            .setSmallIcon(notificationIconResource)
            .setBubbleMetadata(bubbleMetadata)
        NotificationManagerCompat.from(context).notify(notificationId, builder.build())
    }
}