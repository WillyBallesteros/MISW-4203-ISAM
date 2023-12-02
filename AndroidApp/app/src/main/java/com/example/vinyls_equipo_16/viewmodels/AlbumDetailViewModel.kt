package com.example.vinyls_equipo_16.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.vinyls_equipo_16.models.AlbumDetail
import com.example.vinyls_equipo_16.repositories.AlbumDetailRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlbumDetailViewModel(application: Application, albumId: Int) :  AndroidViewModel(application) {

    private val albumDetailRepository = AlbumDetailRepository(application)
    private val _dataLoaded = MutableLiveData(false)

    private var _album: MutableLiveData<AlbumDetail> = MutableLiveData<AlbumDetail>()

    val dataLoaded: LiveData<Boolean>
        get() = _dataLoaded

    //  Album(0, "", "",     "",     "",     "",     "" )

    val album: LiveData<AlbumDetail>
        get() = _album

    private var _eventNetworkError = MutableLiveData(false)

    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData(false)

    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    val id:Int = albumId

    init {
        refreshDataFromNetwork()
    }

    private fun refreshDataFromNetwork() {
        try {
            viewModelScope.launch (Dispatchers.Default){
                _dataLoaded.postValue(false)
                withContext(Dispatchers.IO){
                    val data: AlbumDetail = albumDetailRepository.refreshData(id)
                    _album.postValue(data)
                    _dataLoaded.postValue(true)
                }
                _eventNetworkError.postValue(false)
                _isNetworkErrorShown.postValue(false)
            }
        }
        catch (e:Exception){
            _eventNetworkError.value = true
        }
    }


    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    class Factory(val app: Application, private val albumId: Int) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AlbumDetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AlbumDetailViewModel(app, albumId) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }


    }
}
