package com.krunal.newsapp.UI

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.krunal.newsapp.Adapter.NewsAdapter
import com.krunal.newsapp.Adapter.SearchAdapter
import com.krunal.newsapp.DataBase.Entity.NewsEntity
import com.krunal.newsapp.Globel.Utility
import com.krunal.newsapp.Globel.Utility.Companion.getJsonStrFromObject
import com.krunal.newsapp.ViewModel.SearchActivityVM
import com.krunal.newsapp.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {

    lateinit var binding: ActivitySearchBinding
    var vm: SearchActivityVM? = null
    private lateinit var adapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        vm = ViewModelProvider(this)[SearchActivityVM::class.java]

        adapter = SearchAdapter()

        binding.rv.adapter = adapter
        binding.rv.layoutManager = LinearLayoutManager(this)


        vm?.getNewsByTitle()?.observe(this, Observer { items ->
            adapter.AddItems(items)
        })


        adapter.setOnItemClickListener {
            val intent = Intent(this,WebViewActivity::class.java)
            intent.putExtra(Utility.URL,it.url)
            startActivity(
                intent)
        }

        binding.edSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(text: Editable?) {
                vm?.let { it.filterCustomerSearch.value = text.toString() }
            }

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
}