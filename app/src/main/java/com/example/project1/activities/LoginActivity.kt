package com.example.project1.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.project1.R
import com.example.project1.apps.Endpoints
import com.example.project1.helpers.SessionManager
import com.example.project1.helpers.hide
import com.example.project1.helpers.show
import com.example.project1.helpers.toast
import com.example.project1.models.LoginResponse
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject
import kotlin.math.log

class LoginActivity : AppCompatActivity() {

    lateinit var sessionManager:SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        init()
    }

    private fun init() {

        sessionManager = SessionManager(this)
        button_login.setOnClickListener {
            login()
        }
    }

    private fun login() {

        progress_circular.show()

        var email = edit_text_email.text.toString()
        var password = edit_text_password.text.toString()

        var params = HashMap<String, String>()
        params["email"] = email
        params["password"] = password

        postData(params)



    }

    private fun postData(params: HashMap<String, String>) {

        val jsonObject = JSONObject(params as Map<String, String>)
        var request = JsonObjectRequest(
            Request.Method.POST,
            Endpoints.getLoginURL(),
            jsonObject,
            Response.Listener {
                //toast(it.toString())
                var gson = GsonBuilder().create()
                var loginResponse = gson.fromJson(it.toString(), LoginResponse::class.java)
                //toast(loginResponse.token)
                var id = loginResponse.user._id
                var name = loginResponse.user.firstName
                var email = loginResponse.user.email
                var mobile = loginResponse.user.mobile
                var token = loginResponse.token
                sessionManager.login(id!!, name!!, email!!, mobile!!, token!!)
                progress_circular.hide()
                startActivity(Intent(this, MainActivity::class.java))
            },
            Response.ErrorListener {
                toast("Failed")
            })

        Volley.newRequestQueue(this).add(request)
    }

}