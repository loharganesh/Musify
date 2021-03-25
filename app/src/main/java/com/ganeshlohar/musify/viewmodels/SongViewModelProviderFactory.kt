package com.ganeshlohar.musify.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ganeshlohar.musify.repository.ArtistRepo

class SongViewModelProviderFactory (
    val artistRepo:ArtistRepo
        ):ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SongViewModel(artistRepo) as T
    }
}