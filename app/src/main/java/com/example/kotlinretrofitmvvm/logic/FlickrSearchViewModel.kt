package com.example.kotlinretrofitmvvm.logic

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.kotlinretrofitmvvm.data.models.Photo

/**
 * Created by A.Elkhami on 1/2/2020.
 */
class FlickrSearchViewModel : ViewModel() {

//    private var photosMutableLiveData = MutableLiveData<PagedList<Photo>>()
//    fun photosLiveData() = photosMutableLiveData.toLiveData()
    private var photosLiveData: LiveData<PagedList<Photo>>? = null
    private var dataSourceFactory: PhotoDataSourceFactory? = null

    private val config = PagedList.Config.Builder()
        .setPageSize(10)
        .setInitialLoadSizeHint(10)
        .setEnablePlaceholders(false)
        .build()


    fun searchFlickrPhotos(searchTerm: String): LiveData<PagedList<Photo>> {

        dataSourceFactory =
            PhotoDataSourceFactory(
                searchTerm
            )

        photosLiveData = LivePagedListBuilder<Int, Photo>(dataSourceFactory!!, config)
            .setInitialLoadKey(1)
            .build()

        return photosLiveData as LiveData<PagedList<Photo>>
    }

    override fun onCleared() {
        super.onCleared()
        dataSourceFactory?.getCompositeDisposable()?.dispose()
        Log.i("disposableTag","items are disposed")
    }

}