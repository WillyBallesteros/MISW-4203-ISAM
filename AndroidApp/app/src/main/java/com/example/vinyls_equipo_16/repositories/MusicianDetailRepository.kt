package com.example.vinyls_equipo_16.repositories

import android.app.Application
import com.example.vinyls_equipo_16.models.MusicianDetail
import com.example.vinyls_equipo_16.network.NetworkServiceAdapter

class MusicianDetailRepository (val application: Application){
    suspend fun refreshData(musicianId: Int ): MusicianDetail {
        return NetworkServiceAdapter.getInstance(application).getMusician(musicianId)

    }
}