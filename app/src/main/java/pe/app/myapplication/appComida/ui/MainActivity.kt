package pe.app.myapplication.appComida.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import pe.app.myapplication.appComida.databinding.ActivityMainBinding
import pe.app.myapplication.appComida.ui.adapter.MealAdapter
import pe.app.myapplication.appComida.ui.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MealAdapter
    private val viewModel: MainViewModel by viewModels()

    private val categories = listOf(
        "Todos los platos",
        "Beef", "Chicken", "Dessert", "Lamb", "Miscellaneous",
        "Pasta", "Pork", "Seafood", "Side", "Starter",
        "Vegan", "Vegetarian", "Breakfast", "Goat"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupDropdown()
        setupObservers()
        viewModel.fetchMeals("Todos los platos")
    }

    private fun setupRecyclerView() {
        adapter = MealAdapter(emptyList()) { idMeal ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("ID_MEAL", idMeal)
            startActivity(intent)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun setupDropdown() {
        val adapterDropdown = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, categories)
        binding.autoCompleteTxt.setAdapter(adapterDropdown)
        binding.autoCompleteTxt.setText(categories[0], false)

        binding.autoCompleteTxt.setOnItemClickListener { _, _, position, _ ->
            val selectedCategory = categories[position]
            viewModel.fetchMeals(selectedCategory)
        }
    }

    private fun setupObservers() {
        viewModel.meals.observe(this) { mealList ->
            adapter.updateList(mealList)
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.recyclerView.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        viewModel.error.observe(this) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }
}