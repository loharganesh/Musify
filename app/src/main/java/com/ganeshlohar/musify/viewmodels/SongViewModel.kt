package com.ganeshlohar.musify.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ganeshlohar.musify.model.Artist
import com.ganeshlohar.musify.repository.ArtistRepo
import com.ganeshlohar.musify.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class SongViewModel(val artistRepo: ArtistRepo) : ViewModel() {
    val artist: MutableLiveData<Resource<Artist>> = MutableLiveData()

    fun searchForArtist(searchQuery: String) = viewModelScope.launch {
        artist.postValue(Resource.Loading())
        val response = artistRepo.getSongs(searchQuery)
        artist.value = handleSearchReponse(response)
    }

    private fun handleSearchReponse(response: Response<Artist>): Resource<Artist> {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        return Resource.Error(response.message())
    }

}