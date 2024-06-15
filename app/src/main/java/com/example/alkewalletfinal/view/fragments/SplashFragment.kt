package com.example.alkewalletfinal.view.fragments

import android.graphics.drawable.AnimatedVectorDrawable
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

        // Configura el ImageView como invisible inicialmente
        binding.splashImage.visibility = View.INVISIBLE

        // Agregar un retraso antes de iniciar la animación
        val animationStartDelay = 500L // 500 ms de retraso antes de iniciar la animación

        view.postDelayed({
            // Hacer visible el ImageView justo antes de iniciar la animación
            binding.splashImage.visibility = View.VISIBLE
            // Inicializa la animación
            val animatedVectorDrawable = binding.splashImage.drawable as AnimatedVectorDrawable
            animatedVectorDrawable.start()
        }, animationStartDelay)

        // Posterga la transición a la siguiente actividad después de splashScreenDuration milisegundos
        view.postDelayed({
            findNavController().navigate(R.id.navigate_splashFragment_to_LoginSignupFragment)
        }, splashScreenDuration)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
