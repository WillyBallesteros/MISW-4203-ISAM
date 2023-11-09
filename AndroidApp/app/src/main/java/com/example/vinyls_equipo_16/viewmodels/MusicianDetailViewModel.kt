package com.example.vinyls_equipo_16.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.vinyls_equipo_16.models.MusicianDetail
import com.example.vinyls_equipo_16.repositories.MusicianDetailRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MusicianDetailViewModel(application: Application, musicianId: Int) :  AndroidViewModel(application) {

    private val musicianDetailRepository = MusicianDetailRepository(application)

    private var _musician: MutableLiveData<MusicianDetail> = MutableLiveData<MusicianDetail>()
    val musician: LiveData<MusicianDetail>
        get() = _musician!!

    private var _eventNetworkError = MutableLiveData<Boolean>(false)

    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)

    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    val id:Int = musicianId

    init {
        refreshDataFromNetwork()
    }

    private fun refreshDataFromNetwork() {
        try {
            viewModelScope.launch (Dispatchers.Default){
                withContext(Dispatchers.IO){
                    val data: MusicianDetail = musicianDetailRepository.refreshData(id)
                    _musician.postValue(data)
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

    class Factory(val app: Application, val musicianId: Int) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MusicianDetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MusicianDetailViewModel(app, musicianId) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }


    }
}
