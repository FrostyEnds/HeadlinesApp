package com.example.headlinesapp.extensions

import android.app.Activity
import android.view.View

//Activity Extension to hide the keyboard
fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}