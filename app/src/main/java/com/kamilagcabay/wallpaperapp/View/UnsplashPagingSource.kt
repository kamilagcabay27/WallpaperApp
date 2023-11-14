package com.kamilagcabay.wallpaperapp.View

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kamilagcabay.wallpaperapp.Model.UnsplashPhoto
import com.kamilagcabay.wallpaperapp.Server.UnsplashApi

class UnsplashPagingSource(private val unsplashApi: UnsplashApi) : PagingSource<Int, UnsplashPhoto>() {
    override fun getRefreshKey(state: PagingState<Int, UnsplashPhoto>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnsplashPhoto> {
        return try {
            val page = params.key ?: 1
            val photos = unsplashApi.getPhotos(page = page)
            LoadResult.Page(
                data = photos,
                prevKey = if (page == 1) null else page -1,
                nextKey = if (photos.isEmpty()) null else page +1
            )

        } catch (e:Exception) {
            LoadResult.Error(e)
        }
    }
}