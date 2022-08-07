package com.krunal.newsapp.UI

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.krunal.newsapp.Adapter.NewsAdapter
import com.krunal.newsapp.DataBase.AppDatabase
import com.krunal.newsapp.Globel.ApiClient
import com.krunal.newsapp.Globel.MyPref
import com.krunal.newsapp.Globel.RetrofitInterface
import com.krunal.newsapp.Globel.Utility.Companion.URL
import com.krunal.newsapp.Globel.Utility.Companion.logOutFromFacebook
import com.krunal.newsapp.Globel.Utility.Companion.signOutFromGoogle
import com.krunal.newsapp.R
import com.krunal.newsapp.ViewModel.MyViewModelFactory
import com.krunal.newsapp.ViewModel.NewsActivityVM
import com.krunal.newsapp.databinding.ActivityNewsBinding
import kotlinx.coroutines.flow.collectLatest


class NewsActivity : AppCompatActivity() {

    lateinit var binding: ActivityNewsBinding
    private lateinit var newsAdapter: NewsAdapter

    @OptIn(ExperimentalPagingApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val myInterface: RetrofitInterface = ApiClient.getRetrofitInstance()
            .let{ it?.create(RetrofitInterface::class.java)!! }

        val viewModel: NewsActivityVM by viewModels { MyViewModelFactory(
            AppDatabase.getInstance(this)!!, myInterface) }

        binding.swipeRefresh.isRefreshing = true

        newsAdapter = NewsAdapter()

        binding.rv.adapter = newsAdapter
        binding.rv.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launchWhenStarted {
            viewModel.getAllNews().collectLatest { response->
                binding.swipeRefresh.isRefreshing = false
                newsAdapter.submitData(response)
            }
        }

        binding.edSearch.keyListener = null;

        newsAdapter.setOnItemClickListener {
            val intent = Intent(this,WebViewActivity::class.java)
            intent.putExtra(URL,it.url)
            startActivity(
                intent)
        }

        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = true
            newsAdapter.refresh()
        }

        binding.ivLogout.setOnClickListener {

            MaterialAlertDialogBuilder(this)
                .setTitle(resources.getString(R.string.dialog_logout_title))
                .setMessage(resources.getString(R.string.dialog_logout_message))
                .setNegativeButton(resources.getString(R.string.No)) { dialog, _ ->
                    dialog.cancel()
                }
                .setPositiveButton(resources.getString(R.string.Yes)) { _, _ ->

                    logOutFromFacebook()

                    signOutFromGoogle(this)

                    val thread = Thread {
                        MyPref.getInstance(this)
                            ?.saveToPrefs(
                                this, MyPref.isLogin, false
                            )
                    }
                    thread.start()

                    startActivity(Intent(
                            this,
                            MainActivity::class.java))
                    finish()
                }.show()

        }

        binding.edSearch.setOnClickListener{
            startActivity(
                Intent(
                    this,
                    SearchActivity::class.java
                )
            )
        }

        binding.searchCardView.setOnClickListener{
            startActivity(
                Intent(
                    this,
                    SearchActivity::class.java
                )
            )
        }

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

}