package com.example.project1.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.project1.R
import com.example.project1.adapters.AdapterAddressList
import com.example.project1.apps.Endpoints
import com.example.project1.helpers.*
import com.example.project1.models.Address
import com.example.project1.models.AddressResponse
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_address.*

class AddressActivity : AppCompatActivity(), AdapterAddressList.OnAdapterInteraction {

    private var mList: ArrayList<Address> = ArrayList()
    private lateinit var myAdapter: AdapterAddressList
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)

        init()
    }

    override fun onRestart() {
        getData()
        super.onRestart()
    }

    private fun init() {

        toolbar("Add Address")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        myAdapter = AdapterAddressList(this)
        sessionManager = SessionManager(this)
        recycler_view_address.layoutManager = LinearLayoutManager(this)
        recycler_view_address.adapter = myAdapter
        button_add_address.setOnClickListener {
            startActivity(Intent(this, AddAddressActivity::class.java))
        }
        myAdapter.setOnAdapterInteraction(this)

        getData()


    }

    private fun getData() {
        progress_circular.show()
        var userId = sessionManager.getUserId().toString()
        var request = StringRequest(
            Request.Method.GET,
            Endpoints.getAddressURL(userId),
            Response.Listener {
                var gson = GsonBuilder().create()
                var addressResponse = gson.fromJson(it, AddressResponse::class.java)
                for (address in addressResponse.data) {
                    if(! mList.contains(address)){
                        mList?.add(address)
                    }

                }
                Log.d("address", mList.size.toString())
                myAdapter.setData(mList)
                progress_circular.hide()
            },
            Response.ErrorListener {
                toast("Request Failed")
                progress_circular.hide()
            })
        Volley.newRequestQueue(this).add(request)
    }

    override fun onItemClicked(itemView: View, operation: String, address: Address, position: Int) {
        when (operation) {
            AdapterAddressList.DELETE -> {
                deleteData(address)
            }
        }
    }

    private fun deleteData(address: Address) {

        progress_circular.show()
        var request = StringRequest(Request.Method.DELETE, Endpoints.getAddressURL(address._id),
            Response.Listener {
                progress_circular.hide()
            },
            Response.ErrorListener {
                toast("Request Failed")
                progress_circular.hide()
            })

        Volley.newRequestQueue(this).add(request)
    }
}