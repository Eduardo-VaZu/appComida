package pe.app.myapplication.appComida.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pe.app.myapplication.appComida.data.model.Meal
import pe.app.myapplication.appComida.data.network.RetrofitClient


class MainViewModel : ViewModel() {

    private val _meals = MutableLiveData<List<Meal>>()
    val meals: LiveData<List<Meal>> get() = _meals

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun fetchMeals(category: String) {
        _isLoading.value = true
        _error.value = null
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = if (category == "Todos los platos") {
                    RetrofitClient.instance.getMealsByName("")
                } else {
                    RetrofitClient.instance.getMealsByCategory(category)
                }

                launch(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val mealList = response.body()?.meals ?: emptyList()
                        _meals.value = mealList
                    } else {
                        _error.value = "Error en la respuesta: ${response.code()}"
                    }
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                launch(Dispatchers.Main) { _error.value = "Fallo de conexión: ${e.message}"; _isLoading.value = false}
            }
        }
    }
}