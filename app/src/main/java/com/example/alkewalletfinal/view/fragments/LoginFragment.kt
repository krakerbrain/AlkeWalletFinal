package com.example.alkewalletfinal.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.alkewalletfinal.R
import com.example.alkewalletfinal.databinding.FragmentLoginBinding
import com.example.alkewalletfinal.utils.SharedPreferencesManager
import com.example.alkewalletfinal.viewmodel.LoginViewModel
import com.example.alkewalletfinal.viewmodel.ViewModelFactory
import androidx.room.Room
import com.example.alkewalletfinal.api.ApiClient
import com.example.alkewalletfinal.data.database.AppDatabase
import com.example.alkewalletfinal.data.repository.TransactionRepository
import com.example.alkewalletfinal.utils.TransactionFetcher

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
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

    private val loginViewModel: LoginViewModel by viewModels {
        ViewModelFactory(sharedPreferencesManager, transactionFetcher)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        setEmailInput()
        return binding.root
    }

    /**
     * Set email input desde sharedpreferences
     */
    private fun setEmailInput() {
        binding.editTextEmail.setText(sharedPreferencesManager.getUser()?.email)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginBtn.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            loginViewModel.login(email, password)
        }

        binding.tieneCuenta.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }

        loginViewModel.loginResult.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                findNavController().navigate(R.id.action_loginFragment_to_homePageFragment)
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.login_failed),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}