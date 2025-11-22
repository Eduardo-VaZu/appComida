package pe.app.myapplication.appComida.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pe.app.myapplication.appComida.data.network.RetrofitClient
import pe.app.myapplication.appComida.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val idMeal = intent.getStringExtra("ID_MEAL")
        if (idMeal != null) {
            fetchMealDetails(idMeal)
        }

        binding.btnVolver.setOnClickListener {
            finish()
        }
    }

    private fun fetchMealDetails(id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.getMealDetails(id)
                if (response.isSuccessful) {
                    val mealList = response.body()?.meals
                    if (!mealList.isNullOrEmpty()) {
                        val meal = mealList[0]
                        withContext(Dispatchers.Main) {
                            binding.tvIdMeal.text = meal.idMeal
                            binding.tvStrMeal.text = meal.strMeal
                            binding.tvCategory.text = meal.strCategory
                            binding.tvArea.text = meal.strArea
                            binding.tvYoutube.text = meal.strYoutube

                            Glide.with(this@DetailActivity)
                                .load(meal.strMealThumb)
                                .into(binding.imgDetail)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}