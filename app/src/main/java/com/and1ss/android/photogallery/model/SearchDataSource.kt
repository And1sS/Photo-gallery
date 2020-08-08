package com.and1ss.android.photogallery.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.and1ss.android.photogallery.api.FlickrFetchr
import com.and1ss.android.photogallery.api.FlickrResponse
import io.reactivex.rxjava3.core.Observable

class SearchDataSource(private val text: String) : BaseDataSource() {
    override fun fetchData(page: Int, perPage: Int): Observable<FlickrResponse> =
        FlickrFetchr().searchPhotosPage(text, page, perPage)
}
