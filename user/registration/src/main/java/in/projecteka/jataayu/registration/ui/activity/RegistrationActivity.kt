package `in`.projecteka.jataayu.registration.ui.activity

import `in`.projecteka.jataayu.network.utils.Failure
import `in`.projecteka.jataayu.network.utils.Loading
import `in`.projecteka.jataayu.network.utils.PartialFailure
import `in`.projecteka.jataayu.network.utils.Success
import `in`.projecteka.jataayu.presentation.showAlertDialog
import `in`.projecteka.jataayu.presentation.showErrorDialog
import `in`.projecteka.jataayu.presentation.ui.BaseActivity
import `in`.projecteka.jataayu.registration.ui.activity.databinding.RegistrationActivityBinding
import `in`.projecteka.jataayu.registration.ui.fragment.RegistrationFragment
import `in`.projecteka.jataayu.registration.ui.fragment.RegistrationOtpFragment
import `in`.projecteka.jataayu.registration.viewmodel.RegistrationActivityViewModel
import `in`.projecteka.jataayu.registration.viewmodel.RegistrationActivityViewModel.Show
import `in`.projecteka.jataayu.util.startAccountCreation
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegistrationActivity : BaseActivity<RegistrationActivityBinding>() {

    override fun layoutId(): Int = R.layout.registration_activity

    private val viewModel by viewModel<RegistrationActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        viewModel.redirectToRegistrationScreen()
        initObservers()
        initToolbar()
    }

    private fun initToolbar(){
        setSupportActionBar(binding.appToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.appToolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun initObservers() {
        viewModel.redirectTo.observe(this, Observer {
            when (it) {
                Show.VERIFICATION -> replaceFragment(RegistrationOtpFragment.newInstance(), R.id.fragment_container)
                Show.REGISTRATION -> replaceFragment(RegistrationFragment.newInstance(), R.id.fragment_container)
                Show.NEXT -> {
                    finish()
                    startAccountCreation(this) {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                }
            }
        })

        viewModel.requestVerificationResponseLiveData.observe(this, Observer {
            when (it) {
                is Success -> {
                    viewModel.redirectToOtpScreen(it.data?.sessionId)
                }
                is Loading -> {
                    viewModel.showProgress(it.isLoading, R.string.sending_otp)
                }
                is Failure -> {
                    showErrorDialog(it.error.localizedMessage)
                }
                is PartialFailure -> {
                    showAlertDialog(getString(R.string.failure), it.error?.message, getString(android.R.string.ok))
                }
            }
        })
        viewModel.verifyIdentifierResponseLiveData.observe(this, Observer {
            when (it) {
                is Success -> {
                    viewModel.credentialsRepository.accessToken = it.data?.temporaryToken
                    viewModel.preferenceRepository.isUserRegistered = true
                    viewModel.preferenceRepository.mobileIdentifier = viewModel.getIdentifierValue()
                    viewModel.redirectToNext()
                }
                is Loading -> {
                    viewModel.showProgress(it.isLoading, R.string.sending_otp)
                }
                is Failure -> {
                    showErrorDialog(it.error.localizedMessage)
                }
            }
        })
    }
}