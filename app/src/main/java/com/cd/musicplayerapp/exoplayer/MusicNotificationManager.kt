package com.cd.musicplayerapp.exoplayer

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.cd.musicplayerapp.R
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import timber.log.Timber

class MusicNotificationManager(
    private val context: Context,
    sessionToken: MediaSessionCompat.Token,
    notificationListener: PlayerNotificationManager.NotificationListener,
    private val onMusicChanged: () -> Unit
) {

    private val mediaController = MediaControllerCompat(context, sessionToken)
    private val notificationManager: PlayerNotificationManager = PlayerNotificationManager.Builder(
        context,
        NOTIFICATION_ID,
        NOTIFICATION_CHANNEL_ID
    ).apply {
        setChannelNameResourceId(R.string.notification_channel_name)
        setChannelDescriptionResourceId(R.string.notification_channel_description)
        setMediaDescriptionAdapter(DescriptionAdapter(mediaController))
        setNotificationListener(notificationListener)
        setCustomActionReceiver(object: PlayerNotificationManager.CustomActionReceiver{
            override fun createCustomActions(
                context: Context,
                instanceId: Int
            ): MutableMap<String, NotificationCompat.Action> {
                val pendingIntent = Intent(context, Receiver::class.java).apply {
                    action = QUIT_ACTION
                }.let {
                    PendingIntent.getBroadcast(context, 0, it, 0)
                }
                return mutableMapOf<String, NotificationCompat.Action>(
                    "cancel" to NotificationCompat.Action(R.drawable.ic_cancel, "Cancel", pendingIntent)
                )
            }

            override fun getCustomActions(player: Player): MutableList<String> {
                return mutableListOf("cancel")
            }

            override fun onCustomAction(player: Player, action: String, intent: Intent) {
                when(intent.action) {
                    QUIT_ACTION -> {
                        Timber.d("cancel clicked")
                    }
                }
            }
        })
    }.build()
        .apply {
            setSmallIcon(R.drawable.ic_music_note)
            setMediaSessionToken(sessionToken)
        }

    fun showNotification(player: Player) {
        notificationManager.setPlayer(player)
    }

    fun hideNotification() {
        notificationManager.setPlayer(null)
    }

    private inner class DescriptionAdapter(
        private val mediaController: MediaControllerCompat
    ): PlayerNotificationManager.MediaDescriptionAdapter {
        override fun getCurrentContentTitle(player: Player): CharSequence {
            onMusicChanged()
            return mediaController.metadata.description.title.toString()
        }

        override fun createCurrentContentIntent(player: Player): PendingIntent? {
            return mediaController.sessionActivity
        }

        override fun getCurrentContentText(player: Player): CharSequence? {
            Timber.d("getting description content ${mediaController.metadata.getString(
                MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION)}")
            return mediaController.metadata.description.description
        }

        override fun getCurrentLargeIcon(
            player: Player,
            callback: PlayerNotificationManager.BitmapCallback
        ): Bitmap? {
            Glide.with(context).asBitmap()
                .load(MUSIC_IMAGE_URL)
                .into(object: CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        callback.onBitmap(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {

                    }
                })
            return null
        }
    }

}