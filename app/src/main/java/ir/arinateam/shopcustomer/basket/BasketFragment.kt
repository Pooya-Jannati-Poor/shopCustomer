package ir.arinateam.shopcustomer.basket

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ir.arinateam.shopadmin.api.ApiClient
import ir.arinateam.shopcustomer.MainActivity
import ir.arinateam.shopcustomer.R
import ir.arinateam.shopcustomer.api.ApiInterface
import ir.arinateam.shopcustomer.basket.adapter.AdapterRecBasket
import ir.arinateam.shopcustomer.basket.model.ModelRecBasket
import ir.arinateam.shopcustomer.databinding.BasketFragmentBinding
import ir.arinateam.shopcustomer.search.model.ModelSearchProductBase
import ir.arinateam.shopcustomer.utils.BasketItemChange
import ir.arinateam.shopcustomer.utils.Loading
import ir.arinateam.shopcustomer.utils.NumbersSeparator
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BasketFragment : Fragment(), BasketItemChange {

    private lateinit var bindingFragment: BasketFragmentBinding

    private lateinit var imgBack: ImageView
    private lateinit var recProducts: RecyclerView
    private lateinit var tvBasePriceAll: TextView
    private lateinit var tvDiscountedPriceAll: TextView
    private lateinit var tvFinalPrice: TextView
    private lateinit var btnCreateOrder: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        bindingFragment =
            DataBindingUtil.inflate(inflater, R.layout.basket_fragment, container, false)
        return bindingFragment.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        sharedPreferences = requireActivity().getSharedPreferences(
            "data",
            Context.MODE_PRIVATE
        )

        token = sharedPreferences.getString("token", "")!!

        productListApi()

        btnCreateOrder.setOnClickListener {

            createOrderApi()

        }

        back()
    }

    private fun initView() {

        imgBack = bindingFragment.imgBack
        recProducts = bindingFragment.recProducts
        tvBasePriceAll = bindingFragment.tvBasePriceAll
        tvDiscountedPriceAll = bindingFragment.tvDiscountedPriceAll
        tvFinalPrice = bindingFragment.tvFinalPrice
        btnCreateOrder = bindingFragment.btnCreateOrder

    }

    private lateinit var apiClient: ApiClient
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var token: String

    private fun productListApi() {

        makeBasketIds()

        val loadingLottie = Loading(requireActivity())

        apiClient = ApiClient()

        val apiInterface: ApiInterface = ApiClient.retrofit.create(ApiInterface::class.java)

        val callProductListApi = apiInterface.productList("Bearer $token", lsBasketIds)

        callProductListApi.enqueue(object : Callback<ModelSearchProductBase> {

            override fun onResponse(
                call: Call<ModelSearchProductBase>,
                response: Response<ModelSearchProductBase>
            ) {

                loadingLottie.hideDialog()

                if (response.code() == 200) {

                    val data = response.body()!!

                    lsModelRecBasket = ArrayList()

                    var basePriceAll = 0
                    val discountedPriceAll: Int
                    var finalPriceAll = 0

                    data.data.forEach { product ->

                        lsBasketIds.forEach { id ->

                            if (id == product.id) {

                                val tempAmount = findAmountOfProduct(product.id)

                                if (tempAmount != 0) {

                                    lsModelRecBasket.add(
                                        ModelRecBasket(
                                            product.id,
                                            product.image,
                                            product.name,
                                            product.discountedPrice,
                                            tempAmount
                                        )
                                    )

                                }

                                basePriceAll += product.basePrice * tempAmount

                                finalPriceAll += product.discountedPrice * tempAmount

                            }

                        }

                    }

                    discountedPriceAll = basePriceAll - finalPriceAll

                    setBasketDetail(basePriceAll, discountedPriceAll, finalPriceAll)

                    setRecBasket()

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

    private fun setBasketDetail(basePriceAll: Int, discountedPriceAll: Int, finalPriceAll: Int) {

        val numbersSeparator = NumbersSeparator()

        tvBasePriceAll.text = numbersSeparator.doubleToStringNoDecimal(basePriceAll.toDouble())
        tvDiscountedPriceAll.text =
            numbersSeparator.doubleToStringNoDecimal(discountedPriceAll.toDouble())
        tvFinalPrice.text = numbersSeparator.doubleToStringNoDecimal(finalPriceAll.toDouble())

    }

    private fun findAmountOfProduct(productId: Int): Int {

        for (i in 0 until MainActivity.jsonArrayBasket.length()) {

            val item = MainActivity.jsonArrayBasket.getJSONObject(i)

            if (productId == item.getInt("productId")) {

                return item.getInt("amount")

            }

        }

        return 0

    }

    private var lsBasketIds = ArrayList<Int>()

    private fun makeBasketIds() {

        lsBasketIds = ArrayList()

        for (i in 0 until MainActivity.jsonArrayBasket.length()) {

            val item = MainActivity.jsonArrayBasket.getJSONObject(i)

            lsBasketIds.add(item.getInt("productId"))

        }

    }

    private lateinit var adapterRecBasket: AdapterRecBasket
    private lateinit var lsModelRecBasket: ArrayList<ModelRecBasket>

    private fun setRecBasket() {

        adapterRecBasket = AdapterRecBasket(requireActivity(), lsModelRecBasket, this)

        val linearLayoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        recProducts.layoutManager = linearLayoutManager
        recProducts.adapter = adapterRecBasket

    }

    override fun onRemoved(id: Int) {

        removeItemFromBasket(id)

    }

    override fun onChanged() {

        productListApi()

    }

    private fun removeItemFromBasket(id: Int) {

        for (i in 0 until MainActivity.jsonArrayBasket.length()) {

            val item = MainActivity.jsonArrayBasket.getJSONObject(i)

            if (id == item.getInt("productId")) {

                MainActivity.jsonArrayBasket.remove(i)

                MainActivity.jsonObjectBasket = JSONObject()

                MainActivity.jsonObjectBasket.put("orders", MainActivity.jsonArrayBasket)

            }

        }

        productListApi()

    }

    private fun createOrderApi() {

        if (lsModelRecBasket.isNotEmpty()) {

            val loadingLottie = Loading(requireActivity())

            apiClient = ApiClient()

            val request = MainActivity.jsonObjectBasket.toString()
                .toRequestBody("application/json".toMediaTypeOrNull())

            val apiInterface: ApiInterface = ApiClient.retrofit.create(ApiInterface::class.java)

            val callMakeOrderApi = apiInterface.makeOrder("Bearer $token", request, "DELIVERED")

            callMakeOrderApi.enqueue(object : Callback<ResponseBody> {

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {

                    loadingLottie.hideDialog()

                    if (response.code() == 204) {

                        MainActivity.jsonObjectBasket = JSONObject()
                        MainActivity.jsonArrayBasket = JSONArray()

                        lsModelRecBasket = ArrayList()

                        setRecBasket()

                        setBasketDetail(0, 0, 0)

                        Toast.makeText(
                            requireActivity(),
                            "پرداخت با موفقیت انجام شد",
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {

                        Toast.makeText(
                            requireActivity(),
                            resources.getText(R.string.error_receive_data).toString(),
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                    loadingLottie.hideDialog()

                    Toast.makeText(
                        requireActivity(),
                        resources.getText(R.string.error_send_data).toString(),
                        Toast.LENGTH_SHORT
                    ).show()

                }

            })

        } else {

            Toast.makeText(
                requireActivity(),
                "سبد خرید شما خالی می باشد",
                Toast.LENGTH_SHORT
            ).show()

        }

    }

    private fun back() {

        imgBack.setOnClickListener {

            Navigation.findNavController(it).popBackStack()

        }

    }

}