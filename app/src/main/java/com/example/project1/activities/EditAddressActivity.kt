package com.example.project1.activities

import android.content.Intent
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
import com.example.project1.helpers.toast
import com.example.project1.models.Address
import kotlinx.android.synthetic.main.activity_add_address.*
import kotlinx.android.synthetic.main.activity_add_address.edit_text_billing_name
import kotlinx.android.synthetic.main.activity_edit_address.*
import org.json.JSONObject

class EditAddressActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_address)

        init()
    }

    private fun init() {
        var address = intent.getSerializableExtra(Address.KEY) as Address
        edit_text_billing_name_edit.setText(address.name)
        edit_text_mobile_edit.setText(address.mobile)
        edit_text_city_name_edit.setText((address.city))
        edit_text_house_no_edit.setText(address.houseNo)
        edit_text_street_address_edit.setText(address.streetName)
        edit_text_zip_code_edit.setText(address.pincode.toString())
        when (address.type) {
            "Home" -> radio_button_home_edit.isChecked = true
            "Office" -> radio_button_office_edit.isChecked = true
            "Other" -> radio_button_other_edit.isChecked = true
        }

        button_save_address.setOnClickListener {
            if(validation()){
                var jsonObject: JSONObject = makeAddressJsonObject(address)
                postData(address._id, jsonObject)
            }
        }

    }

    private fun validation():Boolean {
        return validatePincode()
    }

    private fun validatePincode() :Boolean{
        var pincode = edit_text_zip_code_edit.text.toString()
        return if(pincode.matches("(\\d+)".toRegex())){
            true
        }else{
            toast("Pincode must be an number")
            false
        }

    }

    private fun makeAddressJsonObject(address: Address): JSONObject {
        var id = address._id
        var v = address.__v
        var userId = address.userId
        var type = getRadioButtonId()
        var name = edit_text_billing_name_edit.text.toString()
        var mobile = edit_text_mobile_edit.text.toString()
        var city = edit_text_city_name_edit.text.toString()
        var houseNo = edit_text_house_no_edit.text.toString()
        var streetName = edit_text_street_address_edit.text.toString()
        var pincode = edit_text_zip_code_edit.text.toString()
        var param = HashMap<String, String>()

        param["_id"] = id
        param["__v"] = v.toString()
        param["userId"] = userId
        param["houseNo"] = houseNo
        param["streetName"] = streetName
        param["type"] = type.toString()
        param["city"] = city
        param["pincode"] = pincode
        param["name"] = name
        param["mobile"] = mobile

        return JSONObject(param as Map<*, *>)

    }

    private fun postData(id:String,jsonObject: JSONObject) {
        Log.d("address",jsonObject.toString())
        var request = JsonObjectRequest(Request.Method.PUT,Endpoints.getAddressURL(id),jsonObject,
            Response.Listener {
                startActivity(Intent(this,AddressActivity::class.java))
                finish()
            }, Response.ErrorListener {
                toast("Request Failed")
            })
        Volley.newRequestQueue(this).add(request)
    }

    private fun getRadioButtonId(): String {
        var selectedId = radio_group_edit.checkedRadioButtonId
        return when (selectedId) {
            R.id.radio_button_home -> "Home"
            R.id.radio_button_office -> "Office"
            else -> "Other"
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}