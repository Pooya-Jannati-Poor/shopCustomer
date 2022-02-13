package ir.arinateam.shopcustomer.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ir.arinateam.shopadmin.api.ApiClient
import ir.arinateam.shopcustomer.R
import ir.arinateam.shopcustomer.api.ApiInterface
import ir.arinateam.shopcustomer.databinding.HomeFragmentBinding
import ir.arinateam.shopcustomer.home.adapter.AdapterRecBannerTopHome
import ir.arinateam.shopcustomer.home.adapter.AdapterRecCategory
import ir.arinateam.shopcustomer.home.adapter.AdapterRecProduct
import ir.arinateam.shopcustomer.home.model.ModelCategory
import ir.arinateam.shopcustomer.home.model.ModelGetCustomerData
import ir.arinateam.shopcustomer.home.model.ModelRecBannerTopHome
import ir.arinateam.shopcustomer.home.model.ModelRecHomeProduct
import ir.arinateam.shopcustomer.utils.Loading
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var bindingFragment: HomeFragmentBinding

    private lateinit var recBannerTopHome: RecyclerView
    private lateinit var recCategory: RecyclerView
    private lateinit var recNewProduct: RecyclerView
    private lateinit var recPopularProduct: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        bindingFragment =
            DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false)
        return bindingFragment.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        getHomeInfoApi()

    }

    private fun initView() {

        recBannerTopHome = bindingFragment.recBannerTopHome
        recCategory = bindingFragment.recCategory
        recNewProduct = bindingFragment.recNewProduct
        recPopularProduct = bindingFragment.recPopularProduct

    }

    private lateinit var apiClient: ApiClient
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var token: String

    private fun getHomeInfoApi() {

        val loadingLottie = Loading(requireActivity())

        apiClient = ApiClient()

        sharedPreferences = requireActivity().getSharedPreferences(
            "data",
            Context.MODE_PRIVATE
        )

        token = sharedPreferences.getString("token", "")!!

        val apiInterface: ApiInterface = ApiClient.retrofit.create(ApiInterface::class.java)

        val callLoading = apiInterface.dashboard("Bearer $token")

        callLoading.enqueue(object : Callback<ModelGetCustomerData> {

            override fun onResponse(
                call: Call<ModelGetCustomerData>,
                response: Response<ModelGetCustomerData>
            ) {

                loadingLottie.hideDialog()

                Log.d("dataTest", response.code().toString())
                Log.d("dataTest", response.body().toString())

                if (response.code() == 200) {

                    val data = response.body()!!

                    lsModelRecBannerTopHome = ArrayList()

                    lsModelRecBannerTopHome.addAll(data.sliders)

                    setRecBannerTopHome()


                    lsModelCategory = ArrayList()

                    lsModelCategory.addAll(data.categories)

                    setRecCategory()


                    lsModelNewProduct = ArrayList()

                    lsModelNewProduct.addAll(data.latestProducts)

                    setRecNewProduct()


                    lsModelPopularProduct = ArrayList()

                    lsModelPopularProduct.addAll(data.popularProducts)

                    setRecPopularProduct()

                } else {

                    Toast.makeText(
                        requireActivity(),
                        resources.getText(R.string.error_receive_data).toString(),
                        Toast.LENGTH_SHORT
                    ).show()

                }

            }

            override fun onFailure(call: Call<ModelGetCustomerData>, t: Throwable) {

                loadingLottie.hideDialog()

                Toast.makeText(
                    requireActivity(),
                    resources.getText(R.string.error_send_data).toString(),
                    Toast.LENGTH_SHORT
                ).show()

            }

        })

    }

    private lateinit var adapterRecBannerTopHome: AdapterRecBannerTopHome
    private lateinit var lsModelRecBannerTopHome: ArrayList<ModelRecBannerTopHome>

    private fun setRecBannerTopHome() {

        adapterRecBannerTopHome =
            AdapterRecBannerTopHome(requireActivity(), lsModelRecBannerTopHome)

        val linearLayoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, true)
        recBannerTopHome.layoutManager = linearLayoutManager
        recBannerTopHome.adapter = adapterRecBannerTopHome

    }


    private lateinit var adapterRecCategory: AdapterRecCategory
    private lateinit var lsModelCategory: ArrayList<ModelCategory>

    private fun setRecCategory() {

        adapterRecCategory = AdapterRecCategory(requireActivity(), lsModelCategory)

        val linearLayoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, true)
        recCategory.layoutManager = linearLayoutManager
        recCategory.adapter = adapterRecCategory

    }


    private lateinit var adapterRecNewProduct: AdapterRecProduct
    private lateinit var lsModelNewProduct: ArrayList<ModelRecHomeProduct>

    private fun setRecNewProduct() {

        adapterRecNewProduct = AdapterRecProduct(requireActivity(), lsModelNewProduct)

        val linearLayoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, true)
        recNewProduct.layoutManager = linearLayoutManager
        recNewProduct.adapter = adapterRecNewProduct

    }


    private lateinit var adapterRecPopularProduct: AdapterRecProduct
    private lateinit var lsModelPopularProduct: ArrayList<ModelRecHomeProduct>

    private fun setRecPopularProduct() {

        adapterRecPopularProduct = AdapterRecProduct(requireActivity(), lsModelPopularProduct)

        val linearLayoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, true)
        recPopularProduct.layoutManager = linearLayoutManager
        recPopularProduct.adapter = adapterRecPopularProduct

    }


}