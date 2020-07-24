package com.example.project1.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project1.R
import com.example.project1.activities.ProductDetailActivity
import com.example.project1.apps.Endpoints
import com.example.project1.database.DBHelper
import com.example.project1.helpers.SessionManager
import com.example.project1.helpers.toast
import com.example.project1.models.Product
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_product_adapter.view.*
import java.lang.Exception

class AdapterProductList(var mContext: Context) :
    RecyclerView.Adapter<AdapterProductList.MyViewHolder>() {

    private var mList: ArrayList<Product> = ArrayList()
    private var listener: OnAdapterInteraction? = null


    companion object {
        const val ADD = "add"
        const val INCREMENT = "increment"
        const val DECREMENT = "decrement"
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var sessionManager = SessionManager(mContext)
        private var dbHelper = DBHelper(mContext)

        fun bind(product: Product, position: Int) {

            initializeItemView(product)

            itemView.setOnClickListener {
                var myIntent = Intent(mContext, ProductDetailActivity::class.java)
                myIntent.putExtra(Product.KEY, product)
                mContext.startActivity(myIntent)
            }

            itemView.button_add.setOnClickListener {
                listener!!.onItemClicked(itemView, ADD, position, product)
            }
            itemView.button_increment.setOnClickListener {
                listener!!.onItemClicked(itemView, INCREMENT, position, product)
            }
            itemView.button_decrement.setOnClickListener {
                listener!!.onItemClicked(itemView, DECREMENT, position, product)
            }
        }


        private fun initializeItemView(product: Product) {
            if (dbHelper.isProductInCart(product)) {
                itemView.button_add.visibility = View.INVISIBLE
                itemView.button_increment_decrement.visibility = View.VISIBLE
                itemView.text_view_count.text = dbHelper.getItemQuantity(product._id).toString()
            }else{
                itemView.text_view_count.text = "1"
                itemView.button_add.visibility = View.VISIBLE
                itemView.button_increment_decrement.visibility = View.INVISIBLE
            }
            itemView.text_view_product_name.text = product.productName
            itemView.text_view_product_price.text = "$${"%.2f".format(product.price)}"
            itemView.text_view_product_mrt.text = "$${"%.2f".format(product.mrp)}"
            itemView.text_view_product_saved.text =
                "Save $${"%.2f".format(product.mrp!! - product.price!!)}"
            Picasso.get().load(Endpoints.getImageURL(product.image!!)).fit().centerCrop()
                .placeholder(R.drawable.progress_loading_image).error(R.drawable.icon_no_image)
                .into(itemView.image_view_product, object:Callback{
                    override fun onSuccess() {
                        listener?.onImageLoadSuccess(itemView)
                    }

                    override fun onError(e: Exception?) {

                    }

                })

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

    interface OnAdapterInteraction {
        fun onItemClicked(view: View, operation: String, position: Int, product: Product)
        fun onImageLoadSuccess(view: View)
    }

    fun setOnAdapterInteraction(onAdapterInteraction: OnAdapterInteraction) {
        listener = onAdapterInteraction
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(mList[position], position)
    }
}