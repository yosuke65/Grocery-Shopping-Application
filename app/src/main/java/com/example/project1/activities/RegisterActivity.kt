package com.example.project1.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.project1.R
import com.example.project1.apps.Endpoints
import com.example.project1.helpers.toast
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {

    lateinit var lottieAnimView:LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        init()
    }

    private fun init() {
        button_register.setOnClickListener {
            register()
        }
    }

    private fun register() {
        lottieAnimView = lottie_anim_view_loading_register
        startAnim()

        var firstName = edit_text_name.text.toString()
        var email = edit_text_email.text.toString()
        var password = edit_text_password.text.toString()
        var mobile = edit_text_mobile.text.toString()

        var params = HashMap<String, String>()
        params["firstName"] = firstName
        params["email"] = email
        params["password"] = password
        params["mobile"] = mobile

        postData(params)

    }

    private fun startAnim() {
        lottieAnimView.playAnimation()
        lottieAnimView.visibility = View.VISIBLE
    }

    private fun postData(params: HashMap<String, String>) {
        val jsonObject = JSONObject(params as Map<*, *>)
        var request = JsonObjectRequest(
            Request.Method.POST,
            Endpoints.getRegisterURL(),
            jsonObject,
            Response.Listener {
                startActivity(Intent(this,LoginActivity::class.java))
                cancelAnim()
            },
            Response.ErrorListener {
                toast("Request Failed")
                cancelAnim()
            })
        Volley.newRequestQueue(this).add(request)
    }

    private fun cancelAnim() {
        lottie_anim_view_loading_register.visibility = View.INVISIBLE
        lottie_anim_view_loading_register.cancelAnimation()
    }
}