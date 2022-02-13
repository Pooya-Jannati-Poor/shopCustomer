package ir.arinateam.shopcustomer.api

import ir.arinateam.shopcustomer.category.model.ModelCategoryDetailBase
import ir.arinateam.shopcustomer.category.model.ModelSpCategoryBase
import ir.arinateam.shopcustomer.home.model.ModelGetCustomerData
import ir.arinateam.shopcustomer.login.model.ModelLogin
import ir.arinateam.shopcustomer.login.model.ModelSignup
import ir.arinateam.shopcustomer.product.model.ModelRecProductInfoBase
import ir.arinateam.shopcustomer.profile.model.ModelRecFavoriteListBase
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    /*@FormUrlEncoded
    @POST("initial/data")
    fun loadingWithUUID(
        @Field("modelName") modelName: String,
        @Field("uuid") uuid: String,
    ): Call<ModelLoading>


    @GET("coffeeShops")
    fun cafeList(
        @Header("Authorization") token: String
    ): Call<ModelCafeList>*/

    @Headers("Accept: application/json")
    @POST("users/login")
    fun login(
        @Query("phone") phoneNumber: String,
        @Query("password") password: String,
        @Query("model") model: String
    ): Call<ModelLogin>


    @Headers("Accept: application/json")
    @POST("users/register")
    fun signup(
        @Query("phone") phone: String,
        @Query("password") password: String,
        @Query("type") type: String,
        @Query("model") model: String
    ): Call<ModelSignup>

    @Headers("Accept: application/json")
    @GET("dashboard")
    fun dashboard(
        @Header("Authorization") token: String
    ): Call<ModelGetCustomerData>


    @Headers("Accept: application/json")
    @GET("products/{id}")
    fun productInfo(
        @Header("Authorization") token: String,
        @Path("id") productId: Int,
        @Query("wantShopDetail") wantShopDetail: Boolean = true,
        @Query("wantCategoryDetail") wantCategoryDetail: Boolean = true,
        @Query("wantUserDetail") wantUserDetail: Boolean = true,
        @Query("wantComments") wantComments: Boolean = true,
        @Query("wantCommentUser") wantCommentUser: Boolean = true,
        @Query("sameWriterProducts") sameWriterProducts: Boolean = true
    ): Call<ModelRecProductInfoBase>


    @Headers("Accept: application/json")
    @POST("categories")
    fun changeFavoriteState(
        @Header("Authorization") token: String,
        @Query("productId") id: Int
    ): Call<ResponseBody>


    @Headers("Accept: application/json")
    @GET("categories")
    fun categoryList(
        @Header("Authorization") token: String
    ): Call<ModelSpCategoryBase>


    @Headers("Accept: application/json")
    @GET("categories/{id}")
    fun categoryDetail(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Query("latestProducts") latestProducts: Boolean = true,
        @Query("otherProducts") otherProducts: Boolean = true
    ): Call<ModelCategoryDetailBase>


    @Headers("Accept: application/json")
    @GET("favorites")
    fun favoriteList(
        @Header("Authorization") token: String
    ): Call<ModelRecFavoriteListBase>

}