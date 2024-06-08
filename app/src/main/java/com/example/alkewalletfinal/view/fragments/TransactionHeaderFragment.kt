package com.example.alkewalletfinal.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.alkewalletfinal.databinding.FragmentTransactionHeaderBinding
import com.example.alkewalletfinal.utils.SharedPreferencesManager


class TransactionHeaderFragment : Fragment() {
    private var _binding: FragmentTransactionHeaderBinding? = null
    private val binding get() = _binding!!

    private val sharedPreferencesManager by lazy { SharedPreferencesManager(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransactionHeaderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHeaderData()
    }

    private fun setHeaderData() {
        val user = sharedPreferencesManager.getUser()
        val firstName = user?.firstName ?: "Usuario"
        val lastName = user?.lastName ?: "Usuario"
        val correo = user?.email ?: "No registrado"
        binding.nameTextView.text = "$firstName $lastName"
        binding.mailTextView.text = "$correo"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}