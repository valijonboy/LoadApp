package com.pop

import android.app.DownloadManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.databinding.DataBindingUtil
import com.pop.databinding.ActivityMainBinding
import com.pop.databinding.ContentMainBinding


class MainActivity : AppCompatActivity() {

    private var glideDownloadId: Long = 0
    private var udacityDownloadId: Long = 0
    private var retrofitDownloadId: Long = 0

    private lateinit var contentBinding: ContentMainBinding
    private lateinit var binding: ActivityMainBinding
    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action
    private lateinit var custom_button: LoadingButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.setOnClickListener {
            contentBinding.radioGroup.clearCheck()
            val checked = contentBinding.radioButtonGlide.isChecked || contentBinding.radioButtonUdacity.isChecked
                    || contentBinding.radioButtonRetrofit.isChecked

            if (checked){
                when{
                    contentBinding.radioButtonGlide.isChecked ->
                    download(glideUrl)

                    contentBinding.radioButtonUdacity.isChecked ->
                        download(udacityUrl)

                    contentBinding.radioButtonRetrofit.isChecked ->
                        download(retrofitUrl)
                }
            }
            else{
                Toast.makeText(this, "Please, check any radioList", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        }
    }

    private fun download(url: String) {
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager

        when(url){
            glideUrl ->{
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
        // enqueue puts the download request in the queue.
    }

    companion object {
        private const val glideUrl =
            "https://github.com/bumptech/glide/archive/master.zip"
        private const val udacityUrl =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val retrofitUrl =
            "https://github.com/square/retrofit/archive/master.zip"
        private const val CHANNEL_ID = "channelId"
    }

}
