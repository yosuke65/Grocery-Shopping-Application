package com.example.project1.models

data class CategoryResponse(
    val count: Int,
    val `data`: ArrayList<Category>,
    val error: Boolean
){}

