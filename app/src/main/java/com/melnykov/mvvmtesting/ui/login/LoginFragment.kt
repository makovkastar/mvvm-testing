package com.melnykov.mvvmtesting.ui.login

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.annotation.VisibleForTesting
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.melnykov.mvvmtesting.R
import com.melnykov.mvvmtesting.databinding.LoginFragmentBinding
import com.melnykov.mvvmtesting.injection.Injectable
import com.melnykov.mvvmtesting.ui.home.HomeActivity
import com.melnykov.mvvmtesting.util.Urls
import com.melnykov.mvvmtesting.util.showToast
import com.melnykov.mvvmtesting.util.startBrowser
import javax.inject.Inject

class LoginFragment : Fragment(), Injectable {

    @Inject
    @VisibleForTesting
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var dataBinding: LoginFragmentBinding

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(LoginViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = LoginFragmentBinding.inflate(
            inflater, container, false)
        dataBinding.viewModel = viewModel
        return dataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.navigateToForgotPasswordPage.observe(this, Observer {
            startBrowser(Urls.FORGOT_PASSWORD)
        })

        viewModel.showErrorToast.observe(this, Observer {
            showToast(R.string.login_error_invalid_credentials)
        })

        viewModel.navigateToNextScreen.observe(this, Observer {
            startHomeActivity()
            activity?.finish()
        })
    }

    private fun startHomeActivity() {
        startActivity(Intent(activity, HomeActivity::class.java))
    }
}
