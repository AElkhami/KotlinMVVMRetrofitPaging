package com.example.kotlinretrofitmvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * Created by A.Elkhami on 1/13/2020.
 */

fun <T> MutableLiveData<T>.toLiveData(): LiveData<T> {
    return this
}