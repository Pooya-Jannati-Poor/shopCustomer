package ir.arinateam.shopcustomer.profile

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ir.arinateam.shopadmin.api.ApiClient
import ir.arinateam.shopcustomer.R
import ir.arinateam.shopcustomer.api.ApiInterface
import ir.arinateam.shopcustomer.category.adapter.AdapterRecProducts
import ir.arinateam.shopcustomer.category.model.ModelRecProduct
import ir.arinateam.shopcustomer.category.model.ModelSpCategoryBase
import ir.arinateam.shopcustomer.databinding.FavoriteFragmentBinding
import ir.arinateam.shopcustomer.profile.adapter.AdapterRecFavoriteList
import ir.arinateam.shopcustomer.profile.model.ModelRecFavoriteList
import ir.arinateam.shopcustomer.profile.model.ModelRecFavoriteListBase
import ir.arinateam.shopcustomer.utils.Loading
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FavoriteFragment : Fragment() {

    private lateinit var bindingFragment: FavoriteFragmentBinding


    private lateinit var imgBack: ImageView
    private lateinit var recProducts: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        bindingFragment = DataBindingUtil.inflate(
            inflater,
            R.layout.favorite_fragment, container, false
        )
        return bindingFragment.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        getFavoriteListApi()
    }

    private fun initView() {

        imgBack = bindingFragment.imgBack
        recProducts = bindingFragment.recProducts

    }

    private lateinit var apiClient: ApiClient
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var token: String

    private fun getFavoriteListApi() {

        val loadingLottie = Loading(requireActivity())

        apiClient = ApiClient()

        sharedPreferences = requireActivity().getSharedPreferences(
            "data",
            Context.MODE_PRIVATE
        )

        token = sharedPreferences.getString("token", "")!!

        val apiInterface: ApiInterface = ApiClient.retrofit.create(ApiInterface::class.java)

        val callFavoriteList = apiInterface.favoriteList("Bearer $token")

        callFavoriteList.enqueue(object : Callback<ModelRecFavoriteListBase> {

            override fun onResponse(
                call: Call<ModelRecFavoriteListBase>,
                response: Response<ModelRecFavoriteListBase>
            ) {

                loadingLottie.hideDialog()

                Log.d("dataTest", response.code().toString())
                Log.d("dataTest", response.body().toString())

                if (response.code() == 200) {

                    val data = response.body()!!

                    lsModelProduct = ArrayList()
                    lsModelFavoriteList = ArrayList()

                    lsModelFavoriteList.addAll(data.data)

                    data.data.forEach {

                        lsModelProduct.add(it.product)

                    }

                    setRecProducts()

                } else {

                    Toast.makeText(
                        requireActivity(),
                        resources.getText(R.string.error_receive_data).toString(),
                        Toast.LENGTH_SHORT
                    ).show()

                }

            }

            override fun onFailure(call: Call<ModelRecFavoriteListBase>, t: Throwable) {

                loadingLottie.hideDialog()

                Toast.makeText(
                    requireActivity(),
                    resources.getText(R.string.error_send_data).toString(),
                    Toast.LENGTH_SHORT
                ).show()

            }

        })

    }

    private lateinit var adapterRecProducts: AdapterRecFavoriteList
    private lateinit var lsModelProduct: ArrayList<ModelRecProduct>
    private lateinit var lsModelFavoriteList: ArrayList<ModelRecFavoriteList>

    private fun setRecProducts() {

        adapterRecProducts =
            AdapterRecFavoriteList(requireActivity(), lsModelProduct, lsModelFavoriteList)

        val linearLayoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        recProducts.layoutManager = linearLayoutManager
        recProducts.adapter = adapterRecProducts

    }

}