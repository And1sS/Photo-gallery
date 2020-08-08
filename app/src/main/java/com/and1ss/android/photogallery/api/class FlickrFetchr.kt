package com.and1ss.android.photogallery.api

import android.util.Log
import com.and1ss.android.photogallery.model.GalleryItem
import com.google.gson.Gson
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Retrofit


private const val BASE_URL = "https://api.flickr.com/"
private const val TAG = "FlickrFetchr"

class FlickrFetchr {
    private val flickrApi: FlickrApi

    init {
        val retrofit = instance

        flickrApi = retrofit.create(FlickrApi::class.java)
    }

    fun fetchPhotosList(page: Int, perPage: Int): Observable<List<GalleryItem>> {
        return fetchPhotosPage(page, perPage)
            .map { flickrResponse ->
                flickrResponse.photoResponse?.galleryItems
                    ?: mutableListOf()
            }
            .map {
                it.filter { galleryItem ->
                    galleryItem.smallUrl.isNotEmpty() || galleryItem.originalUrl.isNotEmpty()
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun fetchPhotosPage(page: Int, perPage: Int): Observable<FlickrResponse> {
        return fetchPhotosMetadata(flickrApi.fetchPhotos(page, perPage).toObservable())
    }

    fun searchPhotosPage(query: String, page: Int, perPage: Int): Observable<FlickrResponse> {
        return fetchPhotosMetadata(flickrApi.searchPhotos(query, page, perPage).toObservable())
    }

    private fun fetchPhotosMetadata(call: Observable<ResponseBody>): Observable<FlickrResponse> {
        return call.map {
            Gson().fromJson(it.string(), FlickrResponse::class.java)
//                ObjectMapper()
//                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
//                    .readValue(
//                    it.string(),
//                    FlickrResponse::class.java
//                ) as FlickrResponse
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    companion object {
        private val _instance: Retrofit by lazy {
            val client = OkHttpClient.Builder()
                .addInterceptor(PhotoInterceptor())
                .build()

            Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .client(client)
                //.addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
        }
        val instance: Retrofit
            get() = _instance
    }
}

//sealed class ApiResponse<out T, out E> {
//    data class Error<T, E>(val error: E) : ApiResponse<T, E>()
//    data class Ok<T, E>(val result: T) : ApiResponse<T, E>()
//
//    inline fun <U> map(transform: (T) -> U): ApiResponse<U, E> = when (this) {
//        is Error -> this as Error<U, E>
//        is Ok -> Ok(transform(result))
//    }
//
//    inline fun <U> mapError(transform: (E) -> U): ApiResponse<T, U> =
//        when (this) {
//            is Error -> Error(transform(error))
//            is Ok -> this as Ok<T, U>
//        }
//}
//
//
//private fun <T> Observable<T?>.filterNotNull(): Observable<T> =
//    this.filter { it != null } as Observable<T>
//
//private fun Observable<ResponseBody>.toResponse():
//        Observable<ApiResponse<FlickrResponse, String>> =
//    map {
//        try {
//            val response =
//                ObjectMapper().readValue(it.string(), FlickrResponse::class.java) as FlickrResponse
//            val response = Gson().fromJson(
//                it.charStream(),
//                FlickrResponse::class.java
//            )
//            ApiResponse.Ok<FlickrResponse, String>(response)
//        } catch (e: Exception) {
//            ApiResponse.Error<FlickrResponse, String>(e.toString())
//        }
//    }

