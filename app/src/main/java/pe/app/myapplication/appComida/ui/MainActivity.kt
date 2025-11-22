package pe.app.myapplication.appComida.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pe.app.myapplication.appComida.data.network.RetrofitClient
import pe.app.myapplication.appComida.databinding.ActivityMainBinding
import pe.app.myapplication.appComida.ui.adapter.MealAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MealAdapter

    private val categories = listOf("Todos los platos",
        "Beef", "Chicken", "Dessert", "Lamb", "Miscellaneous",
        "Pasta", "Pork", "Seafood", "Side", "Starter",
        "Vegan", "Vegetarian", "Breakfast", "Goat")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupDropdown()
        fetchMeals("Todos los platos")
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
            fetchMeals(selectedCategory)
        }
    }

    private fun fetchMeals(category: String) {
        binding.progressBar.visibility = android.view.View.VISIBLE
        binding.recyclerView.visibility = android.view.View.GONE

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = if (category == "Todos los platos") {
                    RetrofitClient.instance.getMealsByName("")
                } else {
                    RetrofitClient.instance.getMealsByCategory(category)
                }
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val meals = response.body()?.meals ?: emptyList()
                        adapter.updateList(meals)
                    } else {
                        Toast.makeText(this@MainActivity, "Error en la respuesta", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Error de red: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } finally {
                withContext(Dispatchers.Main) {
                    binding.progressBar.visibility = android.view.View.GONE
                    binding.recyclerView.visibility = android.view.View.VISIBLE
                }
            }
        }
    }
}