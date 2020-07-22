package com.example.project1.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.project1.R
import com.example.project1.apps.Endpoints
import com.example.project1.database.DBHelper
import com.example.project1.helpers.SessionManager
import com.example.project1.helpers.toast
import com.example.project1.models.*
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_payment.*
import kotlinx.android.synthetic.main.app_bar_add_address.*
import org.json.JSONObject

class PaymentActivity : AppCompatActivity() {
    lateinit var dbHelper: DBHelper
    lateinit var sessionManager: SessionManager

    companion object {
        const val CONFIRMED = "Confirmed"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        init()
    }

    private fun init() {

        var address = intent.getSerializableExtra(Address.KEY) as Address

        initializeView(address)

        button_pay_online.setOnClickListener {
            postOrder(address)
        }
    }

    private fun initializeView(address: Address) {

        dbHelper = DBHelper(this)
        sessionManager = SessionManager(this)
        var toolbar = this.toolbar_add_address
        toolbar.title = "Payment"
        this.setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(resources.getColor(R.color.white1))
        toolbar.setTitleTextAppearance(this, R.style.titleCustomeAppearence)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        text_view_house_no_payment.text = address.houseNo
        text_view_street_name_payment.text = address.streetName
        text_view_city_name_payment.text = address.city
        text_view_pin_code_payment.text = address.pincode.toString()
        text_view_address_type_payment.text = address.getAddressType(address.type.toInt())

        setOrderSummary()
    }

    private fun postOrder(address: Address) {
        var userId = sessionManager.getUserId().toString()
        var orderStatus = CONFIRMED
        var user = sessionManager.getUser()
        var products = dbHelper.readCart()

        var params = HashMap<String, Any?>()
        params["userId"] = userId
        params["orderStatus"] = orderStatus
        params["user"] = makeUserJsonObject(user)
        params["shippingAddress"] = makeAddressJsonObject(address)
        params["products"] = makeProductJsonArray(products)

        var jsonObject = JSONObject(params as Map<*, *>)

        var request = JsonObjectRequest(Request.Method.POST,
            Endpoints.getOrdersURL(),
            jsonObject,
            Response.Listener {
                toast("Request Success")
                startActivity(Intent(this, OrderConfirmationActivity::class.java))
            },
            Response.ErrorListener {
                toast("Request Failed")
            })

        Volley.newRequestQueue(this).add(request)
    }

    private fun makeUserJsonObject(user: User?):JSONObject {
        var params = HashMap<String, String>()
        params["_id"] = user?._id.toString()
        params["email"] = user?.email.toString()
        params["mobile"] = user?.mobile.toString()
        return JSONObject(params as Map<*,*>)
    }

    private fun makeAddressJsonObject(address: Address): JSONObject {
        var params = HashMap<String, String>()
        params["_id"] = address._id
        params["city"] = address.city
        params["houseNo"] = address.houseNo
        params["pincode"] = address.pincode.toString()
        params["streeName"] = address.streetName
        params["type"] = address.type
        return JSONObject(params as Map<*,*>)
    }

    private fun makeProductJsonArray(products: ArrayList<Product>):ArrayList<JSONObject> {
        var productList = ArrayList<JSONObject>()
        var paramsProduct = HashMap<String, Any>()
        for (item in products) {
            paramsProduct["image"] = item.image
            paramsProduct["mrp"] = item.mrp
            paramsProduct["price"] = item.price
            paramsProduct["quantity"] = dbHelper.getItemQuantity(item._id)
            paramsProduct["productName"] = item.productName
            val jsonObjectProduct = JSONObject(paramsProduct as Map<*,*>)
            productList.add(jsonObjectProduct)
        }
        return productList
    }


    private fun setOrderSummary() {
        var mList = dbHelper.readCart()
        text_view_shipping_fee_payment.text = "$${ShoppingCartActivity.SHIPPING_FEE}"
        var subTotal = getSubTotal(mList)
        var orderTotal = subTotal + ShoppingCartActivity.SHIPPING_FEE
        text_view_subtotal_payment.text = "$${"%.2f".format(subTotal)}"
        text_view_order_total_payment.text = "$${"%.2f".format(orderTotal)}"
    }

    private fun getSubTotal(mList: ArrayList<Product>): Double {
        var subTotal = 0.00
        for (product in mList) {
            subTotal += product.price * dbHelper.getItemQuantity(product._id)
        }
        return subTotal
    }

}