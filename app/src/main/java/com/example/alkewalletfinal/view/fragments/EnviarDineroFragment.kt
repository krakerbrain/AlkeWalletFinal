package com.example.alkewalletfinal.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.example.alkewalletfinal.R
import com.example.alkewalletfinal.api.ApiClient
import com.example.alkewalletfinal.data.database.AppDatabase
import com.example.alkewalletfinal.data.repository.TransactionRepository
import com.example.alkewalletfinal.databinding.FragmentEnviarDineroBinding
import com.example.alkewalletfinal.utils.SharedPreferencesManager
import com.example.alkewalletfinal.utils.TransactionFetcher
import com.example.alkewalletfinal.viewmodel.LoginViewModel
import com.example.alkewalletfinal.viewmodel.TransactionViewModel
import com.example.alkewalletfinal.viewmodel.ViewModelFactory

class EnviarDineroFragment : Fragment() {

    private var _binding: FragmentEnviarDineroBinding? = null
    private val binding get() = _binding!!

    private val sharedPreferencesManager by lazy { SharedPreferencesManager(requireContext()) }
    private val appDatabase by lazy {
        AppDatabase.getDatabase(requireContext())
    }

    private val transactionRepository by lazy {
        TransactionRepository(appDatabase.transactionDao(), ApiClient.apiService)
    }

    private val transactionFetcher by lazy {
        TransactionFetcher(transactionRepository)
    }
    private val transactionViewModel: TransactionViewModel by viewModels {
        ViewModelFactory(
            sharedPreferencesManager,
            transactionFetcher
        )
    }
    private val loginViewModel: LoginViewModel by viewModels {
        ViewModelFactory(
            sharedPreferencesManager,
            transactionFetcher
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEnviarDineroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupHeaderFragment()

        binding.enviarBtn.setOnClickListener {
            val type = "payment"
            val concept = binding.conceptoEnvio.text.toString()
            //Se multiplica por -1 para que el monto sea negativo
            val amount = binding.montoEnvio.text.toString().toLong() * -1

            transactionViewModel.depositarOtransferir(type, concept, amount)
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        transactionViewModel.transactionResult.observe(viewLifecycleOwner) { result ->
            if (result) {
                val token = sharedPreferencesManager.getAuthToken()
                if (token != null) {
                    loginViewModel.getUserAccountsDetails(token)
                    loginViewModel.accountDetailsUpdated.observe(viewLifecycleOwner) { isUpdated ->
                        if (isUpdated) {
                            Toast.makeText(context, "Pago realizado correctamente", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.action_enviarDineroFragment_to_homePageFragment)
                        } else {
                            Toast.makeText(context, "Error al actualizar el saldo", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(context, "Token no disponible", Toast.LENGTH_SHORT).show()
                }
            } else {
                transactionViewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMessage ->
                    Toast.makeText(context, "Error en el pago: $errorMessage", Toast.LENGTH_SHORT).show()
                })
            }
        }
    }

    private fun setupHeaderFragment() {
        val fragment = TransactionHeaderFragment()
        childFragmentManager.beginTransaction()
            .replace(R.id.user_container, fragment)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}