package com.kamilagcabay.wallpaperapp.Model

import com.google.gson.annotations.SerializedName

data class UnsplashPhoto(
    val id : String,
    val urls : UnsplashPhotoUrls
)

 data class UnsplashPhotoUrls(
     @SerializedName("small")
     val small : String,
     @SerializedName("regular")
     val medium : String,
     @SerializedName("raw")
     val large : String
 )
