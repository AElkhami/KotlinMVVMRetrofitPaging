package com.example.kotlinretrofitmvvm.data

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by A.Elkhami on 1/19/2020.
 */
class SharedPrefs(context: Context) {

    private val PREF_NAME = "SEARCH-HISTORY"
    private var sharedPreferences: SharedPreferences? = null

    init {
        sharedPreferences  = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    private val editor: SharedPreferences.Editor = sharedPreferences!!.edit()

    fun saveQuery(queryStrings: Set<String>){
        editor.clear()
        editor.putStringSet("SAVED-HISTORY", queryStrings)
        editor.apply()
    }

    fun getSavedQueries(): MutableSet<String>? {
        val defaultSet: MutableSet<String>? = null
        return sharedPreferences!!.getStringSet("SAVED-HISTORY", defaultSet)
    }
}