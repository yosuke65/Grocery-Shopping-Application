package com.example.project1.activities

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.project1.R
import com.example.project1.adapters.AdapterTabViewPager
import com.example.project1.apps.Endpoints
import com.example.project1.fragments.ProductFragment
import com.example.project1.helpers.toast
import com.example.project1.models.Category
import com.example.project1.models.SubCatResponse
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_sub_cat.*
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.app_bar.view.*

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

//        button_back.setOnClickListener{
//            onBackPressed()
//        }

        cat = intent.getSerializableExtra(Category.CATEGORY) as? Category
        var toolbar = toolbar_main
        toolbar.title = cat?.catName
        toolbar.setTitleTextColor(Color.WHITE)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.product_datail_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
            }
            R.id.action_go_to_cart -> {
                toast("Cart clicked")
            }
        }

        return super.onOptionsItemSelected(item)
    }
}