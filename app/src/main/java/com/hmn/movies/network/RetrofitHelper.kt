package com.hmn.movies.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitHelper {
    companion object{
        inline fun <reified T>getRetrofit():T{
            val gson = GsonBuilder().setLenient().create()
            val okCli = OkHttpClient.Builder()

            val inteceptor = HttpLoggingInterceptor()
            inteceptor.level = HttpLoggingInterceptor.Level.BODY
            okCli.interceptors().add(inteceptor)
            val okclient =okCli.addInterceptor {
                val builder = it.request().newBuilder()
                builder.header("secret-key", "\$2b\$10\$SyoXTJ.WeHKW9v2JFyXwSu9x1FOglJJaNq8qB6ADcz4oRq1KNbQ2C")
                return@addInterceptor it.proceed(builder.build())
            }.build()

            return Retrofit.Builder().client(okclient)
                .baseUrl("https://api.jsonbin.io/b/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(T::class.java)
        }
    }
}