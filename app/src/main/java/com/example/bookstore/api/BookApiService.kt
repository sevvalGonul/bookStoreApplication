package com.example.bookstore.api

import com.example.bookstore.model.AuthorsItem
import com.example.bookstore.model.BookData
import com.example.bookstore.model.Deneme
import com.example.bookstore.model.PostBook
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

private val BASE_URL = "https://702183f3-d16b-483f-93fd-6ebf4b10455e.mock.pstmn.io/"

interface BookApiService {
    @GET("books")
    fun getBooks() : Call<List<BookData>>  // Dönüs tipi degistirilecek ??

    @GET("books/{id}")
    fun getBookDetail(
        @Path("id") id : Int
    ) : Call<String> // Dönüs tipi degistirilecek

    @GET("authors")
    fun getAuthors() : Call<List<AuthorsItem>>

    @POST("addBook")
    fun addNewBook(@Body newBook : PostBook) : Call<Deneme>//Call<BookData?>?

    companion object {
        private var apiService: BookApiService? = null
        fun getInstance() : BookApiService {
            if(apiService == null) {
                val moshi = Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()

                val retrofit = Retrofit.Builder()
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .baseUrl(BASE_URL)
                    .build()

                apiService = retrofit.create(BookApiService::class.java)
            }
            return apiService!!
        }
    }

}