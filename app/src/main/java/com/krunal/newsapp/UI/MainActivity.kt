package com.krunal.newsapp.UI

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.viewpager.widget.ViewPager
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.krunal.newsapp.Adapter.ViewPagerAdapter
import com.krunal.newsapp.Globel.MyPref
import com.krunal.newsapp.Globel.Utility.Companion.EMAIL
import com.krunal.newsapp.Globel.Utility.Companion.public_profile
import com.krunal.newsapp.R
import com.krunal.newsapp.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import org.json.JSONException
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var dots: Array<ImageView>
    private lateinit var callbackManager: CallbackManager
    private var dotCount: Int = 0
    private var timer: Timer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        drawSliderDotSymbols()
        setEventListener()

        val thread = Thread {
            val isLogin = MyPref.getInstance(applicationContext)
                ?.getFromPrefs(
                    applicationContext, MyPref.isLogin, false
                )

            runOnUiThread {
                if (isLogin as Boolean) {
                    startActivity(
                        Intent(
                            applicationContext,
                            NewsActivity::class.java
                        )
                    )
                    finish()
                }
            }
        }
        thread.start()


        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)


        val resultGoogleLauncher = registerForActivityResult(StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // Check for existing Google Sign In account, if the user is already signed in
                // the GoogleSignInAccount will be non-null.
                val account = GoogleSignIn.getLastSignedInAccount(this)
                account?.let { handleGoogleSignInResult(it) }
            }
        }

        binding.ivSignInWithGoogle.setOnClickListener {
            val intent = mGoogleSignInClient.signInIntent
            resultGoogleLauncher.launch(intent)
        }

        callbackManager = CallbackManager.Factory.create();

        val loginManager = LoginManager.getInstance()

        loginManager.registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    // App code

                    val request = GraphRequest.newMeRequest(
                        result.accessToken
                    ) { `object`, _ ->
                        if (`object` != null) {
                            try {
                                val name = `object`.getString("name")
                                val email = `object`.getString("email")
                                val fbUserID = `object`.getString("id")


                                if (name.isNotEmpty() or email.isNotEmpty() or fbUserID.isNotEmpty()) {

                                    MyPref.getInstance(applicationContext)
                                        ?.saveToPrefs(
                                            applicationContext, MyPref.isLogin, true
                                        )

                                    startActivity(
                                        Intent(
                                            applicationContext,
                                            NewsActivity::class.java
                                        )
                                    )
                                    finish()
                                }

                                // do action after Facebook login success
                                // or call your API
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            } catch (e: NullPointerException) {
                                e.printStackTrace()
                            }
                        }
                    }

                    val parameters = Bundle()
                    parameters.putString(
                        "fields",
                        "id, name, email"
                    )
                    request.parameters = parameters
                    request.executeAsync()
                }

                override fun onCancel() {
                    // App code
                    Log.e(
                        "tab", "onCancel:"
                    )
                }

                override fun onError(error: FacebookException) {
                    // App code
                    Log.e(
                        "tab", "onError:"
                    )

                }
            })


        binding.ivSignInWithFB.setOnClickListener {

            if (isFbLoggedIn()) {
                startActivity(Intent(this, NewsActivity::class.java))
            } else {
                loginManager.logInWithReadPermissions(
                    this,
                    listOf(EMAIL, public_profile)
                )
            }

        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun drawSliderDotSymbols() {
        val viewPagerAdapter = ViewPagerAdapter(this)
        binding.viewPager.adapter = viewPagerAdapter
        dotCount = viewPagerAdapter.count
//        val sliderDotspanel = binding.sliderDots
        dots = Array(dotCount) { ImageView(this) }
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(8, 0, 8, 0)

        val timerTask: TimerTask = object : TimerTask() {
            override fun run() {
                binding.viewPager.post {
                    binding.viewPager.currentItem =
                        (binding.viewPager.getCurrentItem() + 1) % dotCount
                }
            }
        }
        timer = Timer()
        timer!!.schedule(timerTask, 3000, 3000)

//        repeat(dotCount) {
//            dots[it].setImageDrawable(nonActiveDot)
//
//            Log.e("tag", "repeat: $it");
//        }
//        dots[0].setImageDrawable(activeDot)
    }


    private fun setEventListener() {
        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
//                repeat(dotCount) {
//                    dots[it].setImageDrawable(nonActiveDot)
//                }
//                dots[position].setImageDrawable(activeDot)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }


    @SuppressLint("ResourceAsColor")
    override fun onResume() {
        super.onResume()
        // Transparent StatusBar
        //make statusBar content dark
        WindowCompat.getInsetsController(window, window.decorView)
            .isAppearanceLightStatusBars = true

        window.statusBarColor = Color.TRANSPARENT

    }

    private fun handleGoogleSignInResult(completedTask: GoogleSignInAccount) {
        try {

            // Signed in successfully
            val googleId = completedTask.id ?: ""
            Log.i("Google ID", googleId)
            val googleFirstName = completedTask.displayName ?: ""
            Log.i("Google First Name", googleFirstName)
            val googleLastName = completedTask.familyName ?: ""
            Log.i("Google Last Name", googleLastName)
            val googleEmail = completedTask.email ?: ""
            Log.i("Google Email", googleEmail)
            val googleProfilePicURL = completedTask.photoUrl.toString()
            Log.i("Google Profile Pic URL", googleProfilePicURL)
            val googleIdToken = completedTask.idToken ?: ""
            Log.i("Google ID Token", googleIdToken)

            if (googleEmail.isNotEmpty() or googleFirstName.isNotEmpty()) {
                MyPref.getInstance(applicationContext)
                    ?.saveToPrefs(
                        applicationContext, MyPref.isLogin, true
                    )
                startActivity(Intent(this, NewsActivity::class.java))
                finish()
                Toast.makeText(
                    this, getString(R.string.login_successfully), Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.failed_to_login_please_try_again_later),
                    Toast.LENGTH_SHORT
                ).show()
            }

        } catch (e: ApiException) {
            // Sign in was unsuccessful
            Toast.makeText(
                this, getString(R.string.failed_to_login_please_try_again_later), Toast.LENGTH_SHORT
            ).show()
            Log.e(
                "failed code=", e.statusCode.toString()
            )
        }
    }

    override fun onStop() {
        super.onStop()
        timer?.cancel()
    }

    private fun isFbLoggedIn(): Boolean {
        val accessToken = AccessToken.getCurrentAccessToken()
        return accessToken != null && !accessToken.isExpired
    }

}


