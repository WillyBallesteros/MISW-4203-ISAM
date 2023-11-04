package com.example.vinyls_equipo_16.repositories

import android.app.Application
import com.example.vinyls_equipo_16.models.Album
import com.example.vinyls_equipo_16.network.NetworkServiceAdapter

class AlbumRepository (val application: Application){
    suspend fun refreshData(): List<Album> {
        //Determinar la fuente de datos que se va a utilizar. Si es necesario consultar la red, ejecutar el siguiente código
        return NetworkServiceAdapter.getInstance(application).getAlbums()
    }
}