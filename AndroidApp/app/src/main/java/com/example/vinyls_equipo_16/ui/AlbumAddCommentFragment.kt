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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.vinyls_equipo_16.R
import com.example.vinyls_equipo_16.databinding.AlbumAddCommentFragmentBinding
import com.example.vinyls_equipo_16.databinding.AlbumAddTrackFragmentBinding
import com.example.vinyls_equipo_16.network.NetworkServiceAdapter
import kotlinx.coroutines.launch


class AlbumAddCommentFragment : Fragment() {
    private var _binding: AlbumAddCommentFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AlbumAddCommentFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCreate.setOnClickListener {
            attemptAddComment()
        }
    }

    private fun attemptAddComment() {
        if (!validateFields()) {
            return
        }
        val albumId = arguments?.getString("albumId")?.toInt()
        /*var cover = arguments?.getString("cover")*/
        if (albumId == null || albumId == 0) {
            Toast.makeText(context, "Error al crear el álbum", Toast.LENGTH_LONG).show()
            return
        }
        val comment = binding.textComment.text.toString()
        val rating = binding.ratingBar.rating.toInt()
        val collectorId = 1

        val networkServiceAdapter = NetworkServiceAdapter.getInstance(requireContext())

        lifecycleScope.launch {
            try {
                networkServiceAdapter.addCommentToAlbum(albumId, comment, rating, collectorId)
                Toast.makeText(context, "Comment agregada con éxito", Toast.LENGTH_SHORT).show()
                val bundle = Bundle()
                bundle.putInt("albumId", albumId)
                findNavController().navigate(R.id.action_albumAddCommentFragment_to_albumDetailFragment, bundle)
            } catch (e: Exception) {
                Toast.makeText(context, "Error al agregar Comment a el álbum: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun validateFields(): Boolean {
        if (binding.textComment.text.toString().trim().isEmpty()) {
            binding.textComment.error = "Este campo es requerido"
            return false
        }
        if (binding.ratingBar.rating == 0f) {
            Toast.makeText(context, "Por favor, selecciona una calificación", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    @SuppressLint("SimpleDateFormat")
    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
        }
        activity.actionBar?.title = getString(R.string.title_comments)
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