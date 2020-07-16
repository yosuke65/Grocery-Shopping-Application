package com.example.project1.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.project1.R
import com.example.project1.adapters.AdapterMainCategory
import com.example.project1.apps.Endpoints
import com.example.project1.helpers.SessionManager
import com.example.project1.helpers.toast
import com.example.project1.models.Category
import com.example.project1.models.CategoryResponse
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity() : AppCompatActivity(),View.OnClickListener{

    private var mList: ArrayList<Category> = ArrayList()
    lateinit var sessionManager: SessionManager

    constructor(parcel: Parcel) : this() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    private fun init() {

        var sessionManager = SessionManager(this)
        if(!sessionManager.isLoggedIn()){
            startActivity(Intent(this, LoginRegisterActivity::class.java))
            finish()
        }else{

            text_view_welcome_user.text = "Hi, ${sessionManager.getUser()}"

            var myAdapter = AdapterMainCategory(this)
            recycler_view.layoutManager = GridLayoutManager(this, 2,GridLayoutManager.HORIZONTAL,false)
            recycler_view.adapter = myAdapter

            text_view_welcome_user.setOnClickListener(this)

            var requestQueue = Volley.newRequestQueue(this)
            var request = StringRequest(Request.Method.GET, Endpoints.getCategory(), Response.Listener {
                var gson = GsonBuilder().create()
                var categoryResponse = gson.fromJson(it, CategoryResponse::class.java)

                myAdapter.setData(categoryResponse.data)
                progress_circular.visibility = View.INVISIBLE

            }, Response.ErrorListener {
                this.toast("Response Failed")
            })

            requestQueue.add(request)

        }
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.text_view_welcome_user->{
                startActivity(Intent(this,UserProfileActivity::class.java))
            }
        }
    }

}



