package com.example.restaurantapp

import android.content.Context
import android.content.SharedPreferences
import android.util.Pair

object PreferenceManager  {

    private const val PREF_NAME = "user_pref"
    private const val KEY_EMAIL = "email"
    private const val KEY_PASSWORD = "password"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE)
    }

    fun saveCredentials(context: Context, email: String, password: String) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(KEY_EMAIL, email)
        editor.putString(KEY_PASSWORD, password)
        editor.apply()
    }

    fun getCredentials(context: Context): Pair<String, String>? {
        val sharedPref = getSharedPreferences(context)
        val email = sharedPref.getString(KEY_EMAIL, null)
        val password = sharedPref.getString(KEY_PASSWORD, null)
        return if (email != null && password != null) {
            Pair(email, password)
        } else {
            null
        }
    }

    fun clearCredentials(context: Context) {
        val editor = getSharedPreferences(context).edit()
        editor.remove(KEY_EMAIL)
        editor.remove(KEY_PASSWORD)
        editor.apply()
    }

    // Manipulate login

    fun setLoggedIn(context: Context, isLoggedIn: Boolean) {
        val editor = getSharedPreferences(context).edit()
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
        editor.apply()
    }

    fun isLoggedIn(context: Context): Boolean {
        return getSharedPreferences(context).getBoolean(KEY_IS_LOGGED_IN, false)
    }





}