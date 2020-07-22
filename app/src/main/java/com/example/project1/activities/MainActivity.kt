package com.example.project1.activities

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.project1.R
import com.example.project1.adapters.AdapterMainCategory
import com.example.project1.apps.Endpoints
import com.example.project1.database.DBHelper
import com.example.project1.helpers.SessionManager
import com.example.project1.helpers.toast
import com.example.project1.helpers.toolbar

import com.example.project1.models.CategoryResponse
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


import kotlinx.android.synthetic.main.main_menu.view.*
import kotlinx.android.synthetic.main.nav_header_layout.view.*
import kotlinx.android.synthetic.main.shopping_cart_menu.view.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    lateinit var dbHelper: DBHelper
    lateinit var sessionManager: SessionManager
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private var countTextView: TextView? = null
    private var userNameTextView: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    override fun onRestart() {
        setItemCountInCart()
        super.onRestart()
    }

    private fun init() {

        drawerLayout = drawer
        navView = nav_view
        navView.setNavigationItemSelectedListener(this)
        navView.setItemTextAppearance(R.style.menu_in_nav_style)
        var headerView = navView.getHeaderView(0)

        sessionManager = SessionManager(this)
        dbHelper = DBHelper(this)

        headerView.text_view_user_name_nav.text = sessionManager.getUserName()

        toolbar(getString(R.string.app_name))

        if (!sessionManager.isLoggedIn()) {
            startActivity(Intent(this, LoginRegisterActivity::class.java))
            finish()
        } else {

            var myAdapter = AdapterMainCategory(this)
            recycler_view.layoutManager =
                GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false)
            recycler_view.adapter = myAdapter

            progress_circular.visibility = View.VISIBLE
            getData(myAdapter)
            progress_circular.visibility = View.INVISIBLE
        }
    }

    private fun getData(myAdapter: AdapterMainCategory) {

        var requestQueue = Volley.newRequestQueue(this)
        var request =
            StringRequest(Request.Method.GET, Endpoints.getCategoryURL(), Response.Listener {
                var gson = GsonBuilder().create()
                var categoryResponse = gson.fromJson(it, CategoryResponse::class.java)

                myAdapter.setData(categoryResponse.data)


            }, Response.ErrorListener {
                this.toast("Response Failed")
            })

        requestQueue.add(request)

    }

    private fun setItemCountInCart() {
        countTextView?.text = getTotalCountInCart().toString()
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
                    rootView?.text_view_user_setting?.setOnClickListener {
                        startActivity(Intent(this, UserProfileActivity::class.java))
                    }
                    rootView?.image_view_person?.setOnClickListener {
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
        for (item in cartList) {
            itemCountInCart += dbHelper.getItemQuantity(item._id)
        }

        return itemCountInCart
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
            else -> {
                startActivity(Intent(this, ShoppingCartActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_account -> {
                startActivity(Intent(this, UserProfileActivity::class.java))
            }
            R.id.item_logout -> {
                sessionManager.logout()
                startActivity(Intent(this,LoginRegisterActivity::class.java))
            }
            R.id.item_order ->{
                startActivity(Intent(this,OrderActivity::class.java))
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

}



