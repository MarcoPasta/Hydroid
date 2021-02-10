package com.luan.hsworms.hydroid.backend.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.luan.hsworms.hydroid.MainActivity

/**
 * Debugging tag for Logcat
 */
private const val TAG = "NotificationActivity"

/**
 * Notification Class-Model for systemnotifications.
 * Only holds a companion object for instantiation.
 */
class NotificationActivity  {
    /**
     * Private empty constructor so no object of this class can be instatiated
     */
    // private constructor(){}

    /**
     * Companion object so the functions can be called without any objects.
     */
    companion object {
        /**
         * Builds notification, checks OS-Version to create a notification channel (if needed) and then shows the notification.
         *
         * @param allowNotification     Allows the user to activate notifications. If false this function will be skipped and no notification will be shown.
         * @param CHANNEL_ID            ID provided to the channel that is needed for notifications. The user can change the channels behavior in the Android settings.
         * @param NOTIFICATION_ID       ID provided to each notification so they differ. Notifications from the same channel get overwritten.
         * @param context               context provided from the activity this function is called from.
         * @param smallIcon             Small icon that appears in the top bar.
         * @param contentTitle          Notification title.
         * @param contentText           Notifcations description.
         * @param contentBigText        More specified and bigger notification description.
         * @param contentPriority       Sets the priority for the notification. The higher the priority, the higher the chance for the user to see it.
         */
        fun goalAchievedNotification (
            allowNotification: Boolean,
            CHANNEL_ID: String,
            NOTIFICATION_ID: Int,
            context: Context,
            smallIcon: Int,
            contentTitle: CharSequence,
            contentText: CharSequence,
            contentBigText: CharSequence?,      // ? -> Parameter is nullable
            contentPriority: Int
        ) {

            if(!allowNotification)
                return

            // val intent: Intent? = AlarmService.setNewIntent(context)
            val intent: Intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

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
                setContentIntent(pendingIntent)
                setAutoCancel(true)
                setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
            }

            // Create channel (only needed if OS > Oreo/Android 8
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channelName = "Notifications"
                val channelDescription = "Standard Notification System"
                val channelImportance: Int = NotificationManager.IMPORTANCE_DEFAULT
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

        /**
         * Builds a notification specifically for the repeating helpDrink function.
         *
         * @param CHANNEL_ID                ID provided to the channel that is needed for notifications. The user can change the channels behavior in the Android settings.
         * @param NOTIFICATION_ID           ID provided to each notification so they differ. Notifications from the same channel get overwritten.
         * @param context                   context provided from the activity this function is called from.
         * @param smallIcon                 Small icon that appears in the top bar.
         * @param contentTitle              Notification title.
         * @param contentText               Notifcations description.
         * @param contentBigText            More specified and bigger notification description.
         * @param contentPriority           Sets the priority for the notification. The higher the priority, the higher the chance for the user to see it.
         */
        fun helpDrinkNotification(
            CHANNEL_ID: String,
            NOTIFICATION_ID: Int,
            context: Context,
            smallIcon: Int,
            contentTitle: CharSequence,
            contentText: CharSequence,
            contentBigText: CharSequence?,      // ? -> Parameter is nullable
            contentPriority: Int
        ) {

            // val intent: Intent? = AlarmService.setNewIntent(context)
            val intent: Intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

            // Build notification
            // apply allows us to specify more methods within a method
            Log.d(TAG, "Building Notification...")
            val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, CHANNEL_ID).apply {
                setSmallIcon(smallIcon)
                setContentTitle(contentTitle)
                setContentText(contentText)
                priority = contentPriority
                contentBigText?.let {
                    setStyle(NotificationCompat.BigTextStyle().bigText(it))
                }
                setContentIntent(pendingIntent)
                setAutoCancel(true)
            }

            // Create channel (only needed if OS > Oreo/Android 8
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channelName = "AlarmManager"
                val channelDescription = "AlarmManager Notification System"
                val channelImportance: Int = NotificationManager.IMPORTANCE_DEFAULT
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