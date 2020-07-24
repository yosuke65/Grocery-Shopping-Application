package com.example.project1.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.project1.R
import com.example.project1.activities.OrderDetailActivity
import com.example.project1.apps.Endpoints
import com.example.project1.database.DBHelper
import com.example.project1.models.Order
import com.example.project1.models.Product
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_order_adapter.view.*
import kotlinx.android.synthetic.main.row_orders_adapter.view.*
import kotlinx.android.synthetic.main.row_orders_adapter.view.text_view_product_order_date
import kotlinx.android.synthetic.main.row_orders_adapter.view.text_view_product_order_status
import java.util.ArrayList

class AdapterOrderList(var mContext: Context) :
    RecyclerView.Adapter<AdapterOrderList.MyViewHolder>() {


    var dbHelper = DBHelper(mContext)
    var mList: ArrayList<Order> = ArrayList()


    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(order: Order) {
            Log.d("order", "Product size${order.products.size.toString()}")
            if (order.products.size >= 1) {
                Log.d("order", order.products.size.toString())
                itemView.text_view_product_order_date.text = order.date?.substring(0, 10)
                itemView.text_view_product_order_status.text = order.orderStatus
                itemView.text_view_order_total_payment.text = "$${order.orderSummary.orderAmount.toString()}"
                itemView.text_view_product_order_name1.text = order.products[0].productName
                itemView.text_view_product_order_qt1.text = order.products[0].quantity.toString()
            }
            if (order.products.size >= 2){
                itemView.text_view_order_item_list2.visibility = View.VISIBLE
                itemView.text_view_product_order_name2.text = order.products[1].productName
                itemView.text_view_product_order_qt2.text = order.products[1].quantity.toString()
                itemView.text_view_order_item_list2 below itemView.text_view_order_item_list1
            }
            if (order.products.size >= 3){
                itemView.text_view_order_item_list3.visibility = View.VISIBLE
                itemView.text_view_product_order_name3.text = order.products[2].productName
                itemView.text_view_product_order_qt3.text = order.products[2].quantity.toString()
                itemView.text_view_order_item_list3 below itemView.text_view_order_item_list2
            }
            if (order.products.size >= 4){
                itemView.image_view_more_item.visibility = View.VISIBLE
                itemView.image_view_more_item below itemView.text_view_order_item_list3

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(mContext).inflate(R.layout.row_order_adapter, parent, false)
        )
    }

    infix fun View.below(view: View) {
        (this.layoutParams as? RelativeLayout.LayoutParams)?.addRule(RelativeLayout.BELOW, view.id)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    fun setData(list: ArrayList<Order>) {
        mList = list
        notifyDataSetChanged()
    }
}