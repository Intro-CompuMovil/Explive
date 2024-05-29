package com.example.explive

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User (
    var uid: String,
    var name: String,
    var lastname: String,
    var email: String,
    var password: String,
    var city: String,
    var admin: Boolean
): Parcelable{
    constructor() : this("","","","", "", "", false)
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "name" to name,
            "email" to email,
            "password" to password,
            "city" to city,
            "admin" to admin
        )
    }

}