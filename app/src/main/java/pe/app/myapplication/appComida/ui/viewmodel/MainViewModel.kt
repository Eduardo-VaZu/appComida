package pe.app.myapplication.appComida.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pe.app.myapplication.appComida.data.model.Category
import pe.app.myapplication.appComida.data.model.Meal
import pe.app.myapplication.appComida.data.network.RetrofitClient


class MainViewModel : ViewModel() {

    private val _meals = MutableLiveData<List<Meal>>()
    val meals: LiveData<List<Meal>> get() = _meals

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> get() = _categories

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun fetchCategories() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.getCategories()
                if (response.isSuccessful) {
                    _categories.value = response.body()?.categories ?: emptyList()
                } else {
                    _error.value = "Error al cargar categorías"
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun fetchMeals(category: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = if (category == "Todos los platos") {
                    RetrofitClient.instance.getMealsByName("")
                } else {
                    RetrofitClient.instance.getMealsByCategory(category)
                }

                if (response.isSuccessful) {
                    _meals.value = response.body()?.meals ?: emptyList()
                } else {
                    _error.value = "Error al cargar comidas"
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}