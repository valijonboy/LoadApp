package com.pop

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.pop.utils.sendNotification
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var glideDownloadId: Long = 0
    private var udacityDownloadId: Long = 0
    private var retrofitDownloadId: Long = 0

    private lateinit var downloadManager: DownloadManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        createChannel(
            getString(R.string.download_notification_channel_id),
            getString(R.string.notification_title)
        )

        custom_button.setOnClickListener {
            val checked = radio_button_glide.isChecked || radio_button_udacity.isChecked
                    || radio_button_retrofit.isChecked

            if (checked) {
                when {
                    radio_button_glide.isChecked -> {
                        download(glideUrl)
                    }

                    radio_button_udacity.isChecked -> {
                        download(udacityUrl)
                    }

                    radio_button_retrofit.isChecked -> {
                        download(retrofitUrl)
                    }

                }
            } else {
                Toast.makeText(this, "Please, check any radioList", Toast.LENGTH_SHORT).show()
            }
        }

        downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            val cursor: Cursor = downloadManager.query(DownloadManager.Query().setFilterById(id!!))

            if (cursor.moveToNext()) {
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                val notificationManager =
                    ContextCompat.getSystemService(context!!, NotificationManager::class.java)
                            as NotificationManager
                cursor.close()

                when (status) {
                    DownloadManager.STATUS_FAILED -> {
                        when (id) {
                            glideDownloadId -> {
                                custom_button.setLoadState(ButtonState.Completed)
                                notificationManager.sendNotification(
                                    "Download complete",
                                    context,
                                    "glide",
                                    "failed"
                                )
                            }

                            udacityDownloadId -> {
                                custom_button.setLoadState(ButtonState.Completed)
                                notificationManager.sendNotification(
                                    "Download complete",
                                    context,
                                    "udacity",
                                    "failed"
                                )
                            }

                            retrofitDownloadId -> {
                                custom_button.setLoadState(ButtonState.Completed)
                                notificationManager.sendNotification(
                                    "Download complete",
                                    context,
                                    "retrofit",
                                    "failed"
                                )
                            }
                        }
                    }

                    DownloadManager.STATUS_SUCCESSFUL -> {
                        when (id) {
                            glideDownloadId -> {
                                custom_button.setLoadState(ButtonState.Completed)
                                notificationManager.sendNotification(
                                    "Download complete",
                                    context,
                                    "glide",
                                    "success"
                                )
                            }

                            udacityDownloadId -> {
                                custom_button.setLoadState(ButtonState.Completed)
                                notificationManager.sendNotification(
                                    "Download complete",
                                    context,
                                    "udacity",
                                    "success"
                                )
                            }

                            retrofitDownloadId -> {
                                custom_button.setLoadState(ButtonState.Completed)
                                notificationManager.sendNotification(
                                    "Download complete",
                                    context,
                                    "retrofit",
                                    "success"
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun download(url: String) {
        custom_button.setLoadState(ButtonState.Loading)
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        //val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager

        when (url) {
            glideUrl -> {
                glideDownloadId =
                    downloadManager.enqueue(request)
            }
            udacityUrl -> {
                udacityDownloadId = downloadManager.enqueue(request)
            }
            retrofitUrl -> {
                retrofitDownloadId = downloadManager.enqueue(request)
            }
        }
    }

    companion object {
        private const val glideUrl =
            "https://github.com/bumptech/glide/archive/master.zip"
        private const val udacityUrl =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val retrofitUrl =
            "https://github.com/square/retrofit/archive/master.zip"
    }

    private fun createChannel(channelId: String, channelName: String) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_LOW
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.BLUE
            notificationChannel.enableVibration(true)
            notificationChannel.description = "File is downloaded"

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

}
