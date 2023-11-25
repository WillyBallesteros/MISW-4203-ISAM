package com.example.vinyls_equipo_16.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.vinyls_equipo_16.models.Prize
import com.example.vinyls_equipo_16.repositories.PrizeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PrizeViewModel(application: Application) : AndroidViewModel(application) {

    private val prizesRepository = PrizeRepository(application)
    private val _dataLoaded = MutableLiveData(false)
    val dataLoaded: LiveData<Boolean>
        get() = _dataLoaded


    private val _prizes = MutableLiveData<List<Prize>>()
    val prizes: LiveData<List<Prize>>
        get() = _prizes

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
                _dataLoaded.postValue(false)
                val data = withContext(Dispatchers.IO) {
                    prizesRepository.refreshData()
                }
                _prizes.postValue(data)
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
            if (modelClass.isAssignableFrom(PrizeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return PrizeViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
