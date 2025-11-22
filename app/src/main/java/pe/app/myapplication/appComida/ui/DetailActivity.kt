package pe.app.myapplication.appComida.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import pe.app.myapplication.appComida.databinding.ActivityDetailBinding
import pe.app.myapplication.appComida.ui.viewmodel.DetailViewModel

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
private val viewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val idMeal = intent.getStringExtra("ID_MEAL")
        if (idMeal != null) {
            viewModel.fetchMealDetails(idMeal)
        }

        binding.btnVolver.setOnClickListener {
            finish()
        }

        setupObservers()
    }

    private fun setupObservers() {

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        viewModel.error.observe(this) { errorMessage ->
            if (errorMessage != null) {
                AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage(errorMessage)
                    .setPositiveButton("OK") { _, _ -> }
                    .show()
            }
        }
    }

}