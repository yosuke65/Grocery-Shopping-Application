package com.example.project1.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project1.R
import com.example.project1.activities.SubCatActivity
import com.example.project1.apps.Endpoints
import com.example.project1.models.Category
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.grid_category_adapter.view.*

class AdapterMainCategory(var mContext: Context) :
    RecyclerView.Adapter<AdapterMainCategory.MyViewHolder>() {

    var mList: ArrayList<Category> = ArrayList()

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(cat: Category) {
            itemView.text_view_cat_name.text = cat.catName
            Picasso.get().load(Endpoints.getImage(cat.catImage)).fit().centerCrop()
                .placeholder(R.drawable.no_image_progress).error(R.drawable.icon_no_image)
                .into(itemView.image_view_category)
            itemView.setOnClickListener {
                var myIntent = Intent(mContext, SubCatActivity::class.java)
                myIntent.putExtra(Category.CATEGORY, cat)
                mContext.startActivity(myIntent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(mContext).inflate(R.layout.grid_category_adapter, parent, false)
        )
    }

    fun setData(list: ArrayList<Category>) {
        mList = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(mList[position])
    }
}