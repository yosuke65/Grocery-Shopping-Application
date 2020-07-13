package com.example.project1.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.project1.fragments.ProductFragment
import com.example.project1.models.SubCategory

class AdapterTabViewPager(fm:FragmentManager):FragmentPagerAdapter(fm) {

    var fragmentList:ArrayList<ProductFragment> = ArrayList()
    var subCatList:ArrayList<SubCategory> = ArrayList()

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    fun addFragment(f:ProductFragment){
        fragmentList.add(f)
        notifyDataSetChanged()
    }

    fun addSubCategory(subCat:SubCategory){
        subCatList.add(subCat)
        notifyDataSetChanged()
    }
    override fun getPageTitle(position: Int): CharSequence? {
        return subCatList[position].subName
    }
}