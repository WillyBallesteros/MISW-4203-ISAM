package com.example.vinyls_equipo_16.ui

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.vinyls_equipo_16.R
import com.example.vinyls_equipo_16.databinding.AlbumNewFragmentBinding
import com.example.vinyls_equipo_16.network.NetworkServiceAdapter
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AlbumNewFragment : Fragment() {
    private var _binding: AlbumNewFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AlbumNewFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val genresAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.genres,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.spinnerGenre.adapter = genresAdapter

        val recordLabelAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.record_labels,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.spinnerRecordLabel.adapter = recordLabelAdapter
        binding.etReleaseDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(requireContext(),android.R.style.Theme_DeviceDefault_Light_DarkActionBar,  { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)

                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)

                binding.etReleaseDate.setText(formattedDate)
            }, year, month, day)

            datePickerDialog.show()
        }

        binding.btnCreate.setOnClickListener {
            attemptCreateAlbum()
        }

    }

    private fun attemptCreateAlbum() {
        val name = binding.etName.text.toString()
        val cover = binding.etCoverUrl.text.toString()
        val releaseDate = binding.etReleaseDate.text.toString()
        val description = binding.etDescription.text.toString()
        val genre = binding.spinnerGenre.selectedItem.toString()
        val recordLabel = binding.spinnerRecordLabel.selectedItem.toString()

        val networkServiceAdapter = NetworkServiceAdapter.getInstance(requireContext())

        lifecycleScope.launch {
            try {
                networkServiceAdapter.createAlbum(name, cover, releaseDate, description, genre, recordLabel)
                Toast.makeText(context, "Álbum creado con éxito", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_albumNewFragment_to_albumFragment)
            } catch (e: Exception) {
                Toast.makeText(context, "Error al crear el álbum: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}