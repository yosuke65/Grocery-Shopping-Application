package com.example.project1.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonRequest
import com.android.volley.toolbox.Volley
import com.example.project1.R
import com.example.project1.apps.Endpoints
import com.example.project1.helpers.hide
import com.example.project1.helpers.show
import com.example.project1.helpers.toast
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.edit_text_email
import kotlinx.android.synthetic.main.activity_register.edit_text_password
import kotlinx.android.synthetic.main.activity_register.progress_circular
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
        }
    }

    private fun register() {
        progress_circular.show()
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

    private fun postData(params: HashMap<String, String>) {
        val jsonObject = JSONObject(params as Map<*, *>)
        var request = JsonObjectRequest(
            Request.Method.POST,
            Endpoints.getRegisterURL(),
            jsonObject,
            Response.Listener {
                progress_circular.hide()
                startActivity(Intent(this,LoginActivity::class.java))
            },
            Response.ErrorListener {

            })
        Volley.newRequestQueue(this).add(request)
    }
}