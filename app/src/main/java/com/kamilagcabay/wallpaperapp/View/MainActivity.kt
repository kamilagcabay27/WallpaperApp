package com.kamilagcabay.wallpaperapp.View

import android.app.Dialog
import android.app.WallpaperManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingSource
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.kamilagcabay.wallpaperapp.Adapter.UnsplashAdapter
import com.kamilagcabay.wallpaperapp.R
import com.kamilagcabay.wallpaperapp.ViewModel.UnsplashViewModel
import com.kamilagcabay.wallpaperapp.databinding.ActivityMainBinding
import com.kamilagcabay.wallpaperapp.databinding.DialogLoadingBinding
import com.kamilagcabay.wallpaperapp.utils.SetWallpaperTask
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel : UnsplashViewModel by viewModels()

    @Inject
    lateinit var adapter: UnsplashAdapter

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var wallpaperManager: WallpaperManager



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {

            recyclerView.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
            recyclerView.adapter = adapter

            swipeRefreshLayout.setOnRefreshListener {
                adapter.refresh()
            }

            lifecycleScope.launch {
                viewModel.photos.collectLatest { pagingData ->
                    adapter.submitData(pagingData)

                }

            }

            lifecycleScope.launch {
                adapter.loadStateFlow.collectLatest { loadStates ->
                    swipeRefreshLayout.isRefreshing = loadStates.refresh is LoadState.Loading

                }
            }

            adapter.setOnItemClickListener {
                val loading = Dialog(this@MainActivity)
                val bindingLoading = DialogLoadingBinding.inflate(layoutInflater)
                loading.setContentView(bindingLoading.root)
                loading.setCancelable(false)
                lifecycleScope.launch {
                    loading.show()
                    if (SetWallpaperTask(it.urls.large,wallpaperManager).execute()) {
                        loading.dismiss()
                        Toast.makeText(this@MainActivity, "The Wallpaper has been successfully changed",Toast.LENGTH_LONG).show()
                    } else {
                        loading.dismiss()
                        Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_LONG).show()
                    }
                }
            }

        }
    }
}