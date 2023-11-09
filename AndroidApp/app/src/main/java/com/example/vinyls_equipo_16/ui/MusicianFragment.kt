package com.example.vinyls_equipo_16.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vinyls_equipo_16.R
import com.example.vinyls_equipo_16.databinding.MusicianFragmentBinding
import com.example.vinyls_equipo_16.models.Album
import com.example.vinyls_equipo_16.models.Musician
import com.example.vinyls_equipo_16.ui.adapters.MusiciansAdapter
import com.example.vinyls_equipo_16.viewmodels.AlbumViewModel
import com.example.vinyls_equipo_16.viewmodels.MusicianViewModel


class MusicianFragment : Fragment() {
    private var _binding: MusicianFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: MusicianViewModel
    private var viewModelAdapter: MusiciansAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MusicianFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModelAdapter = MusiciansAdapter()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.musiciansRv
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = viewModelAdapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        activity.actionBar?.title = getString(R.string.title_musicians)
        viewModel = ViewModelProvider(this, MusicianViewModel.Factory(activity.application)).get(
            MusicianViewModel::class.java)
        viewModel.musicians.observe(viewLifecycleOwner, Observer<List<Musician>> {
            it.apply {
                viewModelAdapter!!.musicians = this
            }
        })
        viewModel.eventNetworkError.observe(viewLifecycleOwner, Observer<Boolean> { isNetworkError ->
            if (isNetworkError) onNetworkError()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onNetworkError() {
        if(!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }




}