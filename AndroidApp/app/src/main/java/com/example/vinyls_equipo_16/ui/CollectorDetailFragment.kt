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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.vinyls_equipo_16.R
import com.example.vinyls_equipo_16.databinding.CollectorDetailFragmentBinding
import com.example.vinyls_equipo_16.models.CollectorDetail
import com.example.vinyls_equipo_16.ui.adapters.CommentsAdapter
import com.example.vinyls_equipo_16.viewmodels.CollectorDetailViewModel

private const val ARG_PARAM1 = "collectorId"

class CollectorDetailFragment : Fragment() {
    private var _param1: Int? = null
    private val param1 get() = _param1!!
    private lateinit var recyclerView: RecyclerView
    private var _binding: CollectorDetailFragmentBinding? = null
    private val binding get() = _binding!!
    private var viewModelAdapter: CommentsAdapter? = null
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
    ): View? {
        // Inflate the layout for this fragment
        _binding = CollectorDetailFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModelAdapter = CommentsAdapter()
        // binding.description.text = param1.toString()


        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.commentRv
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = viewModelAdapter
    }

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
        viewModel.collector.observe(viewLifecycleOwner, Observer<CollectorDetail> {

            binding.name.text = it.name
            binding.telephone.text = it.telephone
            binding.email.text = it.email

            viewModelAdapter!!.comments = it.comments
            if (it.comments.isEmpty()) {
                binding.noComments.visibility = View.VISIBLE
            } else {
                binding.noComments.visibility = View.GONE
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
        })
        viewModel.eventNetworkError.observe(
            viewLifecycleOwner,
            Observer<Boolean> { isNetworkError ->
                if (isNetworkError) onNetworkError()
            })

    }

    private fun onNetworkError() {
        if (!viewModel.isNetworkErrorShown.value!!) {
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
         * @return A new instance of fragment CollectorDetailFragment.
         */
// TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CollectorDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}