package com.and1ss.android.photogallery.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.and1ss.android.photogallery.api.FlickrResponse
import io.reactivex.rxjava3.core.Observable

private const val NO_PAGES = -1

abstract class BaseDataSource : PageKeyedDataSource<Int, GalleryItem>() {
    private val _loadStatus: MutableLiveData<LoadStatus> = MutableLiveData()
    val loadStatus: LiveData<LoadStatus>
        get() = _loadStatus

    abstract fun fetchData(page: Int, perPage: Int): Observable<FlickrResponse>

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, GalleryItem>
    ) {
        _loadStatus.postValue(LoadStatus.Loading)
        fetchData(INITIAL_PAGE, PER_PAGE)
            .subscribe({ flickrResponse ->
                callback.onResult(prepareData(flickrResponse), INITIAL_PAGE, INITIAL_PAGE + 1)
                _loadStatus.postValue(LoadStatus.Loaded)
            }, {
                _loadStatus.postValue(LoadStatus.Error(it.message ?: ""))
            })
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, GalleryItem>
    ) {
        _loadStatus.postValue(LoadStatus.Loading)
        fetchData(params.key, PER_PAGE)
            .subscribe({ flickrResponse ->
                val adjasentKey =
                    if (flickrResponse.photoResponse?.pages ?: NO_PAGES > params.key) {
                        params.key + 1
                    } else {
                        null
                    }
                callback.onResult(prepareData(flickrResponse), adjasentKey)
                _loadStatus.postValue(LoadStatus.Loaded)
            }, {
                _loadStatus.postValue(LoadStatus.Error(it.message ?: ""))
            })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, GalleryItem>) {
        _loadStatus.postValue(LoadStatus.Loading)
        fetchData(params.key, PER_PAGE)
            .subscribe({ flickrResponse ->
                callback.onResult(
                    prepareData(flickrResponse),
                    if (params.key > 1) params.key - 1 else null
                )
                _loadStatus.postValue(LoadStatus.Loaded)
            }, {
                _loadStatus.postValue(LoadStatus.Error(it.message ?: ""))
            })
    }

    private fun prepareData(flickrResponse: FlickrResponse) =
        flickrResponse.photoResponse?.galleryItems?.filter(::filter)
            ?: mutableListOf()

    private fun filter(galleryItem: GalleryItem) =
        galleryItem.smallUrl.isNotEmpty() && galleryItem.originalUrl.isNotEmpty()

    sealed class LoadStatus {
        object Loading : LoadStatus()
        object Loaded : LoadStatus()
        class Error(val msg: String) : LoadStatus()
    }

    companion object {
        const val INITIAL_PAGE = 0
        const val PER_PAGE = 100
    }
}