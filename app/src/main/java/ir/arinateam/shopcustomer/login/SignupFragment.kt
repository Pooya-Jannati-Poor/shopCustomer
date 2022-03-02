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
import ir.arinateam.shopcustomer.api.ApiInterface
import ir.arinateam.shopcustomer.login.model.ModelSignup
import ir.arinateam.shopcustomer.R
import ir.arinateam.shopcustomer.ShopActivity
import ir.arinateam.shopcustomer.databinding.SignupFragmentBinding
import ir.arinateam.shopcustomer.utils.Loading
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignupFragment : Fragment() {

    private lateinit var bindingFragment: SignupFragmentBinding

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
            DataBindingUtil.inflate(inflater, R.layout.signup_fragment, container, false)
        return bindingFragment.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        signupCustomer()

        backToLoginFragment()

    }

    private fun initView() {

        btnLogin = bindingFragment.btnLogin
        btnSignup = bindingFragment.btnSignup
        edPhoneNumber = bindingFragment.edPhoneNumber
        edPassword = bindingFragment.edPassword

    }


    private fun signupCustomer() {

        btnSignup.setOnClickListener {

            checkInputsShop()

        }

    }

    private lateinit var phoneNumber: String
    private lateinit var password: String

    private fun checkInputsShop() {

        phoneNumber = edPhoneNumber.text.toString().trim()
        password = edPassword.text.toString().trim()

        if (phoneNumber.length == 11 && password.length >= 8) {

            signupCustomerApi()

        } else {

            Toast.makeText(
                requireActivity(),
                "لطفا تمامی ورودی ها را وارد نمایید",
                Toast.LENGTH_SHORT
            ).show()

        }

    }

    private lateinit var loading: Loading
    private lateinit var apiClient: ApiClient
    private lateinit var sharedPreferences: SharedPreferences

    private fun signupCustomerApi() {

        loading = Loading(requireActivity())

        apiClient = ApiClient()

        val mobileModel = Build.MODEL

        val apiInterface: ApiInterface = ApiClient.retrofit.create(ApiInterface::class.java)
        val signupApi = apiInterface.signup(phoneNumber, password, "USER", mobileModel)

        signupApi.enqueue(object : Callback<ModelSignup> {
            override fun onResponse(
                call: Call<ModelSignup>,
                response: Response<ModelSignup>
            ) {
                loading.hideDialog()

                Log.d("dataTest", response.code().toString())
                Toast.makeText(requireActivity(), response.code().toString(), Toast.LENGTH_LONG).show()

                if (response.code() == 200) {

                    val data = response.body()

                    if (data != null) {

                        sharedPreferences = requireActivity().getSharedPreferences(
                            "data",
                            Context.MODE_PRIVATE
                        )

                        val edSharedPreferences = sharedPreferences.edit()
                        edSharedPreferences.putString("token", data.token)
                        edSharedPreferences.apply()

                        val intent = Intent(requireActivity(), ShopActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        requireActivity().startActivity(intent)

                        Toast.makeText(
                            requireActivity(),
                            "با موفقیت ثبت نام کردید",
                            Toast.LENGTH_SHORT
                        )
                            .show()

                    }

                } else {

                    Toast.makeText(requireActivity(), "لطفا مجددا سعی نمایید", Toast.LENGTH_SHORT)
                        .show()

                }

            }

            override fun onFailure(
                call: Call<ModelSignup>,
                t: Throwable
            ) {
                loading.hideDialog()

                Log.d("dataTest", t.message.toString())

                Toast.makeText(requireActivity(), "لطفا مجددا سعی نمایید", Toast.LENGTH_SHORT)
                    .show()

            }

        })

    }

    private fun backToLoginFragment() {

        btnLogin.setOnClickListener {

            Navigation.findNavController(it).popBackStack()

        }
    }


}