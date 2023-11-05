package com.example.vinyls_equipo_16.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.vinyls_equipo_16.models.Album
import com.example.vinyls_equipo_16.repositories.AlbumRepository
import kotlinx.coroutines.*

class AlbumViewModel(application: Application) : AndroidViewModel(application) {

    private val albumsRepository = AlbumRepository(application)

    private val _albums = MutableLiveData<List<Album>>()
    val albums: LiveData<List<Album>>
        get() = _albums

    private val _eventNetworkError = MutableLiveData<Boolean>(false)
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private val _isNetworkErrorShown = MutableLiveData<Boolean>(false)
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    init {
        refreshDataFromNetwork()
    }

    private fun refreshDataFromNetwork() {
        viewModelScope.launch {
            try {
                // It's safe to call Dispatchers.IO immediately because it's a background operation
                val data = withContext(Dispatchers.IO) {
                    albumsRepository.refreshData()
                }
                _albums.postValue(data)
                _eventNetworkError.postValue(false)
            } catch (e: Exception) {
                _eventNetworkError.postValue(true)
            } finally {
                _isNetworkErrorShown.postValue(false)
            }
        }
    }

    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AlbumViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AlbumViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
