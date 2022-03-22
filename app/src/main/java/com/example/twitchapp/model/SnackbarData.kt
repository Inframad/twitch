package com.example.twitchapp.model

import android.view.View

class SnackbarData (
    val message: String,
    val actionName: String,
    val action: (View) -> Unit
)