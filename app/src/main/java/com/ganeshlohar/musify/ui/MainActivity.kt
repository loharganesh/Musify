package com.ganeshlohar.musify.ui

import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.androiddevs.mvvmnewsapp.adapters.SongsAdapter
import com.ganeshlohar.musify.R
import com.ganeshlohar.musify.repository.ArtistRepo
import com.ganeshlohar.musify.util.Resource
import com.ganeshlohar.musify.viewmodels.SongViewModel
import com.ganeshlohar.musify.viewmodels.SongViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    lateinit var viewModel: SongViewModel
    lateinit var songsAdapter: SongsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupRecyclerView()
        val artistRepo = ArtistRepo()
        val viewModelProviderFactory = SongViewModelProviderFactory(artistRepo)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(SongViewModel::class.java)

        var job: Job? = null

        etAmArtistName.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(400L)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        viewModel.searchForArtist(editable.toString())
                    }
                }
            }
        }

        viewModel.artist.observe(this, { response ->
            when (response) {
                is Resource.Success -> {
                    handleProgressBar(View.GONE)
                    response.data?.let { artist ->
                        songsAdapter.differ.submitList(artist.results)
                    }
                    if(response.data?.resultCount == 0){
                        laNoResults.visibility = View.VISIBLE
                    }else{
                        laNoResults.visibility = View.GONE
                    }
                }
                is Resource.Error -> {
                    handleProgressBar(View.GONE)
                    response.message?.let { message ->
                        System.out.println("Error -> $message")
                    }
                }
                is Resource.Loading -> {
                    handleProgressBar(View.VISIBLE)
                }
            }
        })

//        Handle Keyboard Visibility
        handleScrollingBeheviour()

    }

    private fun handleProgressBar(visibility: Int) {
        laProgressBar.visibility = visibility
    }

    private fun handleScrollingBeheviour() {
        rcyAmSongs.setOnTouchListener(object : OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                val imm: InputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(etAmArtistName.getWindowToken(), 0)
                return false
            }
        })
    }

    private fun setupRecyclerView() {
        songsAdapter = SongsAdapter()
        rcyAmSongs.apply {
            adapter = songsAdapter
        }
    }

}