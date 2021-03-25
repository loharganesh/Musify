package com.ganeshlohar.musify.api

import com.ganeshlohar.musify.model.Artist
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ArtistAPI {
    @GET("/search?")
    suspend fun getSongsForArtist(
        @Query("term")
        query: String,
    ): Response<Artist>

}