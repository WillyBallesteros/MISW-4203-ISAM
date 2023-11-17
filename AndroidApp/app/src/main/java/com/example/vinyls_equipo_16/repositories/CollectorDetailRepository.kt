package com.example.vinyls_equipo_16.repositories

import android.app.Application
import com.example.vinyls_equipo_16.models.AlbumDetail
import com.example.vinyls_equipo_16.models.CollectorDetail
import com.example.vinyls_equipo_16.network.NetworkServiceAdapter

class CollectorDetailRepository (val application: Application){
    suspend fun refreshData(collectorId: Int ): CollectorDetail {
        return NetworkServiceAdapter.getInstance(application).getCollector(collectorId)

    }
}