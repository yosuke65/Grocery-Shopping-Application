package com.example.project1.models

data class OrderResponse(
    val count: Int,
    val `data`: ArrayList<Order>,
    val error: Boolean
)

