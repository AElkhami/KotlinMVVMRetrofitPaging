package com.example.kotlinretrofitmvvm.data

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Created by A.Elkhami on 1/2/2020.
 */
class APIClient {

    companion object {
        const val API_KEY = "68f5b38e37e174c0f411dcdad76698f7"
        const val baseURL = "https://www.flickr.com/"
    }

    private fun retrofitBuilder(): Retrofit {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient().newBuilder().addInterceptor(loggingInterceptor).build()

        val gson = GsonBuilder()
            .setLenient()
            .create()

        return Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClient)
            .build()
    }

    val apiInterface: APIInterface = retrofitBuilder().create(APIInterface::class.java)

}


fun <T> Call<T>.enqueue(callback: CallBack<T>.() -> Unit) {
    val callBack = CallBack<T>()
    callback.invoke(callBack)
    this.enqueue(callBack)
}

class CallBack<T> : Callback<T> {

    var onResponse: ((Response<T>) -> Unit)? = null
    var onFailure: ((t: Throwable?) -> Unit)? = null

    override fun onFailure(call: Call<T>, t: Throwable) {
        onFailure?.invoke(t)
    }

    override fun onResponse(call: Call<T>, response: Response<T>) {
        onResponse?.invoke(response)
    }

}