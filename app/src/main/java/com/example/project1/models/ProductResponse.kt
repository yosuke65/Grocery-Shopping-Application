package com.example.project1.models

data class ProductResponse(
    val count: Int,
    val `data`: ArrayList<Product>,
    val error: Boolean
)
