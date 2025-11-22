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
import pe.app.myapplication.appComida.ui.DetailActivity
import pe.app.myapplication.appComida.ui.adapter.MealAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MealAdapter

    // Categorías según PDF
    private val categories = listOf("Beef", "Chicken", "Dessert", "Lamb", "Miscellaneous",
        "Pasta", "Pork", "Seafood", "Side", "Starter",
        "Vegan", "Vegetarian", "Breakfast", "Goat")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupDropdown()
    }

    private fun setupRecyclerView() {
        adapter = MealAdapter(emptyList()) { idMeal ->
            // Navegación al detalle
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

        binding.autoCompleteTxt.setOnItemClickListener { _, _, position, _ ->
            val selectedCategory = categories[position]
            fetchMeals(selectedCategory)
        }
    }

    private fun fetchMeals(category: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.getMealsByCategory(category)
                if (response.isSuccessful) {
                    val meals = response.body()?.meals ?: emptyList()
                    withContext(Dispatchers.Main) {
                        adapter.updateList(meals)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}