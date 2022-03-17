package com.example.twitchapp.ui.temp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class MyBroadcastReceiver: BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {
        Toast.makeText(p0, p1?.extras?.getString("something")!!, Toast.LENGTH_SHORT).show()
    }
}