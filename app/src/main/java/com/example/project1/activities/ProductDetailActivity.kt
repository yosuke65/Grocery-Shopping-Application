package com.example.project1.activities

import android.graphics.Color
import android.graphics.ColorFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.project1.R
import com.example.project1.apps.Endpoints
import com.example.project1.helpers.toast
import com.example.project1.models.Product
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_product_detail.*
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.row_product_adapter.*

class ProductDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        init()
    }

    private fun init() {

        var product = intent.getSerializableExtra(Product.KEY) as Product
//        supportActionBar?.title = product.productName
        text_view_product_name_detail.text = product.productName
        text_view_product_price_detail.text = "$${"%.2f".format(product.price)}"
        text_view_product_description_detail.text = product.description
        Picasso.get().load(Endpoints.getImage(product.image)).error(R.drawable.icon_no_image)
            .into(image_view_product_detail)

        var toolbar = toolbar_main
        toolbar.title = product.productName
        toolbar.setTitleTextColor(Color.WHITE)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        button_back.setOnClickListener{
//            onBackPressed()
//        }

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.product_datail_menu, menu)
        return super.onCreateOptionsMenu(menu)
//        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
            }
            else ->{
                toast("Saved")
            }
        }

        return super.onOptionsItemSelected(item)
    }
}