package com.example.project1.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.project1.R
import com.example.project1.adapters.AdapterTabViewPager
import com.example.project1.apps.Endpoints
import com.example.project1.fragments.ProductFragment
import com.example.project1.models.Category
import com.example.project1.models.SubCatResponse
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_sub_cat.*

class SubCatActivity : AppCompatActivity() {

    private var cat:Category? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_cat)

        init()
    }

    private fun init() {

        var myAdapter = AdapterTabViewPager(supportFragmentManager)
        view_pager.adapter = myAdapter


        cat = intent.getSerializableExtra(Category.CATEGORY) as Category
        var url = Endpoints.getSubCategory(cat!!.catId)

        var requestQueue = Volley.newRequestQueue(this)
        var request = StringRequest(Request.Method.GET,url,Response.Listener {
            var gson = GsonBuilder().create()
            var subCatResponse = gson.fromJson(it,SubCatResponse::class.java)
            for(data in subCatResponse.data){
                myAdapter.addFragment(ProductFragment.newInstance(data.subId))
                myAdapter.addSubCategory(data)
            }
            progress_circular.visibility = View.INVISIBLE
            tab_layout.setupWithViewPager(view_pager)
        }, Response.ErrorListener {

        })

        requestQueue.add(request)

    }
}