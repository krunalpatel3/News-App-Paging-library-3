package com.krunal.newsapp.Globel

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.widget.ImageView
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.HttpMethod
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.gson.GsonBuilder
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class Utility {

    companion object {

        const val RC_SIGN_IN = 1
        const val EMAIL = "email"
        const val public_profile = "public_profile"
        const val ApiKey = "2c7289536d1e48a182819ff5387c8a2f"
        const val URL = "Url"
        const val yyyy_MM_dd_T_HHmmss_format = "yyyy-MM-dd'T'HH:mm:ss"

        fun isValidEmail(email: String?): Boolean {
            if (email == null) {
                return false
            }
            val emailPattern =
                "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
            val matcher: Matcher
            val pattern = Pattern.compile(emailPattern)
            matcher = pattern.matcher(email)
            return matcher?.matches() ?: false
        }


        fun bindImage(imgView: ImageView, imgUrl: String?) {
            imgUrl?.let {
                val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
                Glide.with(imgView.context)
                    .load(imgUri)
                    .fitCenter()
                    .into(imgView)
            }
        }


        fun logOutFromFacebook() {
            if (AccessToken.getCurrentAccessToken() == null) {
                return  // already logged out
            }
            GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/permissions/",
                null,
                HttpMethod.DELETE,
                { LoginManager.getInstance().logOut() })
                .executeAsync()
        }

         fun signOutFromGoogle(context: Context) {
            // Configure sign-in to request the user's ID, email address, and basic
            // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
            // Configure sign-in to request the user's ID, email address, and basic
            // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

            // Build a GoogleSignInClient with the options specified by gso.
            val mGoogleSignInClient = GoogleSignIn.getClient(context, gso)
            mGoogleSignInClient.signOut()
        }

        fun getJsonStrFromObject(`object`: Any?): String? {
            try {
                val gson1 = GsonBuilder()
                    .serializeNulls()
                    .create()
                return gson1.toJson(`object`)
            } catch (e: Exception) {
                Log.e("Check", "Exception: " + e.message)
            }
            return ""
        }

        /**
         * get Date from given Date format string.
         *
         * @param datetime_str
         * @return number
         */
        fun getDateFromTimeFormat(datetime_str: String?, format: String?): Date? {
            var convertedCurrentDate: Date? = null
            try {
//            yyyy_MM_dd_format
                @SuppressLint("SimpleDateFormat")
                val sdf = SimpleDateFormat(format)
                convertedCurrentDate = datetime_str?.let { sdf.parse(it) }
            } catch (e: java.lang.Exception) {
                Log.e(
                    "TAG",
                    "getDateFromTimeFormat Exception: " + e.message)
            }
            return convertedCurrentDate
        }


        fun isNetworkAvailable(context: Context?): Boolean {
            if (context == null) return false
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connectivityManager != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val capabilities =
                        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                    if (capabilities != null) {
                        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                            return true
                        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                            return true
                        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                            return true
                        }
                    }
                } else {
                    try {
                        val activeNetworkInfo = connectivityManager.activeNetworkInfo
                        if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                            Log.i("update_statut", "Network is available : true")
                            return true
                        }
                    } catch (e: java.lang.Exception) {
                        Log.i("update_statut", "" + e.message)
                    }
                }
            }
            Log.i("update_statut", "Network is available : FALSE ")
            return false
        }


    }


}


