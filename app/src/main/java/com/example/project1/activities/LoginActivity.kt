package com.example.project1.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.project1.R
import com.example.project1.apps.Endpoints
import com.example.project1.helpers.SessionManager
import com.example.project1.helpers.toast
import com.example.project1.models.LoginResponse
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject
import kotlin.math.log

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        init()
    }

    private fun init() {
        button_login.setOnClickListener {
            login()
        }
    }

    private fun login() {

        var sessionManager = SessionManager(this)

        var email = edit_text_email.text.toString()
        var password = edit_text_password.text.toString()

        var params = HashMap<String, String>()
        params["email"] = email
        params["password"] = password
//
//        if(sessionManager.login(email,password)){
//            toast("Login Success")
//        }else{
//            toast("Login Failed")
//
//        }

        val jsonObject = JSONObject(params as Map<String, String>)
        var request = JsonObjectRequest(
            Request.Method.POST,
            Endpoints.postLogin(),
            jsonObject,
            Response.Listener {
                //toast(it.toString())
                var gson = GsonBuilder().create()
                var loginResponse = gson.fromJson(it.toString(), LoginResponse::class.java)
                //toast(loginResponse.token)
                var name = loginResponse.user.firstName
                var email = loginResponse.user.email
                var mobile = loginResponse.user.mobile
                var token = loginResponse.token
                sessionManager.login(name!!, email!!, mobile!!, token!!)
                startActivity(Intent(this, MainActivity::class.java))
            },
            Response.ErrorListener {
                toast("Failed")
            })

        Volley.newRequestQueue(this).add(request)
    }
}