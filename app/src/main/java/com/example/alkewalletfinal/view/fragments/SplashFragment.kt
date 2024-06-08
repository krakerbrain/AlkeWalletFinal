package com.example.alkewalletfinal.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.alkewalletfinal.R
import com.example.alkewalletfinal.databinding.FragmentSplashBinding


class SplashFragment : Fragment() {
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        val view = binding.root
        val splashScreenDuration = resources.getInteger(R.integer.splash_screen_duration).toLong()

        // Posterga la transición a la siguiente actividad después de splashScreenDuration milisegundos
        view.postDelayed({
            findNavController().navigate(R.id.navigate_splashFragment_to_LoginSignupFragment)
        }, splashScreenDuration)

        return view
    }
}