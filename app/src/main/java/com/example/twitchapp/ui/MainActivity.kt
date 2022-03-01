package com.example.twitchapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.twitchapp.App
import com.example.twitchapp.R
import com.example.twitchapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        (application as App).appComponent.inject(this)

        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, GameStreamsFragment())
                .commit()
        }
    }
}