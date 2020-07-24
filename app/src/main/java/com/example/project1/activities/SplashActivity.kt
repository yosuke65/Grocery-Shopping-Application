package com.example.project1.activities

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.example.project1.R
import com.example.project1.helpers.SessionManager
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    val delayedTime:Long = 10000
    lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        val animDrawable = splash_layout.background as AnimationDrawable
        animDrawable.setEnterFadeDuration(10)
        animDrawable.setExitFadeDuration(2000)
        animDrawable.start()


        sessionManager = SessionManager(this)
        var handler = Handler()
        handler.postDelayed({
            checkLogin()
        }, delayedTime)
    }

    fun checkLogin(){
        var intent = if(sessionManager.isLoggedIn()){
            Intent(this, MainActivity::class.java)
        }else{
            Intent(this, LoginRegisterActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}