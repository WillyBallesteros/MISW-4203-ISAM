package com.example.vinyls_equipo_16.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.vinyls_equipo_16.R
import com.example.vinyls_equipo_16.R.id.action_prizeNewFragment_to_prizeFragment
import com.example.vinyls_equipo_16.databinding.PrizeNewFragmentBinding
import com.example.vinyls_equipo_16.network.NetworkServiceAdapter
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class PrizeNewFragment : Fragment() {
    private var _binding: PrizeNewFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PrizeNewFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnCreate.setOnClickListener {
            attemptCreatePrize()
        }
    }

    private fun attemptCreatePrize() {
        if (!validateFields()) {
            return
        }
        val name = binding.etName.text.toString()
        val description = binding.etDescription.text.toString()
        val organization = binding.etOrganization.text.toString()

        val networkServiceAdapter = NetworkServiceAdapter.getInstance(requireContext())

        lifecycleScope.launch {
            try {
                networkServiceAdapter.createPrize(name, description, organization)
                Toast.makeText(context, getString(R.string.create_ok), Toast.LENGTH_SHORT).show()
                findNavController().navigate(action_prizeNewFragment_to_prizeFragment)
            } catch (e: Exception) {
                Toast.makeText(context, "${getString(R.string.create_error)} - ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun validateFields(): Boolean {
        if (binding.etName.text.toString().trim().isEmpty()) {
            binding.etName.error = "Este campo es requerido"
            return false
        }

        if (binding.etDescription.text.toString().trim().isEmpty()) {
            binding.etDescription.error = "Este campo es requerido"
            return false
        }

        if (binding.etOrganization.text.toString().trim().isEmpty()) {
            binding.etOrganization.error = "Este campo es requerido"
            return false
        }

        return true
    }

    private fun isValidUrl(url: String): Boolean {
        return Patterns.WEB_URL.matcher(url).matches()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}