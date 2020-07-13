package com.example.project1.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.project1.R
import com.example.project1.apps.Endpoints
import com.example.project1.models.Product
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_product_detail.*
import kotlinx.android.synthetic.main.row_product_adapter.*

class ProductDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        init()
    }

    private fun init() {
        var product = intent.getSerializableExtra(Product.KEY) as Product
        text_view_product_name_detail.text = product.productName
        text_view_product_price_detail.text = "$${product.price}"
        text_view_product_description_detail.text = product.description
        Picasso.get().load(Endpoints.getImage(product.image)).error(R.drawable.icon_no_image).into(image_view_product_detail)
    }
}