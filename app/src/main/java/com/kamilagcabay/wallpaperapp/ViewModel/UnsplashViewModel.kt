package com.kamilagcabay.wallpaperapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kamilagcabay.wallpaperapp.Model.UnsplashPhoto
import com.kamilagcabay.wallpaperapp.Server.UnsplashApi
import com.kamilagcabay.wallpaperapp.View.UnsplashPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class UnsplashViewModel
@Inject constructor(private val unsplashApi: UnsplashApi) : ViewModel()
{

    val photos : Flow<PagingData<UnsplashPhoto>> = Pager(
        config = PagingConfig(
            pageSize = 40,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { UnsplashPagingSource(unsplashApi) }

    ).flow

}
