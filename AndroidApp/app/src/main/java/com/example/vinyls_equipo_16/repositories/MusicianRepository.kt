package com.example.vinyls_equipo_16.repositories

import android.app.Application
import com.example.vinyls_equipo_16.models.Album
import com.example.vinyls_equipo_16.models.Musician
import com.example.vinyls_equipo_16.network.NetworkServiceAdapter

class MusicianRepository (val application: Application){
    suspend fun refreshData(): List<Musician> {
        return NetworkServiceAdapter.getInstance(application).getMusicians()
    }
}