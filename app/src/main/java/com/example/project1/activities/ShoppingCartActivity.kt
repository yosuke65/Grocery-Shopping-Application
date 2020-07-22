package com.example.project1.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project1.R
import com.example.project1.adapters.AdapterShoppingCartList
import com.example.project1.database.DBHelper
import com.example.project1.helpers.SessionManager
import com.example.project1.helpers.toolbar
import com.example.project1.models.Product
import kotlinx.android.synthetic.main.activity_shopping_cart.*
import kotlinx.android.synthetic.main.main_menu.view.*
import kotlinx.android.synthetic.main.row_shopping_cart_adapter.view.*
import kotlinx.android.synthetic.main.shopping_cart_menu.view.*


class ShoppingCartActivity : AppCompatActivity(), AdapterShoppingCartList.OnAdapterInteraction {

    var mList: ArrayList<Product> = ArrayList()
    lateinit var dbHelper: DBHelper
    lateinit var sessionManager: SessionManager
    private var userNameTextView:TextView? = null
    private var countTextView:TextView? = null

    companion object {
        const val SHIPPING_FEE = 4.99
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_cart)

        init()
    }

    private fun init() {
        sessionManager = SessionManager(this)
        dbHelper = DBHelper(this)

        this.toolbar(getString(R.string.titleShoppingCart))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mList = dbHelper.readCart()


        setOrderSummary()

        if (dbHelper.isProductInCart()) {
            showEmptyMessage(false)
            showOrderSummary(true)
        }else{
            showEmptyMessage(true)
            showOrderSummary(false)
        }

        button_checkout.setOnClickListener{
            startActivity(Intent(this,AddressActivity::class.java))
        }



        recycler_view_shopping_cart.layoutManager = LinearLayoutManager(this)
        var myAdapter = AdapterShoppingCartList(this, mList)
        myAdapter.setOnAdapterInteraction(this)
        recycler_view_shopping_cart.adapter = myAdapter
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
            R.id.action_go_to_cart ->{
                startActivity(Intent(this,ShoppingCartActivity::class.java))
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemClicked(view: View, operation: String, position: Int, product: Product) {
        when (operation) {
            AdapterShoppingCartList.REMOVE -> {
                removeFromCart(product)
            }
            AdapterShoppingCartList.INCREMENT -> {
                incrementItemQuantity(view, product._id)
            }
            AdapterShoppingCartList.DECREMENT -> {
                decrementItemQuantity(view, product._id)
            }
        }
    }

    private fun incrementItemQuantity(view: View, id: String) {
        var count = view.text_view_count_cart.text.toString().toInt()
//        view.button_decrement_cart.visibility = View.VISIBLE
        view.button_decrement_cart.isEnabled = true
        count++
        view.text_view_count_cart.text = count.toString()
        dbHelper.updateItem(id, count)
        setOrderSummary()
        setItemCountInCart()
    }

    private fun decrementItemQuantity(view: View, id: String) {
        var count = view.text_view_count_cart.text.toString().toInt()

        if (count == 1) {
            view.button_decrement_cart.isEnabled = false
        } else {
            count--
            view.text_view_count_cart.text = count.toString()
        }
        dbHelper.updateItem(id, count)
        setOrderSummary()
        setItemCountInCart()
    }

    private fun removeFromCart(product: Product) {
        mList.remove(product)
        dbHelper.deleteItem(product._id)
        if (mList.isEmpty()) {
            showEmptyMessage(true)
            animOrderSummary()
        }
        setOrderSummary()
        setItemCountInCart()
    }

    private fun animOrderSummary() {
        var animation: Animation =
            AnimationUtils.loadAnimation(this, R.anim.anim_order_summary)
        order_summary.startAnimation(animation)
    }

    private fun showOrderSummary(flag: Boolean) {
        if (flag) {
            order_summary.visibility = View.VISIBLE
        } else {
            order_summary.visibility = View.INVISIBLE
        }
    }

    private fun showEmptyMessage(flag: Boolean) {
        if (flag) {
            text_view_cart_empty.visibility = View.VISIBLE
        } else {
            text_view_cart_empty.visibility = View.INVISIBLE
        }
    }

    private fun setItemCountInCart() {
        countTextView?.text = getTotalCountInCart().toString()
    }

    private fun setOrderSummary() {
        text_view_shipping_fee.text = "$$SHIPPING_FEE"
        var subTotal = getSubTotal(mList)
        var orderTotal = subTotal + SHIPPING_FEE
        text_view_sub_total_price.text = "$${"%.2f".format(subTotal)}"
        text_view_total_price.text = "$${"%.2f".format(orderTotal)}"
    }

    private fun getSubTotal(mList: ArrayList<Product>): Double {
        var subTotal = 0.00
        for (product in mList) {
            subTotal += product.price * dbHelper.getItemQuantity(product._id)
        }
        return subTotal
    }

}