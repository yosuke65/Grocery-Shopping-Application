package com.example.project1.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.project1.R
import com.example.project1.helpers.SessionManager
import kotlinx.android.synthetic.main.activity_user_setting.*

class UserProfileActivity : AppCompatActivity() {

    lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_setting)

        init()
    }

    private fun init() {
        sessionManager = SessionManager(this)
        var name = sessionManager.getUser()
        var email = sessionManager.getEmail()
        var mobile = sessionManager.getMobile()
        text_view_user_profile_name.text = "Hi,$name"
        text_view_user_profile_mobile.text = mobile
        text_view_user_profile_email.text = email

        button_sign_out.setOnClickListener{
            sessionManager.logout()
            startActivity(Intent(this,MainActivity::class.java))
        }
    }
}