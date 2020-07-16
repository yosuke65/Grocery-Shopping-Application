package com.example.project1.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonRequest
import com.android.volley.toolbox.Volley
import com.example.project1.R
import com.example.project1.apps.Endpoints
import com.example.project1.helpers.SessionManager
import com.example.project1.helpers.toast
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        init()
    }

    private fun init() {
        button_register.setOnClickListener {
            register()
            startActivity(Intent(this,LoginActivity::class.java))
        }
    }

    private fun register() {

        var firstName = edit_text_name.text.toString()
        var email = edit_text_email.text.toString()
        var password = edit_text_password.text.toString()
        var mobile = edit_text_mobile.text.toString()

        var params = HashMap<String, String>()
        params["firstName"] = firstName
        params["email"] = email
        params["password"] = password
        params["mobile"] = mobile

        val jsonObject = JSONObject(params as Map<*, *>)

        var request = JsonObjectRequest(
            Request.Method.POST,
            Endpoints.postRegister(),
            jsonObject,
            Response.Listener {
                toast("User Registered")
            },
            Response.ErrorListener {
                toast("Register Failed")
            })
        Volley.newRequestQueue(this).add(request)
    }
}