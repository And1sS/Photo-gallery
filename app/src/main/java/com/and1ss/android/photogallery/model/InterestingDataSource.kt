package com.and1ss.android.photogallery.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.and1ss.android.photogallery.api.FlickrFetchr
import com.and1ss.android.photogallery.api.FlickrResponse
import io.reactivex.rxjava3.core.Observable

class InterestingDataSource : BaseDataSource() {
    override fun fetchData(page: Int, perPage: Int): Observable<FlickrResponse> =
        FlickrFetchr().fetchPhotosPage(page, perPage)

}
