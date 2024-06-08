package com.example.alkewalletfinal.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.alkewalletfinal.databinding.FragmentLoginSignupBinding
import androidx.navigation.fragment.findNavController
import com.example.alkewalletfinal.R


class LoginSignUpFragment : Fragment() {
    private var _binding: FragmentLoginSignupBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tieneCuenta.setOnClickListener {
            findNavController().navigate(R.id.action_loginSignUpFragment_to_loginFragment)
        }

        binding.creaCuenta.setOnClickListener {
            findNavController().navigate(R.id.action_loginSignUpFragment_to_signUpFragment)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}