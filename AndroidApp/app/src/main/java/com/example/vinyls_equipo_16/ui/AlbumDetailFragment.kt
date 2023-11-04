package com.example.vinyls_equipo_16.ui


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.vinyls_equipo_16.R
import com.example.vinyls_equipo_16.databinding.AlbumDetailFragmentBinding
import com.example.vinyls_equipo_16.models.AlbumDetail
import com.example.vinyls_equipo_16.ui.adapters.AlbumsAdapter
import com.example.vinyls_equipo_16.viewmodels.AlbumDetailViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "albumId"

/**
 * A simple [Fragment] subclass.
 * Use the [AlbumDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AlbumDetailFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var _param1: Int? = null
    private val param1 get() = _param1!!
    private var _binding: AlbumDetailFragmentBinding? = null
    private val binding get() = _binding!!
    private var viewModelAdapter: AlbumsAdapter? = null
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
        viewModelAdapter = AlbumsAdapter()
        binding.idAlbum.text = param1.toString()






        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        activity.actionBar?.title = getString(R.string.title_comments)
        val args: CommentFragmentArgs by navArgs()
        Log.d("Args", args.albumId.toString())

        viewModel = ViewModelProvider(this, AlbumDetailViewModel.Factory(activity.application, param1)).get(AlbumDetailViewModel::class.java)
        viewModel.album.observe(viewLifecycleOwner, Observer<AlbumDetail> {
            binding.idAlbum.text = it.description
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