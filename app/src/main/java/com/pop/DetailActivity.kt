package com.pop

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_detail.*


class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val statusExtra = intent.getStringExtra("STATUS")
        val fileName = intent.getStringExtra("TYPE")

        file_type.text = fileName
        status.text = statusExtra

        if (statusExtra == "FAIL") {
            status.setTextColor(Color.RED)
        } else if (statusExtra == "SUCCESS") {
            status.setTextColor(Color.GREEN)
        }


        button.setOnClickListener{
            val mainIntent= Intent(applicationContext,MainActivity::class.java)
            startActivity(mainIntent)
        }
    }

}
