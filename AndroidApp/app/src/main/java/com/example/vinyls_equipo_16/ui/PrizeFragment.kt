package com.example.vinyls_equipo_16.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.vinyls_equipo_16.R
import com.example.vinyls_equipo_16.databinding.PrizeFragmentBinding
import com.example.vinyls_equipo_16.ui.adapters.PrizesAdapter
import com.example.vinyls_equipo_16.viewmodels.PrizeViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton


@Suppress("DEPRECATION")
class PrizeFragment : Fragment() {
    private var _binding: PrizeFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: PrizeViewModel
    private var viewModelAdapter: PrizesAdapter? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PrizeFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModelAdapter = PrizesAdapter()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.prizesRv
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = viewModelAdapter

        view.findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            findNavController().navigate(R.id.action_prizeFragment_to_prizeNewFragment)
        }

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout?.setOnRefreshListener {
            viewModel.RefreshData()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        activity.actionBar?.title = getString(R.string.title_prizes)
        viewModel = ViewModelProvider(this, PrizeViewModel.Factory(activity.application)).get(
            PrizeViewModel::class.java)
        viewModel.prizes.observe(viewLifecycleOwner) {
            it.apply {
                viewModelAdapter!!.prizes = this
            }
        }
        viewModel.eventNetworkError.observe(viewLifecycleOwner) { isNetworkError ->
            if (isNetworkError) onNetworkError()
        }
        viewModel.dataLoaded.observe(viewLifecycleOwner) { isDataLoaded ->
            swipeRefreshLayout?.isRefreshing = !isDataLoaded;
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onNetworkError() {
        if(!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, getString(R.string.network_error), Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }
}