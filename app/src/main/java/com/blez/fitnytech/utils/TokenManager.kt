package com.blez.fitnytech.utils

import android.content.Context
import android.content.SharedPreferences
import com.blez.fitnytech.utils.Constants.PREFS_TOKEN_FILE
import com.blez.fitnytech.utils.Constants.USER_EMAIL
import com.blez.fitnytech.utils.Constants.USER_NAME
import com.blez.fitnytech.utils.Constants.USER_TOKEN


class TokenManager(context: Context) {
    private var prefs : SharedPreferences = context.getSharedPreferences(PREFS_TOKEN_FILE,Context.MODE_PRIVATE)

    fun saveToken(token : String)
    {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN,token)
        editor.apply()
    }
    fun saveEmail(email : String)
    {
        val editor = prefs.edit()
        editor.putString(USER_EMAIL,email)
        editor.apply()
    }
    fun saveUserName(username : String){
        val editor = prefs.edit()
        editor.putString(USER_NAME,username)
        editor.apply()
    }
    fun getUserName() : String?{
        return prefs.getString(USER_NAME,null)
    }



    fun getToken() : String?
    {
        return prefs.getString(USER_TOKEN,null)
    }
    fun getEmail() : String?{
        return prefs.getString(USER_EMAIL,null)
    }
    fun deteleCredit(){
            val editor = prefs.edit()
            editor.clear()
            editor.apply()
    }

}