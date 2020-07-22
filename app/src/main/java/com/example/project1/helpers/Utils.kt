package com.example.project1.helpers

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.project1.R
import kotlinx.android.synthetic.main.app_bar.*

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.d(message: String) {
    Log.d("grocery", message)
}

fun Context.e(message: String) {
    Log.d("grocery", message)
}

fun ProgressBar.show() {
    this.visibility = View.VISIBLE
}

fun ProgressBar.hide() {
    this.visibility = View.INVISIBLE
}

fun AppCompatActivity.toolbar(title:String){
    var toolbar = this.toolbar_main
    toolbar.title = title
    this.setSupportActionBar(toolbar)
    toolbar.setTitleTextColor(resources.getColor(R.color.darkGrey4))
    toolbar.setTitleTextAppearance(this, R.style.titleCustomeAppearence)
//    this.supportActionBar.setDefaultDisplayHomeAsUpEnabled(true)
}