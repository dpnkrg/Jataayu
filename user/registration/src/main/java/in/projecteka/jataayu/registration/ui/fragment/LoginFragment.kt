package `in`.projecteka.jataayu.registration.ui.fragment

import `in`.projecteka.jataayu.network.utils.Failure
import `in`.projecteka.jataayu.network.utils.Loading
import `in`.projecteka.jataayu.network.utils.PartialFailure
import `in`.projecteka.jataayu.network.utils.Success
import `in`.projecteka.jataayu.presentation.showAlertDialog
import `in`.projecteka.jataayu.presentation.showErrorDialog
import `in`.projecteka.jataayu.presentation.ui.fragment.BaseDialogFragment
import `in`.projecteka.jataayu.registration.listener.LoginClickHandler
import `in`.projecteka.jataayu.registration.listener.LoginEnableListener
import `in`.projecteka.jataayu.registration.listener.LoginValuesWatcher
import `in`.projecteka.jataayu.registration.ui.activity.R
import `in`.projecteka.jataayu.registration.ui.activity.databinding.FragmentLoginBinding
import `in`.projecteka.jataayu.registration.viewmodel.LoginViewModel
import `in`.projecteka.jataayu.util.sharedPref.setAuthToken
import `in`.projecteka.jataayu.util.sharedPref.setIsUserLoggedIn
import `in`.projecteka.jataayu.util.startLauncher
import `in`.projecteka.jataayu.util.startRegistration
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginFragment : BaseDialogFragment(), LoginClickHandler, LoginEnableListener{
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModel()

    companion object {
        const val SPACE = " "
        fun newInstance() = LoginFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }


    override fun onRegisterClick(view: View) {
        startRegistration(activity!!, null)
//        activity?.setResult(Activity.RESULT_FIRST_USER)
//        activity?.finish()
    }

    override fun onLoginClick(view: View) {
        viewModel.login(getUsername(), binding.etPassword.text.toString())
    }

    private fun getProviderName(): String {
        return binding.tvProviderName.text.toString()
    }

    private fun getUsername(): String {
        return et_username?.text.toString() + getProviderName()
    }

    override fun showOrHidePassword(view: View) {
        when (binding.etPassword.inputType) {
            getPasswordInputType() -> {
                binding.passwordInputType = getVisiblePasswordInputType()
                binding.btnShowHidePassword.text = getString(R.string.hide)
            }
            getVisiblePasswordInputType() -> {
                binding.passwordInputType = getPasswordInputType()
                binding.btnShowHidePassword.text = getString(R.string.show)
            }
        }
        binding.etPassword.post { binding.etPassword.setSelection(binding.etPassword.text.length) }
    }

    private fun getVisiblePasswordInputType(): Int {
        return InputType.TYPE_CLASS_TEXT + InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
    }


    private fun getPasswordInputType(): Int {
        return InputType.TYPE_CLASS_TEXT + InputType.TYPE_TEXT_VARIATION_PASSWORD
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initBindings()
    }

    private fun initObservers() {
        viewModel.loginResponse.observe(this, Observer {
            when (it) {
                is Loading -> showProgressBar(it.isLoading, getString(R.string.logging_in))
                is Success -> {
                    activity?.setAuthToken(viewModel.getAuthTokenWithTokenType(authToken = it.data?.accessToken, tokenType = it.data?.tokenType))
                    activity?.setIsUserLoggedIn(true)
                    activity?.finish()
                    startLauncher(activity!!)

                }
                is PartialFailure -> {
                    context?.showAlertDialog(
                        getString(R.string.failure), it.error?.message,
                        getString(android.R.string.ok)
                    )
                }
                is Failure -> {
                    context?.showErrorDialog(it.error.localizedMessage)
                }
            }
        })
    }

    private fun initBindings() {
        binding.passwordInputType = getPasswordInputType()
        binding.loginClickHandler = this
        binding.enableLogin = false
        binding.loginValuesWatcher = LoginValuesWatcher(this)
    }

    override fun enableLoginButton() {
        binding.enableLogin = et_username.text.isNotEmpty() && et_password.text.isNotEmpty()
    }
}