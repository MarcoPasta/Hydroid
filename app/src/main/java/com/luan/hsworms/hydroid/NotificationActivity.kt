package com.luan.hsworms.hydroid

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

/**
 * Notification Class-Model for systemnotifications.
 * Only holds a companion object for instantiation.
 */
class NotificationActivity  {

    /**
     * Private empty constructor so no object of this class can be instatiated
     */
    private constructor(){}

    /**
     * Companion object so the functions can be called without any objects.
     */
    companion object {

        /**
         * Builds notification, checks OS-Version to create a notification channel (if needed) and then shows the notification.
         *
         * @param allowNotification     Allows the user to activate notifications. If false this function will be skipped and no notification will be shown.
         * @param CHANNEL_ID
         * @param NOTIFICATION_ID
         * @param context               Fragment/ViewModel context needed for notification functions. Mostly used with requireContext()
         * @param smallIcon             The little icon displayed on the notification
         * @param contentTitle          The notification title
         * @param contentText           Description displayed on the notification
         * @param contentBigText        Allows more text in the description. Nullable variable because it's not always needed.
         * @param contentPriority       States the priority of the notification.
         * @param channelName
         * @param channelDescription
         * @param channelImportance
         */
        fun showNotification (
            allowNotification: Boolean,
            CHANNEL_ID: String,
            NOTIFICATION_ID: Int,
            context: Context,
            smallIcon: Int,
            contentTitle: CharSequence,
            contentText: CharSequence,
            contentBigText: CharSequence?,      // ? -> Parameter is nullable
            contentPriority: Int,
            channelName: String,
            channelDescription: String,
            channelImportance: Int
        )
        {

            if(!allowNotification)
                return

            // Build notification
            // apply allows us to specify more methods within a method
            val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, CHANNEL_ID).apply {
                setSmallIcon(smallIcon)
                setContentTitle(contentTitle)
                setContentText(contentText)
                priority = contentPriority
                contentBigText?.let {
                    setStyle(NotificationCompat.BigTextStyle().bigText(it))
                }
            }

            // Create channel (only needed if OS > Oreo/Android 8
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(CHANNEL_ID, channelName, channelImportance).apply {
                    description = channelDescription
                }
                val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }

            // Show notification
            NotificationManagerCompat.from(context).apply {
                notify(NOTIFICATION_ID, builder.build())
            }

        }
    }

}