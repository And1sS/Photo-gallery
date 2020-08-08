package com.and1ss.android.photogallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.and1ss.android.photogallery.model.BaseDataSource
import com.and1ss.android.photogallery.model.BaseDataSource.Companion.PER_PAGE
import com.and1ss.android.photogallery.model.GalleryItem
import com.and1ss.android.photogallery.model.InterestingDataSource
import com.and1ss.android.photogallery.model.GalleryItemDataSourceFactory


class PhotoGalleryViewModel : ViewModel() {
    val galleryItemPagedList: LiveData<PagedList<GalleryItem>>
    val loadStatus: LiveData<BaseDataSource.LoadStatus>
    val factory = GalleryItemDataSourceFactory()

    init {
        loadStatus = factory.loadStatus

        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(PER_PAGE).build()

        galleryItemPagedList = LivePagedListBuilder(factory, pagedListConfig).build()
    }

    fun switchToSearchDataSource(searchQuery: String) {
        factory.switchToSearchDataSource(searchQuery)
    }

    fun switchToInterestingDataSource() {
        factory.switchToInterestingDataSource()
    }

    fun reloadCurrentDataSource() {
        factory.reloadCurrentDataSource()
    }
}