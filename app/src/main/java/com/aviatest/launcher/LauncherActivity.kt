package com.aviatest.launcher

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aviatest.R
import com.aviatest.search.SearchActivity

class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        startActivity(Intent(this, SearchActivity::class.java))
        finish()

        super.onCreate(savedInstanceState)
    }
}