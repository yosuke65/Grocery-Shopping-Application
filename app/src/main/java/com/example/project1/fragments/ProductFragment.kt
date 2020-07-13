package com.example.project1.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.project1.R
import com.example.project1.adapters.AdapterProductList
import com.example.project1.apps.Endpoints
import com.example.project1.helpers.toast
import com.example.project1.models.Product
import com.example.project1.models.ProductResponse
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_product.*

private const val ARG_PARAM1 = "param1"

class ProductFragment : Fragment() {

    private var subId: Int? = null
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
        return inflater.inflate(R.layout.fragment_product, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
    }

    private fun init() {
        var myAdapter = AdapterProductList(activity!!, subId!!)
        recycler_view.layoutManager = LinearLayoutManager(activity)
        recycler_view.adapter = myAdapter

        var requestQueue = Volley.newRequestQueue(activity)
        var request = StringRequest(Request.Method.GET, Endpoints.getProductsBySubId(subId!!),Response.Listener {
            var gson = GsonBuilder().create()
            var productResponse = gson.fromJson(it,ProductResponse::class.java)
            myAdapter.setData(productResponse.data)
            progress_circular.visibility = View.INVISIBLE
        }, Response.ErrorListener {
            activity!!.toast("Failed to read data")
        })

        requestQueue.add(request)
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