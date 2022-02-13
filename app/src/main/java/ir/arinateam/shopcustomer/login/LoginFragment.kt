package ir.arinateam.shopcustomer.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.google.android.material.textfield.TextInputEditText
import ir.arinateam.shopadmin.api.ApiClient
import ir.arinateam.shopcustomer.R
import ir.arinateam.shopcustomer.ShopActivity
import ir.arinateam.shopcustomer.api.ApiInterface
import ir.arinateam.shopcustomer.databinding.LoginFragmentBinding
import ir.arinateam.shopcustomer.login.model.ModelLogin
import ir.arinateam.shopcustomer.utils.Loading
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginFragment : Fragment() {

    private lateinit var bindingFragment: LoginFragmentBinding

    private lateinit var btnLogin: Button
    private lateinit var btnSignup: Button
    private lateinit var edPhoneNumber: TextInputEditText
    private lateinit var edPassword: TextInputEditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        bindingFragment =
            DataBindingUtil.inflate(inflater, R.layout.login_fragment, container, false)
        return bindingFragment.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        checkLogin()

        login()

        goToSignupFragment()

    }

    private fun initView() {

        btnLogin = bindingFragment.btnLogin
        btnSignup = bindingFragment.btnSignup
        edPhoneNumber = bindingFragment.edPhoneNumber
        edPassword = bindingFragment.edPassword

    }

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var token: String

    private fun checkLogin() {

        sharedPreferences = requireActivity().getSharedPreferences(
            "data",
            Context.MODE_PRIVATE
        )

        token = sharedPreferences.getString("token", "")!!

        Log.d("dataTest", token)

        if (token.isNotEmpty()) {

            val intent = Intent(requireActivity(), ShopActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            requireActivity().startActivity(intent)

        }

    }

    private fun login() {

        btnLogin.setOnClickListener {

            checkInputs()

        }

    }

    private lateinit var phoneNumber: String
    private lateinit var password: String

    private lateinit var loading: Loading
    private lateinit var apiClient: ApiClient

    private fun checkInputs() {

        phoneNumber = edPhoneNumber.text.toString().trim()
        password = edPassword.text.toString().trim()

        if (phoneNumber.length == 11 && password.isNotEmpty()) {

            loginApi()

        } else {

            Toast.makeText(
                requireActivity(),
                "لطفا تمامی ورودی ها را وارد نمایید",
                Toast.LENGTH_SHORT
            ).show()

        }

    }

    private fun loginApi() {

        loading = Loading(requireActivity())

        apiClient = ApiClient()

        val mobileModel = Build.MODEL

        val apiInterface: ApiInterface = ApiClient.retrofit.create(ApiInterface::class.java)

        val callLoading = apiInterface.login(phoneNumber, password, mobileModel)

        callLoading.enqueue(object : Callback<ModelLogin> {

            override fun onResponse(call: Call<ModelLogin>, response: Response<ModelLogin>) {

                loading.hideDialog()

                if (response.code() == 200) {

                    val data = response.body()!!

                    sharedPreferences = requireActivity().getSharedPreferences(
                        "data",
                        Context.MODE_PRIVATE
                    )

                    val edSharedPreferences = sharedPreferences.edit()
                    edSharedPreferences.putString("token", data.token)
                    edSharedPreferences.apply()

                    token = data.token

                    Toast.makeText(requireActivity(), "با موفقیت وارد شدید", Toast.LENGTH_SHORT)
                        .show()

                    val intent = Intent(requireActivity(), ShopActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    requireActivity().startActivity(intent)

                } else {

                    Toast.makeText(
                        requireActivity(),
                        "رمز عبور یا نام کاربری خود را اشتباه وارد کرده اید",
                        Toast.LENGTH_SHORT
                    )
                        .show()

                }

            }

            override fun onFailure(call: Call<ModelLogin>, t: Throwable) {

                loading.hideDialog()

                Toast.makeText(
                    requireActivity(),
                    resources.getText(R.string.error_send_data).toString(),
                    Toast.LENGTH_SHORT
                ).show()

            }

        })

    }

    private fun goToSignupFragment() {

        btnSignup.setOnClickListener {

            Navigation.findNavController(it)
                .navigate(R.id.action_loginFragment_to_signupFragment)

        }

    }


}