package com.example.project1.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.project1.R
import com.example.project1.adapters.AdapterOrderList
import com.example.project1.apps.Endpoints
import com.example.project1.database.DBHelper
import com.example.project1.helpers.SessionManager
import com.example.project1.helpers.toast
import com.example.project1.helpers.toolbar
import com.example.project1.models.Order
import com.example.project1.models.OrderResponse
import com.example.project1.models.Product
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_order.*

class OrderActivity : AppCompatActivity() {
    var mList: ArrayList<Order> = ArrayList()
    private var productList:ArrayList<Product> = ArrayList()
    lateinit var dbHelper:DBHelper

    lateinit var myAdapter:AdapterOrderList
    lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        init()
    }

    private fun init() {

        dbHelper = DBHelper(this)
        toolbar("My Order")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        sessionManager = SessionManager(this)
        recycler_view_orders.layoutManager = LinearLayoutManager(this)
        myAdapter = AdapterOrderList(this)
        recycler_view_orders.adapter = myAdapter

        getData()
    }

    private fun getData() {

        var userId = sessionManager.getUserId()
        var request = StringRequest(Request.Method.GET, Endpoints.getOrdersURL(userId),
            Response.Listener {
                var gson = GsonBuilder().create()
                var orderResponse = gson.fromJson(it, OrderResponse::class.java)
                Log.d("order", orderResponse.data.size.toString())
                for (order in orderResponse.data){
                    mList.add(order)
                    Log.d("order", order?.orderStatus)
                    Log.d("order", order?.date)
                    for (product in order.products){
                        product.orderStatus = order.orderStatus
                        product.orderDate = order.date
                        productList.add(product)
                    }
                }
                Log.d("order", productList.size.toString())

                myAdapter.setData(productList)

            }, Response.ErrorListener {
                toast("Request Failed")
            })
        Volley.newRequestQueue(this).add(request)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}