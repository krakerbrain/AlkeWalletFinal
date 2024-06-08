package com.example.alkewalletfinal.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alkewalletfinal.R
import com.example.alkewalletfinal.api.ApiClient
import com.example.alkewalletfinal.data.database.AppDatabase
import com.example.alkewalletfinal.data.repository.TransactionRepository
import com.example.alkewalletfinal.databinding.FragmentHomePageBinding
import com.example.alkewalletfinal.utils.SharedPreferencesManager
import com.example.alkewalletfinal.utils.TransactionFetcher
import com.example.alkewalletfinal.view.adapter.TransactionAdapter
import com.example.alkewalletfinal.viewmodel.TransactionViewModel
import com.example.alkewalletfinal.viewmodel.ViewModelFactory


class HomePageFragment : Fragment() {

    private var _binding: FragmentHomePageBinding? = null
    private val binding get() = _binding!!
    private val transactionAdapter = TransactionAdapter()
    private val transactionDao by lazy { AppDatabase.getDatabase(requireContext()).transactionDao() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupHeaderFragment()
        setupRecyclerView()
        loadTransactions()
        binding.ingresarDinero.setOnClickListener {
            findNavController().navigate(R.id.action_homePageFragment_to_ingresarDineroFragment,
                Bundle().apply {
                    putString("transaction_type", "topup")
                })
        }

        binding.enviarDinero.setOnClickListener {
            findNavController().navigate(R.id.action_homePageFragment_to_enviarDineroFragment,
                Bundle().apply {
                    putString("transaction_type", "payment")
                })
        }

        binding.headerContainerHomePage.profileImg.setOnClickListener {
            findNavController().navigate(R.id.action_homePageFragment_to_profileFragment)
        }

    }

    private fun setupRecyclerView() {
        binding.transactionsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = transactionAdapter
        }
    }

    private fun loadTransactions() {
        transactionDao.getAllTransactions().observe(viewLifecycleOwner, Observer { transactions ->
            transactions?.let {
                transactionAdapter.submitList(it)
            }
        })
    }

    private fun setupHeaderFragment() {
        val fragment = HeaderHomePageFragment()
        childFragmentManager.beginTransaction()
            .replace(R.id.headerContainerHomePage, fragment)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}