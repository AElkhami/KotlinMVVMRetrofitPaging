package com.example.kotlinretrofitmvvm.data

import com.example.kotlinretrofitmvvm.data.APIClient.Companion.API_KEY
import com.example.kotlinretrofitmvvm.data.models.FlickrResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by A.Elkhami on 1/2/2020.
 */
interface APIInterface {

    @GET("services/rest/?method=flickr.photos.search&format=json&nojsoncallback=1&sort=relevance")
    fun searchFlickrPhotos(@Query("text") photoSearchTerm: String,
                           @Query("page") page: Int = 1,
                           @Query("api_key") apiKey: String = API_KEY)
            : Single<FlickrResponse>
}