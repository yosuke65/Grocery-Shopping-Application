package com.example.project1.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project1.R
import com.example.project1.activities.AddAddressActivity
import com.example.project1.activities.PaymentActivity
import com.example.project1.database.DBHelper
import com.example.project1.helpers.SessionManager
import com.example.project1.models.Address
import kotlinx.android.synthetic.main.row_address_adapter.view.*

class AdapterAddressList(var mContext: Context):RecyclerView.Adapter<AdapterAddressList.MyViewHolder>() {

    private var mList:ArrayList<Address>? = ArrayList()
    private var sessionManager = SessionManager(mContext)
    private var listener:OnAdapterInteraction? = null

    companion object{
        const val DELETE = "delete"
        const val EDIT = "edit"
    }

    inner class MyViewHolder(view: View):RecyclerView.ViewHolder(view){
        fun bind(address:Address, position: Int){
            itemView.text_view_house_no.text = address.houseNo
            itemView.text_view_street_name.text = address.streetName
            itemView.text_view_city_name.text = address.city
            itemView.text_view_pin_code.text = address.pincode.toString()
            itemView.text_view_address_type.text = address.getAddressType(address.type.toInt())

            itemView.setOnClickListener{
                var myIntent = Intent(mContext, PaymentActivity::class.java)
                myIntent.putExtra(Address.KEY, address)
                mContext.startActivity(myIntent)
            }

            itemView.button_delete_address.setOnClickListener{
                listener?.onItemClicked(itemView, DELETE, address, position)
                mList?.remove(address)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, mList!!.size)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.row_address_adapter, parent, false))
    }

    override fun getItemCount(): Int {
        return mList!!.size
    }

    fun setData(list:ArrayList<Address>){
        mList = list
        notifyDataSetChanged()
    }

    interface OnAdapterInteraction{
        fun onItemClicked(itemView:View,operation:String, address:Address, position:Int)
    }

    fun setOnAdapterInteraction(onAdapterInteraction: OnAdapterInteraction){
        listener = onAdapterInteraction
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(mList!![position], position)
    }
}