package com.example.vinyls_equipo_16.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.vinyls_equipo_16.R
import com.example.vinyls_equipo_16.databinding.AlbumAddTrackFragmentBinding
import com.example.vinyls_equipo_16.network.NetworkServiceAdapter
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class AlbumAddTrackFragment : Fragment() {
    private var _binding: AlbumAddTrackFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AlbumAddTrackFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textTrackDuration = view.findViewById<EditText>(R.id.textTrackDuration)
        textTrackDuration.setOnClickListener {
            showDurationPicker()
        }

        binding.btnCreate.setOnClickListener {
            attemptAddTrack()
        }
    }

    private fun attemptAddTrack() {
        if (!validateFields()) {
            return
        }
        val albumId = arguments?.getString("albumId")?.toInt()
        /*var cover = arguments?.getString("cover")*/
        if (albumId == null) {
            Toast.makeText(context, "Error al crear el álbum", Toast.LENGTH_LONG).show()
            return
        }
        val duration = binding.textTrackDuration.text.toString()
        val name = binding.textTrackName.text.toString()

        val networkServiceAdapter = NetworkServiceAdapter.getInstance(requireContext())

        lifecycleScope.launch {
            try {
                networkServiceAdapter.addTrackToAlbum(albumId, name, duration)
                Toast.makeText(context, "Álbum creado con éxito", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_albumAddTrackFragment_to_albumDetailFragment)
            } catch (e: Exception) {
                Toast.makeText(context, "Error al crear el álbum: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun validateFields(): Boolean {
        if (binding.textTrackDuration.text.toString().trim().isEmpty()) {
            binding.textTrackDuration.error = "Este campo es requerido"
            return false
        }

        if (binding.textTrackDuration.text.toString().trim().isEmpty()) {
            binding.textTrackDuration.error = "Este campo es requerido"
            return false
        }

        return true
    }

    private fun showDurationPicker() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.duration_picker)

        val npMin = dialog.findViewById<NumberPicker>(R.id.numberPickerMinutes)
        val npSec = dialog.findViewById<NumberPicker>(R.id.numberPickerSeconds)
        val confirmButton = dialog.findViewById<Button>(R.id.confirmButton)

        // Configuración para el NumberPicker de minutos
        npMin.maxValue = 59  // Máximo valor para minutos (0 a 59)
        npMin.minValue = 0   // Mínimo valor para minutos
        npMin.wrapSelectorWheel = true  // Permite que el selector rote infinitamente

// Configuración para el NumberPicker de segundos
        npSec.maxValue = 59  // Máximo valor para segundos (0 a 59)
        npSec.minValue = 0   // Mínimo valor para segundos
        npSec.wrapSelectorWheel = true  // Permite que el selector rote infinitamente

        confirmButton.setOnClickListener {
            val minutes = npMin.value
            val seconds = npSec.value
            view?.findViewById<EditText>(R.id.textTrackDuration)?.setText("$minutes:$seconds")
            dialog.dismiss()
        }

        dialog.show()
    }


}