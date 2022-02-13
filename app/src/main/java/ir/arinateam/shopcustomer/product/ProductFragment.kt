package ir.arinateam.shopcustomer.product

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.imageview.ShapeableImageView
import ir.arinateam.shopadmin.api.ApiClient
import ir.arinateam.shopcustomer.R
import ir.arinateam.shopcustomer.api.ApiInterface
import ir.arinateam.shopcustomer.databinding.ProductFragmentBinding
import ir.arinateam.shopcustomer.home.adapter.AdapterRecProduct
import ir.arinateam.shopcustomer.home.model.ModelRecHomeProduct
import ir.arinateam.shopcustomer.product.adapter.AdapterRecComment
import ir.arinateam.shopcustomer.product.adapter.AdapterRecProductInfo
import ir.arinateam.shopcustomer.product.model.ModelRecComment
import ir.arinateam.shopcustomer.product.model.ModelRecProductInfo
import ir.arinateam.shopcustomer.product.model.ModelRecProductInfoBase
import ir.arinateam.shopcustomer.utils.Loading
import ir.arinateam.shopcustomer.utils.NumbersSeparator
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductFragment : Fragment() {

    private lateinit var bindingFragment: ProductFragmentBinding


    private lateinit var imgBack: ImageView
    private lateinit var imgProduct: ShapeableImageView
    private lateinit var tvProductName: TextView
    private lateinit var tvWriter: TextView
    private lateinit var rateProduct: RatingBar
    private lateinit var imgFavorite: ImageView
    private lateinit var tvProductDescription: TextView
    private lateinit var recProductInfo: RecyclerView
    private lateinit var recProductComment: RecyclerView
    private lateinit var btnAddComment: Button
    private lateinit var recSameWriter: RecyclerView
    private lateinit var btnAddToBasket: Button
    private lateinit var tvBasePrice: TextView
    private lateinit var tvDiscountedPrice: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        bindingFragment =
            DataBindingUtil.inflate(inflater, R.layout.product_fragment, container, false)
        return bindingFragment.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        getProductDetailApi()

        back()

    }

    private fun initView() {

        imgBack = bindingFragment.imgBack
        imgProduct = bindingFragment.imgProduct
        tvProductName = bindingFragment.tvProductName
        tvWriter = bindingFragment.tvWriter
        rateProduct = bindingFragment.rateProduct
        imgFavorite = bindingFragment.imgFavorite
        tvProductDescription = bindingFragment.tvProductDescription
        recProductInfo = bindingFragment.recProductInfo
        recProductComment = bindingFragment.recProductComment
        btnAddComment = bindingFragment.btnAddComment
        recSameWriter = bindingFragment.recSameWriter
        btnAddToBasket = bindingFragment.btnAddToBasket
        tvBasePrice = bindingFragment.tvBasePrice
        tvDiscountedPrice = bindingFragment.tvDiscountedPrice

    }

    private lateinit var apiClient: ApiClient
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var token: String

    private fun getProductDetailApi() {

        val loadingLottie = Loading(requireActivity())

        apiClient = ApiClient()

        sharedPreferences = requireActivity().getSharedPreferences(
            "data",
            Context.MODE_PRIVATE
        )

        token = sharedPreferences.getString("token", "")!!

        val apiInterface: ApiInterface = ApiClient.retrofit.create(ApiInterface::class.java)

        val callLoading =
            apiInterface.productInfo("Bearer $token", requireArguments().getInt("productId"))

        callLoading.enqueue(object : Callback<ModelRecProductInfoBase> {

            override fun onResponse(
                call: Call<ModelRecProductInfoBase>,
                response: Response<ModelRecProductInfoBase>
            ) {

                loadingLottie.hideDialog()

                Log.d("dataTest", response.code().toString())
                Log.d("dataTest", response.body().toString())

                if (response.code() == 200) {

                    val data = response.body()!!

                    changeFavorite()

                    Glide.with(requireActivity())
                        .load("http://applicationfortests.ir/" + data.product.image)
                        .diskCacheStrategy(
                            DiskCacheStrategy.ALL
                        )
                        .fitCenter().into(imgProduct)

                    tvProductName.text = data.product.name
                    tvWriter.text = data.product.writer
                    tvProductDescription.text = data.product.description
                    rateProduct.rating = data.product.score.toFloat()

                    val numbersSeparator = NumbersSeparator()

                    tvDiscountedPrice.text =
                        numbersSeparator.doubleToStringNoDecimal(data.product.discountedPrice.toDouble()) + " تومان"

                    if (data.product.hasDiscount) {

                        tvBasePrice.visibility = View.VISIBLE
                        tvBasePrice.text =
                            numbersSeparator.doubleToStringNoDecimal(data.product.price.toDouble()) + " تومان"
                        tvBasePrice.paintFlags =
                            tvBasePrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                    }

                    lsModelRecProductInfo = ArrayList()

                    lsModelRecProductInfo.add(ModelRecProductInfo("شابک", data.product.isbn))
                    lsModelRecProductInfo.add(
                        ModelRecProductInfo(
                            "انتشارات",
                            data.product.publisher
                        )
                    )
                    lsModelRecProductInfo.add(
                        ModelRecProductInfo(
                            "سال نشر",
                            data.product.publish_year.toString()
                        )
                    )
                    lsModelRecProductInfo.add(
                        ModelRecProductInfo(
                            "تعداد صفحات",
                            data.product.pages.toString()
                        )
                    )

                    setRecProductInfo()


                    lsModelRecComment = ArrayList()

                    data.product.comments.forEach {

                        lsModelRecComment.add(it.comment)

                    }

                    setRecComment()


                    lsModelSameWriter = ArrayList()

                    lsModelSameWriter.addAll(data.sameWriterProducts)

                    setRecSameWriter()


                } else {

                    Toast.makeText(
                        requireActivity(),
                        resources.getText(R.string.error_receive_data).toString(),
                        Toast.LENGTH_SHORT
                    ).show()

                }

            }

            override fun onFailure(call: Call<ModelRecProductInfoBase>, t: Throwable) {

                loadingLottie.hideDialog()

                Toast.makeText(
                    requireActivity(),
                    resources.getText(R.string.error_send_data).toString(),
                    Toast.LENGTH_SHORT
                ).show()

            }

        })

    }


    private lateinit var adapterRecProductInfo: AdapterRecProductInfo
    private lateinit var lsModelRecProductInfo: ArrayList<ModelRecProductInfo>

    private fun setRecProductInfo() {

        adapterRecProductInfo = AdapterRecProductInfo(requireActivity(), lsModelRecProductInfo)

        val linearLayoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, true)
        recProductInfo.layoutManager = linearLayoutManager
        recProductInfo.adapter = adapterRecProductInfo

    }


    private lateinit var adapterRecComment: AdapterRecComment
    private lateinit var lsModelRecComment: ArrayList<ModelRecComment>

    private fun setRecComment() {

        adapterRecComment = AdapterRecComment(requireActivity(), lsModelRecComment)

        val linearLayoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, true)
        recProductComment.layoutManager = linearLayoutManager
        recProductComment.adapter = adapterRecComment

    }


    private lateinit var adapterRecSameWriter: AdapterRecProduct
    private lateinit var lsModelSameWriter: ArrayList<ModelRecHomeProduct>

    private fun setRecSameWriter() {

        adapterRecSameWriter = AdapterRecProduct(requireActivity(), lsModelSameWriter)

        val linearLayoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, true)
        recSameWriter.layoutManager = linearLayoutManager
        recSameWriter.adapter = adapterRecSameWriter

    }


    private fun changeFavorite() {

        imgFavorite.setOnClickListener {

            changeFavoriteStateApi()

        }

    }


    private fun changeFavoriteStateApi() {

        val loadingLottie = Loading(requireActivity())

        apiClient = ApiClient()

        sharedPreferences = requireActivity().getSharedPreferences(
            "data",
            Context.MODE_PRIVATE
        )

        token = sharedPreferences.getString("token", "")!!

        val apiInterface: ApiInterface = ApiClient.retrofit.create(ApiInterface::class.java)

        val callLoading =
            apiInterface.changeFavoriteState(
                "Bearer $token",
                requireArguments().getInt("productId")
            )

        callLoading.enqueue(object : Callback<ResponseBody> {

            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {

                loadingLottie.hideDialog()

                Log.d("dataTest", response.code().toString())
                Log.d("dataTest", response.body().toString())

                if (response.code() == 200) {

                    Toast.makeText(
                        requireActivity(),
                        "با موفقیت انجام شد",
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

    }

    private fun back() {

        imgBack.setOnClickListener {

            Navigation.findNavController(it).popBackStack()

        }

    }

}