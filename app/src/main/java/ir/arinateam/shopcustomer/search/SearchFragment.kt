package ir.arinateam.shopcustomer.search

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ir.arinateam.shopadmin.api.ApiClient
import ir.arinateam.shopcustomer.R
import ir.arinateam.shopcustomer.api.ApiInterface
import ir.arinateam.shopcustomer.category.adapter.AdapterRecProducts
import ir.arinateam.shopcustomer.category.model.ModelRecProduct
import ir.arinateam.shopcustomer.databinding.SearchFragmentBinding
import ir.arinateam.shopcustomer.search.model.ModelSearchProductBase
import ir.arinateam.shopcustomer.utils.Loading
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment() {

    private lateinit var bindingFragment: SearchFragmentBinding

    private lateinit var edBookName: EditText
    private lateinit var recProducts: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        bindingFragment =
            DataBindingUtil.inflate(inflater, R.layout.search_fragment, container, false)
        return bindingFragment.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        search()

    }

    private fun initView() {

        edBookName = bindingFragment.edBookName
        recProducts = bindingFragment.recProducts

    }

    private fun search() {

        edBookName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

                if (edBookName.text.toString() != "") {

                    getSearchProductApi(edBookName.text.toString())

                }

            }

        })

    }

    private lateinit var apiClient: ApiClient
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var token: String

    private fun getSearchProductApi(bookName: String) {

        val loadingLottie = Loading(requireActivity())

        apiClient = ApiClient()

        sharedPreferences = requireActivity().getSharedPreferences(
            "data",
            Context.MODE_PRIVATE
        )

        token = sharedPreferences.getString("token", "")!!

        val apiInterface: ApiInterface = ApiClient.retrofit.create(ApiInterface::class.java)

        val callLoading = apiInterface.searchProduct("Bearer $token", bookName)

        callLoading.enqueue(object : Callback<ModelSearchProductBase> {

            override fun onResponse(
                call: Call<ModelSearchProductBase>,
                response: Response<ModelSearchProductBase>
            ) {

                loadingLottie.hideDialog()

                Log.d("dataTest", response.code().toString())
                Log.d("dataTest", response.body().toString())

                if (response.code() == 200) {

                    val data = response.body()!!

                    lsModelProduct = ArrayList()

                    lsModelProduct.addAll(data.data)

                    setRecProducts()

                } else {

                    Toast.makeText(
                        requireActivity(),
                        resources.getText(R.string.error_receive_data).toString(),
                        Toast.LENGTH_SHORT
                    ).show()

                }

            }

            override fun onFailure(call: Call<ModelSearchProductBase>, t: Throwable) {

                loadingLottie.hideDialog()

                Toast.makeText(
                    requireActivity(),
                    resources.getText(R.string.error_send_data).toString(),
                    Toast.LENGTH_SHORT
                ).show()

            }

        })

    }

    private lateinit var adapterRecProduct: AdapterRecProducts
    private lateinit var lsModelProduct: ArrayList<ModelRecProduct>

    private fun setRecProducts() {

        adapterRecProduct = AdapterRecProducts(requireActivity(), lsModelProduct)

        val linearLayoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        recProducts.layoutManager = linearLayoutManager
        recProducts.adapter = adapterRecProduct

    }

}