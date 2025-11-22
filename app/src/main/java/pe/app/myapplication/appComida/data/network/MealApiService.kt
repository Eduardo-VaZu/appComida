package pe.app.myapplication.appComida.data.network

import pe.app.myapplication.appComida.data.model.MealResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApiService {
    @GET("filter.php")
    suspend fun getMealsByCategory(@Query("c") category: String): Response<MealResponse>

    @GET("lookup.php")
    suspend fun getMealDetails(@Query("i") id: String): Response<MealResponse>
}