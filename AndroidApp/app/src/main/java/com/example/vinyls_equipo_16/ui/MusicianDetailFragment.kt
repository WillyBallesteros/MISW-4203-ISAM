package com.example.vinyls_equipo_16.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.vinyls_equipo_16.R
import com.example.vinyls_equipo_16.databinding.MusicianDetailFragmentBinding
import com.example.vinyls_equipo_16.ui.adapters.AlbumsAdapter
import com.example.vinyls_equipo_16.ui.adapters.CommentsAdapter
import com.example.vinyls_equipo_16.ui.adapters.MusicianAlbumAdapter
import com.example.vinyls_equipo_16.ui.adapters.PerformerPrizeAdapter
import com.example.vinyls_equipo_16.ui.adapters.TracksAdapter
import com.example.vinyls_equipo_16.viewmodels.AlbumViewModel
import com.example.vinyls_equipo_16.viewmodels.MusicianDetailViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.Date


private const val ARG_PARAM1 = "musicianId"
@Suppress("DEPRECATION")
class MusicianDetailFragment : Fragment() {

    private var _param1: Int? = null
    private val param1 get() = _param1!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAlbums: RecyclerView
    private var _binding: MusicianDetailFragmentBinding? = null
    private val binding get() = _binding!!
    private var viewModelAdapter: PerformerPrizeAdapter? = null
    private var viewModelAdapterAlbums: MusicianAlbumAdapter? = null
    private lateinit var viewModel: MusicianDetailViewModel
    private val bundle = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            _param1 = it.getInt(ARG_PARAM1)
            print(param1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = MusicianDetailFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModelAdapter = PerformerPrizeAdapter()
        viewModelAdapterAlbums = MusicianAlbumAdapter()
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.prizesRv
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = viewModelAdapter

        recyclerViewAlbums = binding.albumsRv
        recyclerViewAlbums.layoutManager = LinearLayoutManager(context)
        recyclerViewAlbums.adapter = viewModelAdapterAlbums

        bundle.putString("musicianId", arguments?.getInt("musicianId").toString())

        view.findViewById<FloatingActionButton>(R.id.add_button_prize).setOnClickListener {
            findNavController().navigate(R.id.action_musicianDetailFragment_to_musicianAddPrizeFragment, bundle)
        }

        view.findViewById<FloatingActionButton>(R.id.add_button_album).setOnClickListener {
            findNavController().navigate(R.id.action_musicianDetailFragment_to_musicianAddAlbumFragment, bundle)
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
        }
        activity.actionBar?.title = getString(R.string.musician_detail_prize_tittle)
        //val args: CommentFragmentArgs by navArgs()
        //Log.d("Args", args.albumId.toString())

        val sdfInput = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val sdfOutput = SimpleDateFormat("yyyy-MM-dd")

        viewModel =
            ViewModelProvider(this, MusicianDetailViewModel.Factory(activity.application, param1)).get(
                MusicianDetailViewModel::class.java
            )
        viewModel.musician.observe(viewLifecycleOwner) {

            binding.name.text = it.name
            val date: Date? = sdfInput.parse(it.birthDate)
            val formattedDate: String = sdfOutput.format(date!!)
            binding.birthdate.text = formattedDate
            binding.description.text = it.description
            bundle.putString("name", "${getString(R.string.selected_by)} ${it.name}:")

            viewModelAdapter!!.prizes = it.performerPrizes
            if (it.performerPrizes.isEmpty()) {
                binding.noPrizes.visibility = View.VISIBLE
            } else {
                binding.noPrizes.visibility = View.GONE
            }
            Glide.with(this)
                .load(it.image.toUri().buildUpon().scheme("https").build())
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.loading_animation)

                        .error(R.drawable.ic_broken_image)
                )
                .into(binding.image)

            viewModelAdapterAlbums!!.albums = it.albums
            if (it.albums.isEmpty()) {
                binding.noAlbums.visibility = View.VISIBLE
            } else {
                binding.noAlbums.visibility = View.GONE
            }
        }

        viewModel.eventNetworkError.observe(
            viewLifecycleOwner)
            { isNetworkError ->
                if (isNetworkError) onNetworkError()
            }

    }

    private fun onNetworkError() {
        if (!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, getString(R.string.network_error), Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }


}