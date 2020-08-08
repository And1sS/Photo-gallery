package com.and1ss.android.photogallery.api

import com.and1ss.android.photogallery.model.GalleryItem
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSetter
import com.google.gson.annotations.SerializedName

data class PhotoResponse @JsonCreator constructor(
    @SerializedName("pages")
    @JsonProperty("pages")
    var pages: Int = 0,
    @SerializedName("photo")
    @JsonProperty("photo")
    var galleryItems: List<GalleryItem>? = null
)
