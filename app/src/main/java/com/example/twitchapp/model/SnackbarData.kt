package com.example.twitchapp.model

import android.view.View
import com.google.android.material.snackbar.Snackbar

class SnackbarData (
    val message: String,
    val actionName: String,
    val length: Int = Snackbar.LENGTH_LONG,
    val action: (View) -> Unit
)