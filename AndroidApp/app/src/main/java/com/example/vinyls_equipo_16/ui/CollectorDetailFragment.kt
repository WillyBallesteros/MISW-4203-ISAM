package com.example.vinyls_equipo_16.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vinyls_equipo_16.R
import com.example.vinyls_equipo_16.databinding.CollectorDetailFragmentBinding
import com.example.vinyls_equipo_16.ui.adapters.CollectorAlbumsAdapter
import com.example.vinyls_equipo_16.ui.adapters.CommentsAdapter
import com.example.vinyls_equipo_16.ui.adapters.FavoritePerformersAdapter
import com.example.vinyls_equipo_16.viewmodels.CollectorDetailViewModel

private const val ARG_PARAM1 = "collectorId"

@Suppress("DEPRECATION")
class CollectorDetailFragment : Fragment() {
    private var _param1: Int? = null
    private val param1 get() = _param1!!
    private lateinit var recyclerViewComments: RecyclerView
    private lateinit var recyclerViewFavoritePerformers: RecyclerView
    private lateinit var recyclerViewCollectorAlbums: RecyclerView
    private var _binding: CollectorDetailFragmentBinding? = null
    private val binding get() = _binding!!
    private var viewModelAdapterComments: CommentsAdapter? = null
    private var viewModelAdapterFavoritePerformers: FavoritePerformersAdapter? = null
    private var viewModelAdapterCollectorAlbums: CollectorAlbumsAdapter? = null
    private lateinit var viewModel: CollectorDetailViewModel


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
        _binding = CollectorDetailFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModelAdapterComments = CommentsAdapter()
        viewModelAdapterFavoritePerformers = FavoritePerformersAdapter()
        viewModelAdapterCollectorAlbums = CollectorAlbumsAdapter()
        // binding.description.text = param1.toString()


        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerViewComments = binding.commentRv
        recyclerViewComments.layoutManager = LinearLayoutManager(context)
        recyclerViewComments.adapter = viewModelAdapterComments

        recyclerViewFavoritePerformers = binding.favoritePerformerRv
        recyclerViewFavoritePerformers.layoutManager = LinearLayoutManager(context)
        recyclerViewFavoritePerformers.adapter = viewModelAdapterFavoritePerformers

        recyclerViewCollectorAlbums = binding.collectorAlbumRv
        recyclerViewCollectorAlbums.layoutManager = LinearLayoutManager(context)
        recyclerViewCollectorAlbums.adapter = viewModelAdapterCollectorAlbums


    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
        }
        activity.actionBar?.title = getString(R.string.comment_detail_record_list)
        //val args: CommentFragmentArgs by navArgs()
        //Log.d("Args", args.collectorId.toString())

        viewModel =
            ViewModelProvider(this, CollectorDetailViewModel.Factory(activity.application, param1)).get(
                CollectorDetailViewModel::class.java
            )
        viewModel.collector.observe(this.viewLifecycleOwner) {

            binding.name.text = it.name
            binding.telephone.text = it.telephone
            binding.email.text = it.email

            viewModelAdapterComments!!.comments = it.comments
            if (it.comments.isEmpty()) {
                binding.noComments.visibility = View.VISIBLE
            } else {
                binding.noComments.visibility = View.GONE
            }

            viewModelAdapterFavoritePerformers!!.favoritePerformers = it.favoritePerformers
            if (it.favoritePerformers.isEmpty()) {
                binding.noFavoritePerformers.visibility = View.VISIBLE
            } else {
                binding.noFavoritePerformers.visibility = View.GONE
            }

            viewModelAdapterCollectorAlbums!!.collectorAlbums = it.collectorAlbums
            if (it.collectorAlbums.isEmpty()) {
                binding.noCollectorsAlbums.visibility = View.VISIBLE
            } else {
                binding.noCollectorsAlbums.visibility = View.GONE
            }


            /*it.apply {
                viewModelAdapter!!.collector = this
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
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }


}