package com.example.project1.models

import java.io.Serializable

data class Category(
    val __v: Int,
    val _id: String,
    val catDescription: String,
    val catId: Int,
    val catImage: String,
    val catName: String,
    val position: Int,
    val slug: String,
    val status: Boolean
): Serializable{
    companion object{
        const val CAT_ID = "cat_id"
        const val CATEGORY = "category"
    }
}