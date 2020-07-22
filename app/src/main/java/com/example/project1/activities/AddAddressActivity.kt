package com.example.project1.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.project1.R
import com.example.project1.apps.Endpoints
import com.example.project1.database.DBHelper
import com.example.project1.helpers.*
import com.example.project1.models.Address
import kotlinx.android.synthetic.main.activity_add_address.*
import kotlinx.android.synthetic.main.app_bar_add_address.*
import org.json.JSONObject

class AddAddressActivity : AppCompatActivity() {

    lateinit var sessionManager: SessionManager
    lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_address)

        init()
    }


    private fun init() {

        var toolbar = this.toolbar_add_address
        toolbar.title = "Add New Address"
        this.setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(resources.getColor(R.color.white1))
        toolbar.setTitleTextAppearance(this, R.style.titleCustomeAppearence)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        dbHelper = DBHelper(this)
        sessionManager = SessionManager(this)
        button_add_address.setOnClickListener {
            var userId = sessionManager.getUserId().toString()
            var houseNo = edit_text_house_no.text.toString()
            var streetName = edit_text_street_address.text.toString()
            var type = getRadioButtonId()
            var city = edit_text_city_name.text.toString()
            var zip = edit_text_zip_code.text.toString()

            var billingName = edit_text_billing_name.text.toString()
//            dbHelper.saveAddress(Address(null,userId, houseNo, streetName, type, city, zip, billingName))



            var param = HashMap<String, String>()
//            Log.d("address", userId)
//            Log.d("address", houseNo)
//            Log.d("address", streetName)
//            Log.d("address", type)
//            Log.d("address", city)
//            Log.d("address", zip)

            param["userId"] = userId
            param["houseNo"] = houseNo
            param["streetName"] = streetName
            param["type"] = type.toString()
            param["city"]  = city
            param["pincode"] = zip

            var jsonObject = JSONObject(param as Map<*, *>)

            var request = JsonObjectRequest(
                Request.Method.POST,
                Endpoints.getAddressURL(),
                jsonObject,
                Response.Listener {
                    toast("Address added")
                    progress_circular.hide()
                    finish()
                },
                Response.ErrorListener {
                    toast(it.toString())
                    Log.d("address", it.toString())
                    progress_circular.hide()
                })

            progress_circular.show()
            Volley.newRequestQueue(this).add(request)
        }
    }

    private fun getRadioButtonId(): Int {
        var selectedId = radio_group.checkedRadioButtonId
        return when(selectedId){
            R.id.radio_button_home -> 1
            R.id.radio_button_office -> 2
            else -> 3
        }
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