package com.example.project1.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project1.R
import com.example.project1.apps.Endpoints
import com.example.project1.database.DBHelper
import com.example.project1.models.Product
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_orders_adapter.view.*
import java.util.ArrayList

class AdapterOrderList(var mContext: Context): RecyclerView.Adapter<AdapterOrderList.MyViewHolder>() {


    var dbHelper = DBHelper(mContext)
    var productList:ArrayList<Product> = ArrayList()


    inner class MyViewHolder(view: View):RecyclerView.ViewHolder(view){
        fun bind(product: Product){
            itemView.text_view_product_order_name.text = product.productName
            itemView.text_view_product_order_date.text = product.orderDate?.substring(0,10)
            itemView.text_view_product_order_status.text = product.orderStatus
            itemView.text_view_product_order_qt.text = product.quantity.toString()
            Picasso.get().load(Endpoints.getImageURL(product.image)).error(R.drawable.icon_no_image).placeholder(R.drawable.no_image_progress).fit().centerCrop().into(itemView.image_view_product_order)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.row_orders_adapter, parent, false))
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(productList[position])
    }

    fun setData(list: ArrayList<Product>) {
        Log.d("order",productList.size.toString())
        productList = list
        notifyDataSetChanged()
    }
}