package com.example.vinyls_equipo_16.network

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.vinyls_equipo_16.models.Album
import com.example.vinyls_equipo_16.models.AlbumDetail
import com.example.vinyls_equipo_16.models.Collector
import com.example.vinyls_equipo_16.models.CollectorAlbum
import com.example.vinyls_equipo_16.models.CollectorDetail
import com.example.vinyls_equipo_16.models.Comment
import com.example.vinyls_equipo_16.models.FavoritePerformer
import com.example.vinyls_equipo_16.models.Musician
import com.example.vinyls_equipo_16.models.MusicianDetail
import com.example.vinyls_equipo_16.models.Prize
import com.example.vinyls_equipo_16.models.Track
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class NetworkServiceAdapter constructor(context: Context) {
    companion object{
        //const val BASE_URL=  "http://34.69.222.171/"
        const val BASE_URL= "https://vynils-back-heroku.herokuapp.com/"
        var instance: NetworkServiceAdapter? = null
        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: NetworkServiceAdapter(context).also {
                    instance = it
                }
            }
    }
    private val requestQueue: RequestQueue by lazy {
        // applicationContext keeps you from leaking the Activity or BroadcastReceiver if someone passes one in.
        Volley.newRequestQueue(context.applicationContext)
    }

    suspend fun getMusicians()= suspendCoroutine<List<Musician>>{ cont ->
        requestQueue.add(getRequest("musicians",
            { response ->
                val resp = JSONArray(response)
                val list = mutableListOf<Musician>()
                for (i in 0 until resp.length()) {
                    val item = resp.getJSONObject(i)
                    list.add(i, Musician(musicianId = item.getInt("id"),name = item.getString("name"), image = item.getString("image")))
                }
                cont.resume(list)
            },
            {
                cont.resumeWithException(it)
            }))
    }
    suspend fun getAlbums()= suspendCoroutine<List<Album>>{ cont ->
        requestQueue.add(getRequest("albums",
            { response ->
                val resp = JSONArray(response)
                val list = mutableListOf<Album>()
                for (i in 0 until resp.length()) {
                    val item = resp.getJSONObject(i)
                    list.add(i, Album(albumId = item.getInt("id"),name = item.getString("name"), cover = item.getString("cover"), recordLabel = item.getString("recordLabel"), releaseDate = item.getString("releaseDate"), genre = item.getString("genre"), description = item.getString("description")))
                }
                cont.resume(list)
            },
            {
                cont.resumeWithException(it)
            }))
    }

    suspend fun getCollectors()= suspendCoroutine<List<Collector>>{ cont ->
        requestQueue.add(getRequest("collectors",
            { response ->
                val resp = JSONArray(response)
                val list = mutableListOf<Collector>()
                for (i in 0 until resp.length()) {
                    val item = resp.getJSONObject(i)
                    list.add(i, Collector(collectorId = item.getInt("id"),name = item.getString("name"), telephone = item.getString("telephone"), email = item.getString("email")))
                }
                cont.resume(list)
            },
            {
                cont.resumeWithException(it)
            }))
    }

    suspend fun getAlbum(albumId:Int) = suspendCoroutine<AlbumDetail>{ cont->
        requestQueue.add(getRequest("albums/$albumId",
            { response ->
                val item = JSONObject(response)
                val tracks = mutableListOf<Track>()
                val trackItemArray = item.getJSONArray("tracks")
                var trackItem:JSONObject? = null
                for (i in 0 until trackItemArray.length()) {
                    trackItem = trackItemArray.getJSONObject(i)
                    val track = Track( id = trackItem.getInt("id"), name = trackItem.getString("name"), duration = trackItem.getString("duration"))
                    tracks.add(track)
                }

                val album = AlbumDetail(
                    albumId = item.getInt("id"),
                    name = item.getString("name"),
                    cover = item.getString("cover"),
                    recordLabel = item.getString("recordLabel"),
                    releaseDate = item.getString("releaseDate"),
                    genre = item.getString("genre"),
                    description = item.getString("description"),
                    tracks = tracks )

                cont.resume(album)
            },
            {
                cont.resumeWithException(it)
            }))
    }

    suspend fun getMusician(musicianId:Int) = suspendCoroutine<MusicianDetail>{ cont->
        requestQueue.add(getRequest("musicians/$musicianId",
            { response ->
                val item = JSONObject(response)
                val albums = mutableListOf<Album>()
                val albumsItemArray = item.getJSONArray("albums")
                var albumItem:JSONObject? = null
                for (i in 0 until albumsItemArray.length()) {
                    albumItem = albumsItemArray.getJSONObject(i)
                    val album = Album( albumId = albumItem.getInt("id"),
                        name = albumItem.getString("name"),
                        cover = albumItem.getString("cover"),
                        releaseDate = albumItem.getString("releaseDate"),
                        description = albumItem.getString("description"),
                        genre = albumItem.getString("genre"),
                        recordLabel = albumItem.getString("recordLabel"))
                    albums.add(album)
                }

                val prizes = mutableListOf<Prize>()
                val prizesItemArray = item.getJSONArray("performerPrizes")
                var prizeItem:JSONObject? = null
                for (i in 0 until prizesItemArray.length()) {
                    prizeItem = prizesItemArray.getJSONObject(i)
                    val prize = Prize( prizeId = prizeItem.getInt("id"),
                        premiationDate = prizeItem.getString("premiationDate"))
                    prizes.add(prize)
                }



                val musician = MusicianDetail(
                    musicianId = item.getInt("id"),
                    name = item.getString("name"),
                    image = item.getString("image"),
                    description = item.getString("description"),
                    birthDate = item.getString("birthDate"),
                    albums = albums,
                    performerPrizes = prizes)

                cont.resume(musician)
            },
            {
                cont.resumeWithException(it)
            }))
    }

    suspend fun getCollector(collectorId:Int) = suspendCoroutine<CollectorDetail>{ cont->
        requestQueue.add(getRequest("collectors/$collectorId",
            { response ->
                val item = JSONObject(response)
                //comments
                val comments = mutableListOf<Comment>()
                val commentsItemArray = item.getJSONArray("comments")
                var commentItem:JSONObject? = null
                for (i in 0 until commentsItemArray.length()) {
                    commentItem = commentsItemArray.getJSONObject(i)
                    val comment = Comment( commentId = commentItem.getInt("id"),
                        description = commentItem.getString("description"),
                        rating = commentItem.getString("rating"))
                    comments.add(comment)
                }
                //favoritePerformers
                val favoritePerformers = mutableListOf<FavoritePerformer>()
                val favoritePerformersItemArray = item.getJSONArray("favoritePerformers")
                var favoritePerformerItem:JSONObject? = null
                for (i in 0 until favoritePerformersItemArray.length()) {
                    favoritePerformerItem = favoritePerformersItemArray.getJSONObject(i)

                    val favoritePerformer = FavoritePerformer( favoritePerformerId = favoritePerformerItem.getInt("id"),
                        name = favoritePerformerItem.getString("name"),
                        image = favoritePerformerItem.getString("image"),
                        description = favoritePerformerItem.getString("description"),
                        birthDate = if (favoritePerformerItem.has("birthDate")) favoritePerformerItem.getString("birthDate") else "",
                        creationDate = if (favoritePerformerItem.has("creationDate"))  favoritePerformerItem.getString("creationDate") else "")
                    favoritePerformers.add(favoritePerformer)
                }
                //collectorAlbums
                val collectorAlbums = mutableListOf<CollectorAlbum>()
                val collectorAlbumsItemArray = item.getJSONArray("collectorAlbums")
                var collectorAlbumItem:JSONObject? = null
                for (i in 0 until collectorAlbumsItemArray.length()) {
                    collectorAlbumItem = collectorAlbumsItemArray.getJSONObject(i)
                    val collectorAlbum = CollectorAlbum( collectorAlbumId = collectorAlbumItem.getInt("id"),
                        price = collectorAlbumItem.getInt("price"),
                        status = collectorAlbumItem.getString("status"))
                    collectorAlbums.add(collectorAlbum)
                }

                val collector = CollectorDetail(
                    collectorId = item.getInt("id"),
                    name = item.getString("name"),
                    telephone = item.getString("telephone"),
                    email = item.getString("email"),
                    comments = comments,
                    favoritePerformers = favoritePerformers,
                    collectorAlbums = collectorAlbums)

                cont.resume(collector)
            },
            {
                cont.resumeWithException(it)
            }))
    }

    private fun getRequest(path:String, responseListener: Response.Listener<String>, errorListener: Response.ErrorListener): StringRequest {
        return StringRequest(Request.Method.GET, BASE_URL+path, responseListener,errorListener)
    }

    suspend fun createAlbum(name: String, cover: String, releaseDate: String, description: String, genre: String, recordLabel: String) = suspendCoroutine<Unit> { cont ->
        val postData = JSONObject().apply {
            put("name", name)
            put("cover", cover)
            put("releaseDate", releaseDate + "T00:00:00.000Z")
            put("description", description)
            put("genre", genre)
            put("recordLabel", recordLabel)
        }

        val jsonRequest = object : JsonObjectRequest(
            Request.Method.POST, BASE_URL + "albums", postData,
            Response.Listener {
                cont.resume(Unit)
            },
            Response.ErrorListener { error ->
                val errorMessage = error.networkResponse?.let { networkResponse ->
                    val responseBody = String(networkResponse.data, Charsets.UTF_8)
                    try {
                        JSONObject(responseBody).getString("message")
                    } catch (e: JSONException) {
                        "Error desconocido: ${networkResponse.statusCode}"
                    }
                } ?: error.localizedMessage ?: "Error desconocido"

                cont.resumeWithException(Exception(errorMessage))
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                return headers
            }
        }

        requestQueue.add(jsonRequest)
    }

}