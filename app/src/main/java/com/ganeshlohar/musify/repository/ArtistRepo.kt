package com.ganeshlohar.musify.repository

import com.ganeshlohar.musify.api.RetrofitInstance

class ArtistRepo {
    suspend fun getSongs(searchQuery: String) = RetrofitInstance.api.getSongsForArtist(searchQuery)
}