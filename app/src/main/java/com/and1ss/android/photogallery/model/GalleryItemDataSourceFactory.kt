package com.and1ss.android.photogallery.model

import android.app.DownloadManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource

class GalleryItemDataSourceFactory
    : DataSource.Factory<Int, GalleryItem>() {

    private var state: State = State.INTERESTING
    private var searchQuery: String = ""

    private val _dataSource: MutableLiveData<BaseDataSource>
            = MutableLiveData()
    val dataSource: LiveData<BaseDataSource>
        get() = _dataSource

    val loadStatus: LiveData<BaseDataSource.LoadStatus>

    init {
        loadStatus = Transformations.switchMap(_dataSource) {
            it.loadStatus
        }
    }

    override fun create(): PageKeyedDataSource<Int, GalleryItem> {
        return when(state) {
            State.INTERESTING -> InterestingDataSource()
            State.SEARCH -> SearchDataSource(searchQuery)
        }.also {
            _dataSource.postValue(it)
        }
    }

    fun switchToSearchDataSource(searchQuery: String) {
        this.searchQuery = searchQuery
        state = State.SEARCH

        dataSource.value?.invalidate()
    }

    fun switchToInterestingDataSource() {
        state = State.INTERESTING
        dataSource.value?.invalidate()
    }

    fun reloadCurrentDataSource() {
        dataSource.value?.invalidate()
    }

    enum class State {
        INTERESTING, SEARCH
    }
}