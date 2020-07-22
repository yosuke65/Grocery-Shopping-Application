package com.example.project1.helpers

import android.content.Context
import com.example.project1.models.User

class SessionManager(mContext: Context) {

    var sharedPreferences = mContext.getSharedPreferences(FILE_USER_INFO, Context.MODE_PRIVATE)
    var editor = sharedPreferences.edit()

    companion object {
        const val FILE_USER_INFO = "user_info"
        const val KEY_USER_ID = "user_id"
        const val KEY_USER_NAME = "user_name"
        const val KEY_USER_EMAIL = "user_email"
        const val KEY_USER_MOBILE = "user_mobile"
        const val KEY_TOKEN = "token"
        const val KEY_IS_LOGGED_IN = "is_logged_in"
    }

    fun getUserName(): String? {
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

    fun getUserId():String?{
        return sharedPreferences.getString(KEY_USER_ID, null)
    }

    fun getUser(): User?{
        return User(null,getUserId(),null,getEmail(),getUserName(),getMobile(),null )
    }


    fun getToken():String?{
        return sharedPreferences.getString(KEY_TOKEN, null)
    }

    fun login(id:String, name: String, email: String, mobile: String, token: String) {
        editor.putString(KEY_TOKEN, token)
        editor.putString(KEY_USER_ID, id)
        editor.putString(KEY_USER_NAME, name)
        editor.putString(KEY_USER_EMAIL, email)
        editor.putString(KEY_USER_MOBILE, mobile)
        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.commit()
    }

    fun logout() {
        editor.clear().commit()
    }
}