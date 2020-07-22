package com.example.project1.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.project1.R
import com.example.project1.adapters.AdapterProductList
import com.example.project1.apps.Endpoints
import com.example.project1.database.DBHelper
import com.example.project1.helpers.SessionManager
import com.example.project1.helpers.toast
import com.example.project1.models.Product
import com.example.project1.models.ProductResponse
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_product.*
import kotlinx.android.synthetic.main.row_product_adapter.view.*
import kotlinx.android.synthetic.main.shopping_cart_menu.*

private const val ARG_PARAM1 = "param1"

class ProductFragment : Fragment(), AdapterProductList.OnAdapterInteraction {

    private var subId: Int? = null
    lateinit var dbHelper: DBHelper
    lateinit var myAdapter:AdapterProductList
    private var listener:OnFragmentInteraction? = null

//    private var mList:ArrayList<Product> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            subId = it.getInt(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var view = inflater.inflate(R.layout.fragment_product, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
    }

    override fun onResume() {
        super.onResume()
        listener?.setText(getTotalCountInCart().toString())
        getData()
    }

    private fun init() {
        var sessionManager = SessionManager(activity!!)
        dbHelper = DBHelper(activity!!)
        myAdapter = AdapterProductList(activity!!)
        myAdapter.setOnAdapterInteraction(this)
        recycler_view.layoutManager = LinearLayoutManager(activity)
        recycler_view.adapter = myAdapter
        progress_circular.visibility = View.VISIBLE
        getData()
        progress_circular.visibility = View.INVISIBLE
    }

    private fun getData() {
        var requestQueue = Volley.newRequestQueue(activity)
        var request = StringRequest(Request.Method.GET, Endpoints.getProductsURL(subId!!),Response.Listener {
            var gson = GsonBuilder().create()
            var productResponse = gson.fromJson(it,ProductResponse::class.java)
            myAdapter.setData(productResponse.data)
        }, Response.ErrorListener {
            activity!!.toast("Failed to read data")
        })

        requestQueue.add(request)
    }

    override fun onItemClicked(view: View, operation:String, position: Int, product: Product) {
        when (operation) {
            AdapterProductList.ADD -> {
                addToCart(view,product)
            }
            AdapterProductList.INCREMENT -> {
                incrementItemQuantity(view,product._id)
            }
            AdapterProductList.DECREMENT -> {
                decrementItemQuantity(view,product._id)
            }
            else ->{
                activity!!.toast("id not found")
            }
        }
    }

    private fun addToCart(view: View,product: Product) {

        view.button_increment_decrement.visibility = View.VISIBLE
        view.button_add.visibility = View.INVISIBLE
        dbHelper.addToCart(product,1)
        setItemCountInCart()
    }

    interface OnFragmentInteraction{
        fun setText(text:String)
    }

    fun setOnFragmentInteraction(onFragmentInteraction:OnFragmentInteraction){
        listener = onFragmentInteraction
    }
    private fun setItemCountInCart() {
        listener?.setText(getTotalCountInCart().toString())
    }

    private fun getTotalCountInCart(): Int {
        var itemCountInCart = 0
        var cartList = dbHelper.readCart()
        for(item in cartList){
            itemCountInCart += dbHelper.getItemQuantity(item._id)
        }

        return itemCountInCart
    }

    private fun decrementItemQuantity(view:View,id: String) {

        var count = view.text_view_count.text.toString().toInt()
        if (count === 1) {
            view.button_add.visibility = View.VISIBLE
            view.button_increment_decrement.visibility = View.INVISIBLE
            removeFromCart(id)
        } else {
            count--
            view.text_view_count.text = count.toString()
            dbHelper.updateItem(id, count)
        }
        setItemCountInCart()
    }

    private fun removeFromCart(id: String) {
        dbHelper.deleteItem(id)
    }

    private fun incrementItemQuantity(view: View,id:String) {
        var count = view.text_view_count.text.toString().toInt()
        count++
        view.text_view_count.text = count.toString()
        dbHelper.updateItem(id, count)
        setItemCountInCart()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int) =
            ProductFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                }
            }
    }
}