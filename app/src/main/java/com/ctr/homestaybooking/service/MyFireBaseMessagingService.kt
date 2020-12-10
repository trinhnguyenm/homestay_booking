package jp.monedge.sonybank.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.data.model.ExtraData
import com.ctr.homestaybooking.util.SharedReferencesUtil
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import java.util.*
import kotlin.random.Random

class MyFireBaseMessagingService : FirebaseMessagingService() {

    companion object {
        private const val NOTIFICATION_ID = 888
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        p0.let {
            SharedReferencesUtil.setString(
                applicationContext,
                SharedReferencesUtil.KEY_DEVICE_TOKEN,
                it
            )
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        remoteMessage.data.let { data ->
            data["title"].apply { Log.d("--=", "title+${this}") }
            data["body"].apply { Log.d("--=", "body+${this}") }
            data["extra"].apply { Log.d("--=", "extra+${this}") }
            if (data["title"].isNullOrEmpty() || data["body"].isNullOrEmpty()) {
                remoteMessage.notification?.let {
                    sendNotification(it.title, it.body)
                }
            } else {
                sendNotification(data["title"], data["body"], data["extra"])
            }
        }
    }

    private fun sendNotification(title: String?, body: String?, extra: String? = null) {
        var bookingId: Int? = null
        extra?.let {
            bookingId = Gson().fromJson(it, ExtraData::class.java).bookingId
        }
        val url = if (bookingId != null) {
            "homestay://payment/$bookingId"
        } else {
            "homestay://payment/"
        }
        Log.d("--=", "sendNotification: ${url}")
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        val pendingIntent =
            PendingIntent.getActivities(this, 0, arrayOf(intent), PendingIntent.FLAG_UPDATE_CURRENT)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        val builder = NotificationCompat.Builder(this, NOTIFICATION_ID.toString())
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_ID.toString(),
                NOTIFICATION_ID.toString(),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                setShowBadge(false)
            }
            notificationManager?.createNotificationChannel(channel)
        }
        val notificationId =
            try {
                Calendar.getInstance().timeInMillis.toString().substring(4).toInt()
            } catch (e: Exception) {
                Random.nextInt(1, 999999999)
            }
        NotificationManagerCompat.from(this)
            .notify(notificationId, builder.build())
    }
}
