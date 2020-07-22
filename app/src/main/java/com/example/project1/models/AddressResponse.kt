package com.example.project1.models

data class AddressResponse(
    val count: Int,
    val `data`: ArrayList<Address>,
    val error: Boolean
)
