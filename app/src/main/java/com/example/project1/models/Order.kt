package com.example.project1.models

data class Order(
    val __v: Int,
    val _id: String,
    val date: String,
    val orderStatus: String,
    val orderSummary: OrderSummary,
    val payment: Payment,
    val products: ArrayList<Product>,
    val shippingAddress: Address,
    val user: User,
    val userId: String
)