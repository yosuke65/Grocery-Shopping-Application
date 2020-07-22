package com.example.project1.models

import android.os.Parcel
import android.os.Parcelable

data class User(
    val __v: Int?,
    val _id: String?,
    val createdAt: String?,
    val email: String?,
    val firstName: String?,
    val mobile: String?,
    val password: String?
):Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(__v!!)
        parcel.writeString(_id)
        parcel.writeString(createdAt)
        parcel.writeString(email)
        parcel.writeString(firstName)
        parcel.writeString(mobile)
        parcel.writeString(password)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }

}