package com.krunal.newsapp.Globel

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager


import java.lang.ref.WeakReference

class MyPref() {

    private var sharedPreferences: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null


    @SuppressLint("CommitPrefEdits", "NotConstructor")
    constructor(context: Context) : this() {
        sharedPreferences = context.getSharedPreferences(
            "setting", Context.MODE_PRIVATE
        )
        editor = sharedPreferences?.edit()
    }


    companion object{
        val isLogin = "Login Status"

        private var mInstance: MyPref? = null
        /**
         * Get default instance of the class to keep it a singleton
         */
        fun getInstance(context: Context): MyPref? {
            if (mInstance == null) {
                mInstance = MyPref(context)
            }
            return mInstance
        }


    }


    fun saveToPrefs(context: Context?, key: String?, value: Any) {
        val contextWeakReference = WeakReference(context)
        if (contextWeakReference.get() != null) {
            val prefs = PreferenceManager.getDefaultSharedPreferences(
                contextWeakReference.get()!!
            )
            val editor = prefs.edit()
            if (value is Int) {
                editor.putInt(key, value)
            } else if (value is String) {
                editor.putString(key, value.toString())
            } else if (value is Boolean) {
                editor.putBoolean(key, value)
            } else if (value is Long) {
                editor.putLong(key, value)
            } else if (value is Float) {
                editor.putFloat(key, value)
            } else if (value is Double) {
                editor.putLong(key, java.lang.Double.doubleToRawLongBits(value))
            }
            editor.apply()
        }
    }

    fun getFromPrefs(context: Context?, key: String?, defaultValue: Any): Any? {
        val contextWeakReference = WeakReference(context)
        if (contextWeakReference.get() != null) {
            val sharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(contextWeakReference.get()!!)
            try {
                if (defaultValue is String) {
                    return sharedPrefs.getString(key, defaultValue.toString())
                } else if (defaultValue is Int) {
                    return sharedPrefs.getInt(key, defaultValue)
                } else if (defaultValue is Boolean) {
                    return sharedPrefs.getBoolean(key, defaultValue)
                } else if (defaultValue is Long) {
                    return sharedPrefs.getLong(key, defaultValue)
                } else if (defaultValue is Float) {
                    return sharedPrefs.getFloat(key, defaultValue)
                } else if (defaultValue is Double) {
                    return java.lang.Double.longBitsToDouble(
                        sharedPrefs.getLong(
                            key, java.lang.Double.doubleToLongBits(
                                defaultValue
                            )
                        )
                    )
                }
            } catch (e: Exception) {
                return defaultValue
            }
        }
        return defaultValue
    }


    fun removeFromPrefs(context: Context?, key: String?) {
        val contextWeakReference = WeakReference(context)
        if (contextWeakReference.get() != null) {
            val prefs = PreferenceManager.getDefaultSharedPreferences(contextWeakReference.get()!!)
            val editor = prefs.edit()
            editor.remove(key)
            editor.apply()
        }
    }

    fun saveToPref(str: String?) {
        editor!!.putBoolean("in_code", true)
        editor!!.putString("code", str)
        editor!!.apply()
    }

    fun getCode(): String? {
        return sharedPreferences!!.getString("code", "")
    }


}