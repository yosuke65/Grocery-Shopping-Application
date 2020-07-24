package com.example.project1.activities

import android.animation.Animator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.view.View
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project1.R
import com.example.project1.database.DBHelper
import com.example.project1.helpers.toast
import kotlinx.android.synthetic.main.activity_order.*
import kotlinx.android.synthetic.main.activity_order_confirmation.*
import kotlinx.android.synthetic.main.content_main.*

class OrderConfirmationActivity : AppCompatActivity() {

    lateinit var dbHelper: DBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_confirmation)

        init()
    }

    private fun init() {
        var lottiAnimation = lottie_payment_success
        lottiAnimation.addAnimatorListener(object: Animator.AnimatorListener{
            override fun onAnimationRepeat(animator: Animator?) {

            }

            override fun onAnimationEnd(animator: Animator?) {
                startAnimation()
            }

            override fun onAnimationCancel(animator: Animator?) {

            }

            override fun onAnimationStart(aniamtor: Animator?) {

            }
        })
        lottiAnimation.playAnimation()
        dbHelper = DBHelper(this)

        button_back_to_home.setOnClickListener{
            dbHelper.deleteItem()
            startActivity(Intent(this,MainActivity::class.java))
        }
    }

    private fun startAnimation() {
        text_view_order_success.visibility = View.VISIBLE
        text_view_order_success.startAnimation(AnimationUtils.loadAnimation(this,R.anim.anim_fade_in_order_success))
    }
}