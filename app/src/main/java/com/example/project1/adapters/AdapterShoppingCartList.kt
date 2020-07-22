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
import com.example.project1.database.DBHelper
import com.example.project1.helpers.SessionManager
import com.example.project1.helpers.toast
import com.example.project1.models.Product
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_product_adapter.view.image_view_product
import kotlinx.android.synthetic.main.row_shopping_cart_adapter.view.*

class AdapterShoppingCartList(var mContext: Context, var mList: ArrayList<Product>) :
    RecyclerView.Adapter<AdapterShoppingCartList.MyViewHolder>() {

    var listener:OnAdapterInteraction? = null
    companion object{
        const val REMOVE = "remove"
        const val INCREMENT = "increment"
        const val DECREMENT = "decrement"
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var sessionManager = SessionManager(mContext)
        var dbHelper = DBHelper(mContext)

        fun bind(product: Product, position: Int) {

            initializeView(product)

            itemView.button_remove_cart.setOnClickListener {
                listener!!.onItemClicked(itemView, REMOVE, position, product)
                mList.remove(product)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, mList.size)
            }
            itemView.button_increment_cart.setOnClickListener {
                listener!!.onItemClicked(itemView, INCREMENT, position, product)
            }
            itemView.button_decrement_cart.setOnClickListener {
                listener!!.onItemClicked(itemView, DECREMENT, position, product)
            }

        }


        private fun initializeView(product: Product) {
            itemView.text_view_product_name_cart.text = product.productName
            itemView.text_view_product_price_cart.text = "$${"%.2f".format(product.price)}"
            itemView.text_view_product_mrt_cart.text = "$${"%.2f".format(product.mrp)}"

            //Move to activity
            var count = dbHelper.getItemQuantity(product._id)
            itemView.text_view_count_cart.text = count.toString()
            Picasso.get().load(Endpoints.getImageURL(product.image!!)).fit().centerCrop()
                .placeholder(R.drawable.progress_loading_image).error(R.drawable.icon_no_image)
                .into(itemView.image_view_product)
        }

    }

    interface OnAdapterInteraction{
        fun onItemClicked(view: View, operation: String, position: Int, product: Product)
    }

    fun setOnAdapterInteraction(onAdapterInteraction: OnAdapterInteraction){
        listener = onAdapterInteraction
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(mContext).inflate(R.layout.row_shopping_cart_adapter, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(mList[position], position)
    }

}