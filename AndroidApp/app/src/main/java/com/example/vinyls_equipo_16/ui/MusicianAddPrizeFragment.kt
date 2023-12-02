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
import com.example.vinyls_equipo_16.databinding.MusicianAddPrizeFragmentBinding
import com.example.vinyls_equipo_16.network.NetworkServiceAdapter
import com.example.vinyls_equipo_16.viewmodels.PrizeViewModel
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MusicianAddPrizeFragment: Fragment() {

    private var _binding: MusicianAddPrizeFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: PrizeViewModel
    private lateinit var miSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MusicianAddPrizeFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        miSpinner = view.findViewById(R.id.spinnerPrize)

        binding.premiationDate.setOnClickListener {
            mostrarDatePicker()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCreate.setOnClickListener {
            attemptAddTrack()
        }
    }

    private fun mostrarDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(selectedYear, selectedMonth, selectedDay)

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formattedDate = dateFormat.format(selectedDate.time)

            binding.premiationDate.setText(formattedDate)
        }, year, month, day)

        dpd.show()
    }

    private fun attemptAddTrack() {
        if (!validateFields()) {
            return
        }
        val musicianId = arguments?.getString("musicianId")?.toInt()

        if (musicianId == null || musicianId == 0) {
            Toast.makeText(context, getString(R.string.association_error), Toast.LENGTH_LONG).show()
            return
        }
        val prizeSeleccionado = miSpinner.selectedItem as Prize
        val prizeId = prizeSeleccionado.prizeId
        val premiationDate = binding.premiationDate.text.toString()

        val networkServiceAdapter = NetworkServiceAdapter.getInstance(requireContext())

        lifecycleScope.launch {
            try {
                networkServiceAdapter.addPrizeToMusician(prizeId, musicianId, premiationDate)
                Toast.makeText(context, getString(R.string.association_ok), Toast.LENGTH_SHORT).show()
                val bundle = Bundle()
                bundle.putInt("musicianId", musicianId)
                findNavController().navigate(R.id.action_musicianAddPrizeFragment_to_musicianDetailFragment, bundle,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.musicianDetailFragment, true)
                        .build()
                )
            } catch (e: Exception) {
                Toast.makeText(context, "${getString(R.string.association_error)} - ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun validateFields(): Boolean {

        if (binding.premiationDate.text.toString().trim().isEmpty()) {
            binding.premiationDate.error = "Este campo es requerido"
            return false
        }

        if (!isValidDate(binding.premiationDate.text.toString().trim())) {
            binding.premiationDate.error = "Formato de fecha inválido (yyyy-mm-dd)"
            return false
        }

        return true
    }

    private fun isValidDate(date: String): Boolean {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        dateFormat.isLenient = false
        try {
            dateFormat.parse(date)
            return true
        } catch (e: ParseException) {
            return false
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
        }

        val musicianName = arguments?.getString("name")

        binding.prizeName.text = musicianName

        viewModel = ViewModelProvider(this, PrizeViewModel.Factory(activity.application)).get(PrizeViewModel::class.java)

        val spinnerAdapter = ArrayAdapter<Prize>(requireContext(), android.R.layout.simple_spinner_item)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        miSpinner.adapter = spinnerAdapter

        viewModel.prizes.observe(viewLifecycleOwner) { albumes ->
            // Transforma la lista de álbumes a una lista de Strings (nombres de álbumes, por ejemplo)
            val nombresDeAlbumes = albumes.map { Prize(it.prizeId, it.name) } // Reemplaza 'nombre' con el campo correspondiente
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
            Toast.makeText(activity, getString(R.string.network_error), Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }

    data class Prize(val prizeId: Int, val name: String) {
        override fun toString(): String {
            return name
        }
    }

}