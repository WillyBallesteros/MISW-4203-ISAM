package com.example.vinyls_equipo_16.ui


import java.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.vinyls_equipo_16.R
import com.example.vinyls_equipo_16.databinding.AlbumDetailFragmentBinding
import com.example.vinyls_equipo_16.models.AlbumDetail
import com.example.vinyls_equipo_16.ui.adapters.TracksAdapter
import com.example.vinyls_equipo_16.viewmodels.AlbumDetailViewModel
import java.util.Date


private const val ARG_PARAM1 = "albumId"


class AlbumDetailFragment : Fragment() {

    private var _param1: Int? = null
    private val param1 get() = _param1!!
    private lateinit var recyclerView: RecyclerView
    private var _binding: AlbumDetailFragmentBinding? = null
    private val binding get() = _binding!!
    private var viewModelAdapter: TracksAdapter? = null
    private lateinit var viewModel: AlbumDetailViewModel


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
    ): View? {
        // Inflate the layout for this fragment
        _binding = AlbumDetailFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModelAdapter = TracksAdapter()
        // binding.description.text = param1.toString()


        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.trackRv
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = viewModelAdapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        activity.actionBar?.title = getString(R.string.title_comments)
        val args: CommentFragmentArgs by navArgs()
        Log.d("Args", args.albumId.toString())

        val sdfInput = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val sdfOutput = SimpleDateFormat("yyyy-MM-dd")

        viewModel = ViewModelProvider(this, AlbumDetailViewModel.Factory(activity.application, param1)).get(AlbumDetailViewModel::class.java)
        viewModel.album.observe(viewLifecycleOwner, Observer<AlbumDetail> {

            binding.name.text = it.name
            val date: Date = sdfInput.parse(it.releaseDate.toString())
            val formattedDate: String = sdfOutput.format(date)
            binding.releaseDate.text =  formattedDate
            binding.genre.text = it.genre
            binding.recordLabel.text = it.recordLabel
            binding.description.text = it.description



            viewModelAdapter!!.tracks = it.tracks
            if(it.tracks.isEmpty()){
                binding.noSongs.visibility = View.VISIBLE
            }else{
                binding.noSongs.visibility = View.GONE
            }
            Glide.with(this)
                .load(it.cover.toUri().buildUpon().scheme("https").build())
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.loading_animation)

                        .error(R.drawable.ic_broken_image))
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
        })
        viewModel.eventNetworkError.observe(viewLifecycleOwner, Observer<Boolean> { isNetworkError ->
            if (isNetworkError) onNetworkError()
        })

    }

    private fun onNetworkError() {
        if(!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AlbumDetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AlbumDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}