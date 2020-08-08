package com.and1ss.android.photogallery.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName

data class GalleryItem @JsonCreator constructor(
    @JsonProperty("title")
    var title: String = "",
    @JsonProperty("id")
    var id: String = "",
    @SerializedName("url_s")
    @JsonProperty("url_s")
    var smallUrl: String = "",
    @SerializedName("url_o")
    @JsonProperty("url_o")
    var originalUrl: String = ""
)