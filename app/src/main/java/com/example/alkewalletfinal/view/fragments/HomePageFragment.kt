package com.example.alkewalletfinal.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
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
    private val transactionDao by lazy {
        AppDatabase.getDatabase(requireContext()).transactionDao()
    }
    private lateinit var sharedPreferencesManager: SharedPreferencesManager

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
        sharedPreferencesManager = SharedPreferencesManager(requireContext())

        // Obtener saldo
        val saldoStr = sharedPreferencesManager.getSaldo()
        val saldo = saldoStr?.toFloatOrNull()

        // Se cambia el color del botón según el saldo
        if (saldo != null && saldo > 0) {
            binding.enviarDinero.backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.verde) // Color habilitado

        } else {
            binding.enviarDinero.backgroundTintList = ContextCompat.getColorStateList(
                requireContext(),
                R.color.gris_claro
            ) // Color deshabilitado

        }

        binding.ingresarDinero.setOnClickListener {
            findNavController().navigate(R.id.action_homePageFragment_to_ingresarDineroFragment,
                Bundle().apply {
                    putString("transaction_type", "topup")
                })
        }

        binding.enviarDinero.setOnClickListener {
            //Se verifica el saldo si es 0 se envia mensaje de que no se pueden enviar dinero.
            //Se prefiere mantener el click para mostrar mensaje al usuario
            if (saldo != null && saldo > 0) {
                findNavController().navigate(R.id.action_homePageFragment_to_enviarDineroFragment,
                    Bundle().apply {
                        putString("transaction_type", "payment")
                    })

            } else {
                Toast.makeText(
                    requireContext(),
                    "No se pueden hacer transferencias de una cuenta sin saldo",
                    Toast.LENGTH_SHORT
                ).show()
            }

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
                if (it.isEmpty()) {
                    binding.transactionsRecyclerView.visibility = View.GONE
                    binding.noTransactionsView.visibility = View.VISIBLE
                } else {
                    binding.transactionsRecyclerView.visibility = View.VISIBLE
                    binding.noTransactionsView.visibility = View.GONE
                    transactionAdapter.submitList(it)
                }
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