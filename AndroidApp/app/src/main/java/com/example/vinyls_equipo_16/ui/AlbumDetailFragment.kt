package com.example.vinyls_equipo_16.ui


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
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
import com.example.vinyls_equipo_16.databinding.AlbumDetailFragmentBinding
import com.example.vinyls_equipo_16.ui.adapters.CommentsAdapter
import com.example.vinyls_equipo_16.ui.adapters.TracksAdapter
import com.example.vinyls_equipo_16.viewmodels.AlbumDetailViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat


private const val ARG_PARAM1 = "albumId"


@Suppress("DEPRECATION")
class AlbumDetailFragment : Fragment() {

    private var _param1: Int? = null
    private val param1 get() = _param1!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewComments: RecyclerView
    private var _binding: AlbumDetailFragmentBinding? = null
    private val binding get() = _binding!!
    private var viewModelAdapter: TracksAdapter? = null
    private var viewModelAdapterComments: CommentsAdapter? = null
    private lateinit var viewModel: AlbumDetailViewModel
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
        _binding = AlbumDetailFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModelAdapter = TracksAdapter()
        viewModelAdapterComments = CommentsAdapter()
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.trackRv
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = viewModelAdapter

        recyclerViewComments = binding.commentRv
        recyclerViewComments.layoutManager = LinearLayoutManager(context)
        recyclerViewComments.adapter = viewModelAdapterComments

        bundle.putString("albumId", arguments?.getInt("albumId").toString())

        view.findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            findNavController().navigate(R.id.action_albumDetailFragment_to_albumAddTrackFragment, bundle)
        }

        view.findViewById<FloatingActionButton>(R.id.comment_icon).setOnClickListener {
            findNavController().navigate(R.id.action_albumDetailFragment_to_albumAddCommentFragment, bundle)
        }

    }

    @SuppressLint("SimpleDateFormat")
    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
        }
        activity.actionBar?.title = getString(R.string.title_comments)
        //val args: CommentFragmentArgs by navArgs()
        //Log.d("Args", args.albumId.toString())

        val sdfInput = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val sdfOutput = SimpleDateFormat("yyyy-MM-dd")

        viewModel =
            ViewModelProvider(this, AlbumDetailViewModel.Factory(activity.application, param1))[AlbumDetailViewModel::class.java]
        viewModel.album.observe(viewLifecycleOwner) {

            binding.name.text = it.name
            val date = sdfInput.parse(it.releaseDate)
            val formattedDate: String = sdfOutput.format(date!!)
            binding.releaseDate.text = formattedDate
            binding.genre.text = it.genre
            binding.recordLabel.text = it.recordLabel
            binding.description.text = it.description
            bundle.putString("cover", it.cover)
            bundle.putString("name", it.name)


            viewModelAdapter!!.tracks = it.tracks
            if (it.tracks.isEmpty()) {
                binding.noSongs.visibility = View.VISIBLE
            } else {
                binding.noSongs.visibility = View.GONE
            }
            viewModelAdapterComments!!.comments = it.comments
            if (it.comments.isEmpty()) {
                binding.noComments.visibility = View.VISIBLE
            } else {
                binding.noComments.visibility = View.GONE
            }
            Glide.with(this)
                .load(it.cover.toUri().buildUpon().scheme("https").build())
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.loading_animation)

                        .error(R.drawable.ic_broken_image)
                )
                .into(binding.albumCover)


            /*it.apply {
                viewModelAdapter!!.album = this
                if(this.isEmpty()){
                    binding.txtNoComments.visibility = View.VISIBLE
                }else{
                    binding.txtNoComments.visibility = View.GONE
                }
                print(this)
            }*/
        }
        viewModel.eventNetworkError.observe(
            viewLifecycleOwner
        ) { isNetworkError ->
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