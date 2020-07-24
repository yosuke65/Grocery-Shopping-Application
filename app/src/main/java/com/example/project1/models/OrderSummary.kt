package com.example.project1.models

data class OrderSummary(
    val _id: String,
    val deliveryCharges: Double,
    val discount: Double,
    val orderAmount: Double,
    val ourPrice: Double,
    val totalAmount: Double
)