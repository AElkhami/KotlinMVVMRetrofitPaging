package com.example.kotlinretrofitmvvm.logic

import androidx.paging.PageKeyedDataSource
import com.example.kotlinretrofitmvvm.data.APIClient
import com.example.kotlinretrofitmvvm.data.APIClient.Companion.API_KEY
import com.example.kotlinretrofitmvvm.data.models.FlickrResponse
import com.example.kotlinretrofitmvvm.data.models.Photo
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by A.Elkhami on 1/5/2020.
 */
class PhotoDataSource(private val searchTerm: String) : PageKeyedDataSource<Int, Photo>() {


    private val compositeDisposable = CompositeDisposable()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Photo>
    ) {

        val request: Single<FlickrResponse> =
            APIClient()
                .apiInterface
                .searchFlickrPhotos(photoSearchTerm = searchTerm, apiKey = API_KEY)

        val onLoadInitialDisposable = request.subscribe { response ->

            if (response?.photos?.total == 0) {
                callback.onResult(
                    response.photos.photo!!,
                    0, 0, null, null
                )
            } else {
                val photo = response?.photos?.photo!!
                //List size + position too large, last item in list beyond totalCount
                callback.onResult(
                    photo,
                    response.photos.page?.times(response.photos.perpage!!)!!,
                    response.photos.total!!,
                    null,
                    response.photos.page + 1
                )
            }

        }

        compositeDisposable.add(onLoadInitialDisposable)
    }


    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Photo>) {

        val request: Single<FlickrResponse> =
            APIClient()
                .apiInterface
                .searchFlickrPhotos(searchTerm, page = params.key)

        val onLoadAfterDisposable = request.subscribe { response ->
            val page = response.photos?.page!!
            val pages = response.photos.pages!!
            var nextPage: Int? = null

            if (page < pages) {
                nextPage = page + 1
            }
            callback.onResult(response.photos.photo!!, nextPage!!)
        }

        compositeDisposable.add(onLoadAfterDisposable)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Photo>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    fun handleDisposables(): CompositeDisposable {
        return compositeDisposable
    }

}