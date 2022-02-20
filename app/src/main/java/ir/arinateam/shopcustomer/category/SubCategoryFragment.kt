package ir.arinateam.shopcustomer.category

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ir.arinateam.shopadmin.api.ApiClient
import ir.arinateam.shopcustomer.R
import ir.arinateam.shopcustomer.api.ApiInterface
import ir.arinateam.shopcustomer.category.adapter.AdapterRecProducts
import ir.arinateam.shopcustomer.category.model.ModelCategoryDetailBase
import ir.arinateam.shopcustomer.category.model.ModelRecProduct
import ir.arinateam.shopcustomer.databinding.SubCategoryFragmentBinding
import ir.arinateam.shopcustomer.home.adapter.AdapterRecProduct
import ir.arinateam.shopcustomer.home.model.ModelRecHomeProduct
import ir.arinateam.shopcustomer.utils.Loading
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SubCategoryFragment : Fragment() {

    private lateinit var bindingFragment: SubCategoryFragmentBinding

    private lateinit var imgBack: ImageView
    private lateinit var tvCategoryName: TextView
    private lateinit var recNewProduct: RecyclerView
    private lateinit var recProducts: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        bindingFragment =
            DataBindingUtil.inflate(inflater, R.layout.sub_category_fragment, container, false)
        return bindingFragment.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        getProductsApi()

        back()

    }

    private fun initView() {

        imgBack = bindingFragment.imgBack
        tvCategoryName = bindingFragment.tvCategoryName
        recNewProduct = bindingFragment.recNewProduct
        recProducts = bindingFragment.recProducts

    }

    private lateinit var apiClient: ApiClient
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var token: String

    private fun getProductsApi() {

        tvCategoryName.text = requireArguments().getString("categoryName")

        val loadingLottie = Loading(requireActivity())

        apiClient = ApiClient()

        sharedPreferences = requireActivity().getSharedPreferences(
            "data",
            Context.MODE_PRIVATE
        )

        token = sharedPreferences.getString("token", "")!!

        val apiInterface: ApiInterface = ApiClient.retrofit.create(ApiInterface::class.java)

        val callLoading =
            apiInterface.categoryDetail("Bearer $token", requireArguments().getInt("categoryId"))

        callLoading.enqueue(object : Callback<ModelCategoryDetailBase> {

            override fun onResponse(
                call: Call<ModelCategoryDetailBase>,
                response: Response<ModelCategoryDetailBase>
            ) {

                loadingLottie.hideDialog()

                Log.d("dataTest", response.code().toString())
                Log.d("dataTest", response.body().toString())

                if (response.code() == 200) {

                    val data = response.body()!!

                    lsModelNewProduct = ArrayList()

                    data.latestProducts.forEach {


                        lsModelNewProduct.add(
                            ModelRecHomeProduct(
                                it.id,
                                it.name,
                                it.image,
                                it.discountedPrice
                            )
                        )

                    }

                    setRecNewProducts()


                    lsModelOtherProduct = ArrayList()

                    lsModelOtherProduct.addAll(data.otherProducts)

                    setRecOtherProducts()

                } else {

                    Toast.makeText(
                        requireActivity(),
                        resources.getText(R.string.error_receive_data).toString(),
                        Toast.LENGTH_SHORT
                    ).show()

                }

            }

            override fun onFailure(call: Call<ModelCategoryDetailBase>, t: Throwable) {

                loadingLottie.hideDialog()

                Toast.makeText(
                    requireActivity(),
                    resources.getText(R.string.error_send_data).toString(),
                    Toast.LENGTH_SHORT
                ).show()

            }

        })

    }

    private lateinit var adapterRecNewProduct: AdapterRecProduct
    private lateinit var lsModelNewProduct: ArrayList<ModelRecHomeProduct>

    private fun setRecNewProducts() {

        adapterRecNewProduct = AdapterRecProduct(requireActivity(), lsModelNewProduct)

        val linearLayoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        recNewProduct.layoutManager = linearLayoutManager
        recNewProduct.adapter = adapterRecNewProduct

    }


    private lateinit var adapterRecOtherProduct: AdapterRecProducts
    private lateinit var lsModelOtherProduct: ArrayList<ModelRecProduct>

    private fun setRecOtherProducts() {

        adapterRecOtherProduct = AdapterRecProducts(requireActivity(), lsModelOtherProduct)

        val linearLayoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        recProducts.layoutManager = linearLayoutManager
        recProducts.adapter = adapterRecOtherProduct

    }


    private fun back() {

        imgBack.setOnClickListener {

            Navigation.findNavController(it).popBackStack()

        }

    }

}