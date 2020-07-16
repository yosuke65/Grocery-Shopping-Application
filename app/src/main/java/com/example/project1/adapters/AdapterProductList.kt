package com.example.project1.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project1.R
import com.example.project1.activities.ProductDetailActivity
import com.example.project1.apps.Endpoints
import com.example.project1.models.Product
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_product_adapter.view.*

class AdapterProductList(var mContext: Context, var subId: Int) :
    RecyclerView.Adapter<AdapterProductList.MyViewHolder>() {

    var mList: ArrayList<Product> = ArrayList()

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        fun bind(product: Product) {
            itemView.text_view_product_name.text = product.productName
            itemView.text_view_product_price.text = "$${"%.2f".format(product.price)}"
            itemView.text_view_product_mrt.text = "$${"%.2f".format(product.mrp)}"
            itemView.text_view_product_saved.text = "Save $${"%.2f".format(product.mrp - product.price)}"
//            itemView.text_view_product_price.text = "$${Math.round(product.price*100)/100.00}"
//            itemView.text_view_product_mrt.text = "$${Math.round(product.mrp*100)/100.00}"
//            itemView.text_view_product_saved.text = "Save $${Math.round(product.mrp - product.price)/100.00}"

            Picasso.get().load(Endpoints.getImage(product.image)).fit().centerCrop()
                .placeholder(R.drawable.progress_loading_image).error(R.drawable.icon_no_image)
                .into(itemView.image_view_product)
            itemView.setOnClickListener {
                var myIntent = Intent(mContext, ProductDetailActivity::class.java)
                myIntent.putExtra(Product.KEY, product)
                mContext.startActivity(myIntent)
            }
            itemView.button_add.setOnClickListener(this)
            itemView.button_increment.setOnClickListener(this)
            itemView.button_decrement.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            when (view?.id) {
                R.id.button_add -> {
                    itemView.button_add.visibility = View.INVISIBLE
                    itemView.button_increment_decrement.visibility = View.VISIBLE
                }
                R.id.button_increment -> {
                    var count = itemView.text_view_count.text.toString().toInt()
                    count++
                    itemView.text_view_count.text = count.toString()
                }
                R.id.button_decrement -> {
                    var count = itemView.text_view_count.text.toString().toInt()
                    if (count === 1) {
                        itemView.button_add.visibility = View.VISIBLE
                        itemView.button_increment_decrement.visibility = View.INVISIBLE
                    } else {
                        count--
                        itemView.text_view_count.text = count.toString()
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(mContext).inflate(R.layout.row_product_adapter, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setData(list: ArrayList<Product>) {
        mList = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(mList[position])
    }
}