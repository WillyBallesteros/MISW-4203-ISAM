package com.example.vinyls_equipo_16.repositories

import android.app.Application
import com.example.vinyls_equipo_16.models.Prize
import com.example.vinyls_equipo_16.network.NetworkServiceAdapter

class PrizeRepository (val application: Application){
    suspend fun refreshData(): List<Prize> {
        return NetworkServiceAdapter.getInstance(application).getPrizes()
    }
}