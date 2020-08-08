package com.and1ss.android.photogallery.api

import io.reactivex.rxjava3.core.Single
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface FlickrApi {

    @GET("services/rest/?method=flickr.interestingness.getList")
    fun fetchPhotos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Single<ResponseBody>

    @GET("services/rest?method=flickr.photos.search")
    fun searchPhotos(
        @Query("text") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Single<ResponseBody>
}