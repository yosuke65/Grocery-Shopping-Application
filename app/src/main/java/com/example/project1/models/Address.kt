package com.example.project1.models

import kotlinx.android.synthetic.main.row_address_adapter.*
import java.io.Serializable

data class Address(
    val __v: Int?,
    val _id: String,
    val city: String,
    val houseNo: String,
    val pincode: Int,
    val streetName: String,
    val type: String,
    val userId: String,
    val billingName:String = ""
): Serializable{

    companion object{
        const val KEY = "address_key"
        const val HOME = 1
    }

    fun getAddressType(type: Int): String {
        return when (type) {
            1 -> "Home"
            2 -> "Office"
            else -> "Other"
        }
    }

}