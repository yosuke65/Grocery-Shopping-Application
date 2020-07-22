package com.example.project1.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.example.project1.R
import com.example.project1.apps.Endpoints
import com.example.project1.database.DBHelper
import com.example.project1.helpers.SessionManager
import com.example.project1.helpers.toolbar
import com.example.project1.models.Product
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_product_detail.*
import kotlinx.android.synthetic.main.main_menu.view.*
import kotlinx.android.synthetic.main.shopping_cart_menu.view.*

class ProductDetailActivity : AppCompatActivity() {

    lateinit var dbHelper:DBHelper
    lateinit var sessionManager: SessionManager
    private var userNameTextView: TextView? = null
    private var countTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        init()
    }

    override fun onResume() {
        var product = intent.getSerializableExtra(Product.KEY) as Product
        getItemCount(product._id)
        setItemCountInCart()
        super.onResume()
    }



    private fun init() {

        sessionManager = SessionManager(this)
        dbHelper = DBHelper(this)
        var product = intent.getSerializableExtra(Product.KEY) as Product


        getItemCount(product._id)

        text_view_product_name_detail.text = product.productName
        text_view_product_price_detail.text = "$${"%.2f".format(product.price)}"
        text_view_product_description_detail.text = product.description
        Picasso.get().load(Endpoints.getImageURL(product.image!!)).error(R.drawable.icon_no_image)
            .into(image_view_product_detail)

        button_add_detail.setOnClickListener{
            addToCart(product)
        }
        button_increment_detail.setOnClickListener{
            incrementItemQuantity(product._id)
        }
        button_decrement_detail.setOnClickListener{
            decrementItemQuantity(product._id)
        }


        this.toolbar(product.productName)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


    }

    private fun getItemCount(id:String) {
        var count:Int = dbHelper.getItemQuantity(id)
        if(count > 0){
            button_add_detail.visibility = View.INVISIBLE
            button_increment_decrement_detail.visibility = View.VISIBLE
            text_view_count_detail.text = count.toString()
        }else{
            button_add_detail.visibility = View.VISIBLE
            button_increment_decrement_detail.visibility = View.INVISIBLE
        }
    }

    private fun addToCart(product: Product) {

        button_increment_decrement_detail.visibility = View.VISIBLE
        button_add_detail.visibility = View.INVISIBLE
        dbHelper.addToCart(product,1)
        setItemCountInCart()
    }

    private fun decrementItemQuantity(id: String) {

        var count = text_view_count_detail.text.toString().toInt()
        if (count === 1) {
            button_add_detail.visibility = View.VISIBLE
            button_increment_decrement_detail.visibility = View.INVISIBLE
            removeFromCart(id)
        } else {
            count--
            text_view_count_detail.text = count.toString()
            dbHelper.updateItem(id, count)
        }
        setItemCountInCart()

    }

    private fun removeFromCart(id: String) {
        dbHelper.deleteItem(id)
    }

    private fun incrementItemQuantity(id:String) {
        var count = text_view_count_detail.text.toString().toInt()
        count++
        text_view_count_detail.text = count.toString()
        dbHelper.updateItem(id, count)
        setItemCountInCart()
    }

    private fun getTotalCountInCart(): Int {
        var itemCountInCart = 0
        var cartList = dbHelper.readCart()
        for(item in cartList){
            itemCountInCart += dbHelper.getItemQuantity(item._id)
        }

        return itemCountInCart
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


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
            }
            else ->{
                startActivity(Intent(this, ShoppingCartActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}