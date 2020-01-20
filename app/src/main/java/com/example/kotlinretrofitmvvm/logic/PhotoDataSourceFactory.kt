package com.example.kotlinretrofitmvvm.logic

import androidx.paging.DataSource
import com.example.kotlinretrofitmvvm.data.models.Photo
import io.reactivex.disposables.CompositeDisposable


/**
 * Created by A.Elkhami on 1/5/2020.
 */
class PhotoDataSourceFactory(query: String):  DataSource.Factory<Int, Photo>() {
    private val dataSource = PhotoDataSource(query)

    override fun create(): DataSource<Int, Photo> {
        getCompositeDisposable()
        return dataSource
    }

    fun getCompositeDisposable(): CompositeDisposable {
       return dataSource.handleDisposables()
    }

    fun getDataSource(): PhotoDataSource {
        return dataSource
    }
}