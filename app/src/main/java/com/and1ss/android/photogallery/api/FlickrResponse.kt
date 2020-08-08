package com.and1ss.android.photogallery.api

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSetter
import com.google.gson.annotations.SerializedName

data class FlickrResponse @JsonCreator constructor(
    @SerializedName("photos")
    @JsonProperty("photos")
    var photoResponse: PhotoResponse? = null,

    @SerializedName("stat")
    @JsonProperty("stat")
    var status: String? = null
)