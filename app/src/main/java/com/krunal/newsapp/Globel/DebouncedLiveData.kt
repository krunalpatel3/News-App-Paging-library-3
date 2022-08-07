package com.krunal.newsapp.Globel

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer

class DebouncedLiveData<T> : MediatorLiveData<T> {

    private lateinit var mSource: LiveData<T>
    private var mDuration = 0
    private val debounceRunnable = Runnable { postValue(mSource.value) }
    private val handler = Looper.myLooper()?.let { Handler(it) }

    @SuppressLint("NotConstructor")
    constructor(source: LiveData<T>, duration: Int) {
        mSource = source
        mDuration = duration
        addSource(mSource, Observer {
            handler?.removeCallbacks(debounceRunnable)
            handler?.postDelayed(debounceRunnable, mDuration.toLong())
        })
    }
}