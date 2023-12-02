package com.example.vinyls_equipo_16.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.vinyls_equipo_16.R
import com.example.vinyls_equipo_16.R.id.action_albumAddTrackFragment_to_albumDetailFragment
import com.example.vinyls_equipo_16.databinding.AlbumAddTrackFragmentBinding
import com.example.vinyls_equipo_16.network.NetworkServiceAdapter
import com.example.vinyls_equipo_16.viewmodels.AlbumDetailViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat


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
        if (albumId == null || albumId == 0) {
            Toast.makeText(context, getString(R.string.create_error), Toast.LENGTH_LONG).show()
            return
        }
        val duration = binding.textTrackDuration.text.toString()
        val name = binding.textTrackName.text.toString()

        val networkServiceAdapter = NetworkServiceAdapter.getInstance(requireContext())

        lifecycleScope.launch {
            try {
                networkServiceAdapter.addTrackToAlbum(albumId, name, duration)
                Toast.makeText(context, getString(R.string.create_ok), Toast.LENGTH_SHORT).show()
                val bundle = Bundle()
                bundle.putInt("albumId", albumId)
                findNavController().navigate(R.id.action_albumAddTrackFragment_to_albumDetailFragment, bundle,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.albumDetailFragment, true)
                        .build()
                )
            } catch (e: Exception) {
                Toast.makeText(context, "${getString(R.string.create_error)} - ${e.message}", Toast.LENGTH_LONG).show()
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


        npMin.maxValue = 59
        npMin.minValue = 0
        npMin.wrapSelectorWheel = true

        npSec.maxValue = 59
        npSec.minValue = 0
        npSec.wrapSelectorWheel = true

        confirmButton.setOnClickListener {
            val minutes = npMin.value
            val seconds = npSec.value
            view?.findViewById<EditText>(R.id.textTrackDuration)?.setText("$minutes:$seconds")
            dialog.dismiss()
        }

        dialog.show()
    }

    @SuppressLint("SimpleDateFormat")
    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
        }
        activity.actionBar?.title = getString(R.string.title_addTrack)
        val albumName = arguments?.getString("name")
        val cover = arguments?.getString("cover")

            binding.albumName.text = albumName

            Glide.with(this)
                .load(cover?.toUri()?.buildUpon()?.scheme("https")?.build())
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.loading_animation)

                        .error(R.drawable.ic_broken_image)
                )
                .into(binding.albumCover)
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
    }

