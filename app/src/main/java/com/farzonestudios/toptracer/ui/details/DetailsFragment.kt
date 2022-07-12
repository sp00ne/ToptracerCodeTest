package com.farzonestudios.toptracer.ui.details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.ImageLoader
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.farzonestudios.toptracer.R
import com.farzonestudios.toptracer.databinding.FragmentDetailsBinding
import com.farzonestudios.toptracer.ui.details.DetailsViewModel.Event
import com.farzonestudios.toptracer.ui.details.DetailsViewModel.GifState
import com.farzonestudios.toptracer.ui.details.data.Gif
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    @Inject
    lateinit var imageLoader: ImageLoader

    private val viewModel: DetailsViewModel by viewModels()

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentDetailsBinding.inflate(inflater, container, false).also {
        _binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenToActions()
        listenToState()
    }

    private fun listenToActions() {
        binding.buttonLogout.setOnClickListener {
            viewModel.logout()
        }
    }

    private fun listenToState() {
        lifecycleScope.launchWhenResumed {
            viewModel.state
                .onEach {
                    when (it) {
                        Event.NavigateToMain ->
                            findNavController().navigate(R.id.action_details_to_main)
                        is Event.SetState -> setData(it)
                    }
                }
                .collect()
        }
    }

    private fun setData(data: Event.SetState) {
        with(binding) {
            image.setImageResource(0)
            imageProgress.visibility = View.GONE
            binding.textWelcome.text = getString(R.string.welcome_formatted, data.userName)
        }

        when (data.gifState) {
            is GifState.Complete -> showImageAndDescription(data.gifState.gif)
            is GifState.Error -> {
                Log.e(this::class.java.name, "GIF loading error", data.gifState.throwable)
                showError(getString(R.string.error_network))
            }
            GifState.Loading -> {
                binding.imageProgress.visibility = View.VISIBLE
                // Ignoring this one for now. already have a spinner for the image listener
            }
        }
    }

    private fun showImageAndDescription(gif: Gif) {
        Glide.with(this)
            .asGif()
            .load(gif.url)
            .listener(object : RequestListener<GifDrawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<GifDrawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.imageProgress.visibility = View.GONE
                    showError(getString(R.string.gif_load_fail))
                    return false
                }

                override fun onResourceReady(
                    resource: GifDrawable?,
                    model: Any?,
                    target: Target<GifDrawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.imageProgress.visibility = View.GONE
                    binding.textImageDescription.text = gif.description
                    return false
                }

            })
            .into(binding.image)
    }

    private fun showError(message: String) {
        view?.let {
            Snackbar.make(it, message, Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}