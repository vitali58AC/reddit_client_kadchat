package com.kadun.kadchat.ui.intro

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.kadun.kadchat.common.InsetsWithBindingFragment
import com.kadun.kadchat.data.network.RedditAuthClient.Companion.fixRedditAuthUrl
import com.kadun.kadchat.databinding.FragmentAuthBinding
import com.kirkbushman.auth.RedditAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class AuthFragment : InsetsWithBindingFragment<FragmentAuthBinding>() {

    private val authClient: RedditAuth by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initWebView()
    }

    private fun initWebView() {
        if (authClient.hasSavedBearer()) {
            openHomeFragment()
        } else {
            binding.wbRoot.webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                    val isRedirected = authClient.isRedirectedUrl(url)
                    if (isRedirected) {
                        binding.wbRoot.stopLoading()
                        lifecycleScope.launch(Dispatchers.IO) {
                            authClient.getTokenBearer(url)?.let {
                                openHomeFragment()
                            }
                        }
                    }
                }
            }
            binding.wbRoot.clearFormData()
            binding.wbRoot.loadUrl(authClient.provideAuthorizeUrl().fixRedditAuthUrl())
        }
    }

    private fun openHomeFragment() = viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
        findNavController().navigate(AuthFragmentDirections.actionAuthFragmentToHomeNavigation())
    }

    override fun getTopView() = binding.root
    override fun getRootView() = binding.root
    override fun getBinding(): (inflater: LayoutInflater, root: ViewGroup?, attachToRoot: Boolean) -> FragmentAuthBinding =
        FragmentAuthBinding::inflate
}