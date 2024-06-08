package com.example.alkewalletfinal.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.alkewalletfinal.databinding.FragmentHeaderHomePageBinding
import com.example.alkewalletfinal.utils.SharedPreferencesManager

class HeaderHomePageFragment : Fragment() {
    private var _binding: FragmentHeaderHomePageBinding? = null
    private val binding get() = _binding!!

    private val sharedPreferencesManager by lazy { SharedPreferencesManager(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHeaderHomePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHeaderData()
    }

    override fun onResume() {
        super.onResume()
        setHeaderData() // Actualizar los datos del encabezado cada vez que el fragmento sea visible
    }

    private fun setHeaderData() {
        val user = sharedPreferencesManager.getUser()
        val getSaldo = sharedPreferencesManager.getSaldo()
        val firstName = user?.firstName ?: "Usuario"
        binding.saludo.text = "Hola $firstName"
        binding.saldo.text = "Saldo: $$getSaldo"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
