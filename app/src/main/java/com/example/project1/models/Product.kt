package com.example.project1.models

import java.io.Serializable

data class Product(
    val __v: Int?,
    val _id: String,
    val catId: Int?,
    val created: String?,
    val description: String?,
    val image: String,
    val mrp: Double,
    val position: Int?,
    val price: Double,
    val productName: String,
    val quantity: Int,
    val status: Boolean?,
    val subId: Int?,
    val unit: String?,
    var orderDate:String?,
    var orderStatus:String?
):Serializable {

    companion object{
        const val KEY = "product_key"
    }
}