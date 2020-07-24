package com.example.project1.activities

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.project1.R
import kotlinx.android.synthetic.main.activity_login_register.*

class LoginRegisterActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_register)

        init()
    }

    private fun init() {

        var animationDrawable = login_register_layout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(10)
        animationDrawable.setExitFadeDuration(2000)
        animationDrawable.start()
        button_sign_in.setOnClickListener(this)
        button_sign_up.setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.button_sign_in->{
                startActivity(Intent(this,LoginActivity::class.java))
            }

            R.id.button_sign_up->{
                startActivity(Intent(this,RegisterActivity::class.java))
            }
        }
    }
}