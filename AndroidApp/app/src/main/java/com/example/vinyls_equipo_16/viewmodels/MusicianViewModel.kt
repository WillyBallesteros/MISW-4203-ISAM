package com.example.vinyls_equipo_16.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.vinyls_equipo_16.models.Musician
import com.example.vinyls_equipo_16.repositories.MusicianRepository
import kotlinx.coroutines.*

class MusicianViewModel(application: Application) : AndroidViewModel(application) {

    private val musiciansRepository = MusicianRepository(application)
    private val _dataLoaded = MutableLiveData(false)
    val dataLoaded: LiveData<Boolean>
        get() = _dataLoaded


    private val _musicians = MutableLiveData<List<Musician>>()
    val musicians: LiveData<List<Musician>>
        get() = _musicians

    private val _eventNetworkError = MutableLiveData(false)
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private val _isNetworkErrorShown = MutableLiveData(false)
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    init {
        refreshDataFromNetwork()
    }

    fun RefreshData() {
        refreshDataFromNetwork()
    }

    private fun refreshDataFromNetwork() {
        viewModelScope.launch {
            try {
                val data = withContext(Dispatchers.IO) {
                    musiciansRepository.refreshData()
                }
                _musicians.postValue(data)
                _dataLoaded.postValue(true)
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
            if (modelClass.isAssignableFrom(MusicianViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MusicianViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
