package com.example.project1.activities


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.AbsListView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.example.project1.models.Category
import com.example.project1.models.CategoryResponse
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.navigation.NavigationView
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.action_layout_title_menu.view.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_payment.*
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.low_category_adapter.view.*
import kotlinx.android.synthetic.main.main_menu.view.*
import kotlinx.android.synthetic.main.nav_header_layout.view.*
import kotlinx.android.synthetic.main.placeholder_layout_main_cat.*
import kotlinx.android.synthetic.main.shopping_cart_menu.view.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    AdapterMainCategory.OnAdapterListner {


    lateinit var dbHelper: DBHelper
    lateinit var sessionManager: SessionManager
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private var countTextView: TextView? = null
    private var userNameTextView: TextView? = null
    private var shimmerFrameLayout: ShimmerFrameLayout? = null
    var lastVisiblePosition = 0
    var animatedPosition = -1
    var viewList: ArrayList<View> = ArrayList()
    var catList: ArrayList<Category> = ArrayList()
    var handler = Handler()
    var delayedTime: Long = 500


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

        sessionManager = SessionManager(this)
        dbHelper = DBHelper(this)

        toolbar(getString(R.string.app_name))


        setDrawer()

        var myAdapter = AdapterMainCategory(this)

        var dividerItemDecoration = DividerItemDecoration(this,LinearLayoutManager.VERTICAL)
        dividerItemDecoration.setDrawable(resources.getDrawable(R.drawable.custom_divider)!!)
        recycler_view.addItemDecoration(dividerItemDecoration)
        recycler_view.layoutManager =
            LinearLayoutManager(this)
        recycler_view.adapter = myAdapter
        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                Log.d("anim1", "onScrolled called")

                lastVisiblePosition =
                    (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastVisibleItemPosition()
                Log.d("anim1", lastVisiblePosition.toString())
            }
        })
        lastVisiblePosition =
            (recycler_view.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
        Log.d("anim1", lastVisiblePosition.toString())
        myAdapter.setOnAdapterListenr(this)

        shimmerFrameLayout = shimmer_view_container
        shimmerFrameLayout!!.visibility = View.VISIBLE
        shimmerFrameLayout!!.startShimmer()


        getData(myAdapter)

    }

    private fun setDrawer() {
        drawerLayout = drawer
        navView = nav_view
        navView.setItemTextAppearance(R.style.menu_in_nav_style)
        var headerView = navView.getHeaderView(0)
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar_main, 0, 0)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)
        headerView.text_view_user_name_nav.text = sessionManager.getUserName()
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
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer((GravityCompat.START))
                } else {
                    drawerLayout.openDrawer(GravityCompat.START)
//                    animMenuStart(navView.menu)
                }
            }
            else -> {
                startActivity(Intent(this, ShoppingCartActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun animMenuStart(menu: Menu) {
        for (i in 0 until menu.size()) {
            var itemMenu = menu.getItem(i)
            when (itemMenu.itemId) {
                R.id.item_account -> {
                    var animation =
                        AnimationUtils.loadAnimation(this, R.anim.anim_show_order_summary)
                    itemMenu.setActionView(R.layout.action_layout_title_menu)
                    itemMenu.actionView.startAnimation(animation)
                }
                R.id.item_order -> {
                    var animation =
                        AnimationUtils.loadAnimation(this, R.anim.anim_show_order_summary)
                    itemMenu.setActionView(R.layout.action_layout_title_menu)
                    itemMenu.actionView.text_view_action_layout.text = "MY ACCOUNT"
                    itemMenu.actionView.startAnimation(animation)
                }
            }
        }

    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_account -> {
                startActivity(Intent(this, UserProfileActivity::class.java))
            }
            R.id.item_logout -> {
                sessionManager.logout()
                startActivity(Intent(this, LoginRegisterActivity::class.java))
            }
            R.id.item_order -> {
                startActivity(Intent(this, OrderActivity::class.java))
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

    override fun imageLoadSuccess(view: View, cat: Category, position: Int) {

        viewList.add(view)
        catList.add(cat)

        if (isImageLoadCompleted(viewList.size)) {
            handleAnimation(viewList, catList)
        }
    }


    private fun handleAnimation(viewList: ArrayList<View>, catList: ArrayList<Category>) {
        shimmerFrameLayout!!.visibility = View.INVISIBLE
        shimmerFrameLayout!!.stopShimmer()
        content_layout_main.visibility = View.VISIBLE
        handler.postDelayed({
            for (i in viewList.indices) {
                viewList[i].text_view_cat_name.text = catList[i].catName
                var animation: Animation =
                    AnimationUtils.loadAnimation(this, R.anim.anim_text_view_cat_name)
                if (i > animatedPosition)
                    viewList[i].text_view_cat_name.startAnimation(animation)
            }
            if (animatedPosition < lastVisiblePosition) {
                animatedPosition = lastVisiblePosition
            }
        }, delayedTime)
    }

    private fun isImageLoadCompleted(size: Int): Boolean {
        return size == lastVisiblePosition + 1
    }

}



