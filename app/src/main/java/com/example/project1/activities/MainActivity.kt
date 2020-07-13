package com.example.project1.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.project1.R
import com.example.project1.adapters.AdapterMainCategory
import com.example.project1.apps.Endpoints
import com.example.project1.helpers.toast
import com.example.project1.models.Category
import com.example.project1.models.CategoryResponse
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var mList: ArrayList<Category> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    private fun init() {

        var myAdapter = AdapterMainCategory(this)
        recycler_view.layoutManager = GridLayoutManager(this, 2)
        recycler_view.adapter = myAdapter

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



