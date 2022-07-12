package com.farzonestudios.toptracer.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.farzonestudios.toptracer.R
import com.farzonestudios.toptracer.databinding.FragmentMainBinding
import com.farzonestudios.toptracer.ui.utils.hideKeyboard
import com.farzonestudios.toptracer.ui.utils.listenToTextChange
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentMainBinding.inflate(inflater, container, false)
        .also {
            _binding = it
        }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenToActions()
        listenToState()
    }

    private fun listenToActions() {
        lifecycleScope.launchWhenResumed {
            binding.textInputEditUsername.listenToTextChange()
                .combine(binding.textInputEditPassword.listenToTextChange()) { username, password ->
                    viewModel.onInputFieldsChanged(username, password)
                }
                .collect()
        }

        binding.buttonLogin.setOnClickListener {
            requireContext().hideKeyboard(binding.buttonLogin)
            viewModel.doLogin(
                username = binding.textInputEditUsername.text?.toString().orEmpty(),
                password = binding.textInputEditPassword.text?.toString().orEmpty()
            )
        }

        binding.buttonForgot.setOnClickListener {
            viewModel.onForgotPasswordClick()
        }
    }

    private fun listenToState() {
        lifecycleScope.launchWhenResumed {
            viewModel.state
                .onEach {
                    when (it) {
                        MainViewModel.Event.NavigateToDetails ->
                            findNavController().navigate(R.id.action_main_to_details)
                        is MainViewModel.Event.ShowSnackbar -> showSnackbar(it.message)
                        is MainViewModel.Event.SetLoginEnabled ->
                            binding.buttonLogin.isEnabled = it.isEnabled
                    }
                }
                .collect()
        }
    }

    private fun showSnackbar(message: String) {
        view?.let {
            Snackbar.make(it, message, Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}