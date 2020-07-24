package com.example.project1.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.project1.R
import com.example.project1.adapters.AdapterTabViewPager
import com.example.project1.apps.Endpoints
import com.example.project1.database.DBHelper
import com.example.project1.fragments.ProductFragment
import com.example.project1.helpers.SessionManager
import com.example.project1.helpers.toolbar
import com.example.project1.models.Category
import com.example.project1.models.SubCatResponse
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_sub_cat.*
import kotlinx.android.synthetic.main.activity_sub_cat.tab_layout
import kotlinx.android.synthetic.main.main_menu.view.*
import kotlinx.android.synthetic.main.placeholder_layout_product_list.*
import kotlinx.android.synthetic.main.shopping_cart_menu.view.*


class SubCatActivity : AppCompatActivity(),ProductFragment.OnFragmentInteraction {

    private var cat: Category? = null
    lateinit var dbHelper:DBHelper
    lateinit var sessionManager: SessionManager
    private var countTextView:TextView? = null
    private var userNameTextView: TextView? = null
    private var shimmerFrameLayout:ShimmerFrameLayout? = null
    var handler = Handler()
    val delayedTime:Long = 1000



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_cat)

        init()
    }


    private fun init() {
        shimmerFrameLayout = shimmer_view_container
        shimmerFrameLayout!!.visibility = View.VISIBLE
        shimmerFrameLayout!!.startShimmer()
        sessionManager = SessionManager(this)
        dbHelper = DBHelper(this)

        var myAdapter = AdapterTabViewPager(supportFragmentManager)
        view_pager.adapter = myAdapter

        cat = intent.getSerializableExtra(Category.CATEGORY) as? Category

        this.toolbar(cat!!.catName)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var url = Endpoints.getSubCategoryURL(cat!!.catId)

        var requestQueue = Volley.newRequestQueue(this)
        var request = StringRequest(Request.Method.GET, url, Response.Listener {
            var gson = GsonBuilder().create()
            var subCatResponse = gson.fromJson(it, SubCatResponse::class.java)
            for (data in subCatResponse.data) {
                var productFragment = ProductFragment.newInstance(data.subId)
                productFragment.setOnFragmentInteraction(this)
                myAdapter.addFragment(productFragment)
                myAdapter.addSubCategory(data)
            }

            tab_layout.setupWithViewPager(view_pager)
        }, Response.ErrorListener {

        })

        requestQueue.add(request)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        for (i in 0 until menu!!.size()) {
            var itemMenu: MenuItem? = menu?.getItem(i)
            when (itemMenu?.itemId) {
                R.id.action_go_to_cart -> {
                    var rootView = itemMenu?.actionView
                    countTextView = rootView?.text_view_cart_badge
                    countTextView?.text = getTotalCountInCart().toString()
                    rootView?.text_view_cart_badge?.setOnClickListener {
                        onOptionsItemSelected(itemMenu!!)
                    }
                    rootView?.image_view_shopping_cart?.setOnClickListener {
                        onOptionsItemSelected(itemMenu!!)
                    }
                }
                R.id.action_go_to_user_setting -> {
                    var rootView = itemMenu?.actionView
                    userNameTextView = rootView?.text_view_user_setting
                    userNameTextView?.text = "Hi, ${sessionManager.getUserName()}"
                    rootView?.text_view_user_setting?.setOnClickListener{
                        startActivity(Intent(this, UserProfileActivity::class.java))
                    }
                    rootView?.image_view_person?.setOnClickListener{
                        startActivity(Intent(this, UserProfileActivity::class.java))
                    }
                }
            }
        }

        return super.onPrepareOptionsMenu(menu)
    }

    private fun getTotalCountInCart(): Int {
        var itemCountInCart = 0
        var cartList = dbHelper.readCart()
        for(item in cartList){
            itemCountInCart += dbHelper.getItemQuantity(item._id)
        }

        return itemCountInCart
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
            R.id.action_go_to_cart -> {
                startActivity(Intent(this, ShoppingCartActivity::class.java))
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun setText(text: String) {
        countTextView?.text = text
    }

    override fun onImageLoadSuccess(view: View) {
        handler.postDelayed({
            shimmerFrameLayout!!.stopShimmer()
            shimmerFrameLayout!!.visibility = View.INVISIBLE
            activity_sub_cat.visibility = View.VISIBLE
        },delayedTime)
    }


}