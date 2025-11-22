package pe.app.myapplication.appComida.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import pe.app.myapplication.appComida.databinding.ActivityDetailBinding
import pe.app.myapplication.appComida.ui.viewmodel.DetailViewModel

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
        setupListeners()

        val idMeal = intent.getStringExtra("ID_MEAL")
        if (idMeal != null) {
            viewModel.fetchMealDetails(idMeal)
        }else{
            Toast.makeText(this, "Error: ID no encontrado", Toast.LENGTH_SHORT).show()
            finish()
        }


    }

    private fun setupListeners() {
        binding.btnVolver.setOnClickListener {
            finish()
        }
    }
    private fun setupObservers() {
        viewModel.meal.observe(this) { mealDetail ->
            mealDetail?.let {
                binding.tvIdMeal.text = it.idMeal
                binding.tvStrMeal.text = it.strMeal
                binding.tvCategory.text = it.strCategory ?: "Sin categoría"
                binding.tvArea.text = it.strArea ?: "Desconocido"
                binding.tvYoutube.text = it.strYoutube ?: "No disponible"

                Glide.with(this)
                    .load(it.strMealThumb)
                    .into(binding.imgDetail)
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(this) { msg ->
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
        }
    }

}