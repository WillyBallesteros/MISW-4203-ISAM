package com.example.vinyls_equipo_16.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.vinyls_equipo_16.R
import com.example.vinyls_equipo_16.databinding.MusicianAddAlbumFragmentBinding
import com.example.vinyls_equipo_16.databinding.MusicianAddPrizeFragmentBinding
import com.example.vinyls_equipo_16.network.NetworkServiceAdapter
import com.example.vinyls_equipo_16.viewmodels.AlbumViewModel
import com.example.vinyls_equipo_16.viewmodels.PrizeViewModel
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MusicianAddAlbumFragment: Fragment() {

    private var _binding: MusicianAddAlbumFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AlbumViewModel
    private lateinit var miSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MusicianAddAlbumFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        miSpinner = view.findViewById(R.id.spinnerAlbum)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCreate.setOnClickListener {
            attemptAddTrack()
        }
    }

    private fun attemptAddTrack() {

        val musicianId = arguments?.getString("musicianId")?.toInt()

        if (musicianId == null || musicianId == 0) {
            Toast.makeText(context, "Error al asociar Premio a Artista", Toast.LENGTH_LONG).show()
            return
        }
        val albumSeleccionado = miSpinner.selectedItem as Album
        val albumId = albumSeleccionado.albumId

        val networkServiceAdapter = NetworkServiceAdapter.getInstance(requireContext())

        lifecycleScope.launch {
            try {
                networkServiceAdapter.addAlbumToMusician(albumId, musicianId)
                Toast.makeText(context, "Asociación creada con éxito", Toast.LENGTH_SHORT).show()
                val bundle = Bundle()
                bundle.putInt("musicianId", musicianId)
                findNavController().navigate(R.id.action_musicianAddAlbumFragment_to_musicianDetailFragment, bundle,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.musicianDetailFragment, true)
                        .build()
                )
            } catch (e: Exception) {
                Toast.makeText(context, "Error al asociar Álbum a Artista: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
        }

        val musicianName = arguments?.getString("name")

        binding.musicianName.text = musicianName

        viewModel = ViewModelProvider(this, AlbumViewModel.Factory(activity.application)).get(AlbumViewModel::class.java)

        val spinnerAdapter = ArrayAdapter<Album>(requireContext(), android.R.layout.simple_spinner_item)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        miSpinner.adapter = spinnerAdapter

        viewModel.albums.observe(viewLifecycleOwner) { albumes ->
            // Transforma la lista de álbumes a una lista de Strings (nombres de álbumes, por ejemplo)
            val nombresDeAlbumes = albumes.map { Album(it.albumId, it.name) } // Reemplaza 'nombre' con el campo correspondiente
            // Actualiza el ArrayAdapter con los nuevos datos
            spinnerAdapter.clear()
            spinnerAdapter.addAll(nombresDeAlbumes)
            spinnerAdapter.notifyDataSetChanged()
        }

        viewModel.eventNetworkError.observe(viewLifecycleOwner) { isNetworkError ->
            if (isNetworkError) onNetworkError()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onNetworkError() {
        if(!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }

    data class Album(val albumId: Int, val name: String) {
        override fun toString(): String {
            return name
        }
    }

}