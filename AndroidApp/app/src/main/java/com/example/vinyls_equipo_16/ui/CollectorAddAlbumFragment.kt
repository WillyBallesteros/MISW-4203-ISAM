package com.example.vinyls_equipo_16.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.vinyls_equipo_16.R
import com.example.vinyls_equipo_16.databinding.CollectorAddAlbumFragmentBinding
import com.example.vinyls_equipo_16.viewmodels.AlbumViewModel

class CollectorAddAlbumFragment: Fragment() {

    private var _binding: CollectorAddAlbumFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: AlbumViewModel
    private lateinit var miSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CollectorAddAlbumFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        miSpinner = view.findViewById(R.id.my_spinner)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val textTrackDuration = view.findViewById<EditText>(R.id.textTrackDuration)
//        textTrackDuration.setOnClickListener {
//            showDurationPicker()
//        }

        binding.btnCreate.setOnClickListener {
            attemptAddTrack()
        }
    }

    private fun attemptAddTrack() {
        if (!validateFields()) {
            return
        }
        val collectorId = arguments?.getString("collectorId")?.toInt()
        /*var cover = arguments?.getString("cover")*/
        if (collectorId == null || collectorId == 0) {
            Toast.makeText(context, "Error al crear el álbum", Toast.LENGTH_LONG).show()
            return
        }
//        val duration = binding.textTrackDuration.text.toString()
//        val name = binding.textTrackName.text.toString()
//
//        val networkServiceAdapter = NetworkServiceAdapter.getInstance(requireContext())
//
//        lifecycleScope.launch {
//            try {
//                networkServiceAdapter.addTrackToAlbum(albumId, name, duration)
//                Toast.makeText(context, "Track agregada con éxito", Toast.LENGTH_SHORT).show()
//                val bundle = Bundle()
//                bundle.putInt("albumId", albumId)
//                findNavController().navigate(R.id.action_albumAddTrackFragment_to_albumDetailFragment, bundle)
//            } catch (e: Exception) {
//                Toast.makeText(context, "Error al agregar Track a el álbum: ${e.message}", Toast.LENGTH_LONG).show()
//            }
//        }
    }

    private fun validateFields(): Boolean {
//        if (binding.textTrackDuration.text.toString().trim().isEmpty()) {
//            binding.textTrackDuration.error = "Este campo es requerido"
//            return false
//        }
//
//        if (binding.textTrackDuration.text.toString().trim().isEmpty()) {
//            binding.textTrackDuration.error = "Este campo es requerido"
//            return false
//        }

        return true
    }

    @SuppressLint("SimpleDateFormat")
    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
        }

        val collectorName = arguments?.getString("name")

        binding.collectorName.text = collectorName

        viewModel = ViewModelProvider(this, AlbumViewModel.Factory(activity.application)).get(AlbumViewModel::class.java)

        val spinnerAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        miSpinner.adapter = spinnerAdapter

        viewModel.albums.observe(viewLifecycleOwner) { albumes ->
            // Transforma la lista de álbumes a una lista de Strings (nombres de álbumes, por ejemplo)
            val nombresDeAlbumes = albumes.map { it.name } // Reemplaza 'nombre' con el campo correspondiente
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
}