package com.example.project1.helpers

import android.content.Context

class SessionManager(mContext: Context) {

    var sharedPreferences = mContext.getSharedPreferences(FILE_USER_INFO, Context.MODE_PRIVATE)
    var editor = sharedPreferences.edit()

    companion object {
        const val FILE_USER_INFO = "user_info"
        const val KEY_USER_NAME = "user_name"
        const val KEY_USER_EMAIL = "user_email"
        const val KEY_USER_MOBILE = "user_mobile"
        const val KEY_TOKEN = "token"
        const val KEY_IS_LOGGED_IN = "is_logged_in"
    }

    fun register(name: String, email: String, mobile: String, password: String) {
        editor.putString(KEY_USER_NAME, name)
        editor.putString(KEY_USER_EMAIL, email)
        editor.putString(KEY_USER_MOBILE, mobile)
        editor.putBoolean(KEY_IS_LOGGED_IN, false)
        editor.commit()
    }

    fun getUser(): String? {
        return sharedPreferences.getString(KEY_USER_NAME, null)
    }
    fun getEmail(): String? {
        return sharedPreferences.getString(KEY_USER_EMAIL, null)
    }
    fun getMobile(): String? {
        return sharedPreferences.getString(KEY_USER_MOBILE, null)
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun login(name: String, email: String, mobile: String, token: String) {
        editor.putString(KEY_TOKEN, token)
        editor.putString(KEY_USER_NAME, name)
        editor.putString(KEY_USER_EMAIL, email)
        editor.putString(KEY_USER_MOBILE, mobile)
        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.commit()
//        var savedEmail = sharedPreferences.getString(KEY_USER_EMAIL, null)
//        var savedPassword = sharedPreferences.getString(KEY_USER_PASSWORD, null)
//
//        return if (email == savedEmail && password == savedPassword) {
//
//            editor.putBoolean(KEY_IS_LOGGED_IN, true)
//            editor.commit()
//            true
//        } else {
//            false
//        }
    }

    fun logout() {
        editor.clear().commit()
    }
}