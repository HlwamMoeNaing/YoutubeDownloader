package com.hmn.movies.network

import com.hmn.movies.network.model.Pojo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers


interface Api {
    @Headers("secret-key: $2b$10\$SyoXTJ.WeHKW9v2JFyXwSu9x1FOglJJaNq8qB6ADcz4oRq1KNbQ2C")
    @GET("5f44a330993a2e110d35d5c0")
    fun getModel(): Call<Pojo>


}
