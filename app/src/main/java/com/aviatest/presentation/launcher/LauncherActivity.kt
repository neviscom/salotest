package com.aviatest.presentation.launcher

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aviatest.presentation.search.SearchActivity

class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        startActivity(Intent(this, SearchActivity::class.java))
        finish()

        super.onCreate(savedInstanceState)
    }
}