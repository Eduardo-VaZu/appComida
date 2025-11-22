package pe.app.myapplication.appComida.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pe.app.myapplication.appComida.data.model.Meal
import pe.app.myapplication.appComida.data.network.RetrofitClient

class DetailViewModel : ViewModel() {

    private val _meal = MutableLiveData<Meal?>()
    val meal: LiveData<Meal?> get() = _meal

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun fetchMealDetails(id: String) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.instance.getMealDetails(id)

                if (response.isSuccessful) {
                    val meals = response.body()?.meals
                    if (!meals.isNullOrEmpty()) {
                        _meal.postValue(meals[0])
                    } else {
                        _error.postValue("No se encontraron detalles para este plato")
                    }
                } else {
                    _error.postValue("Error en el servidor")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _error.postValue("Exception: ${e.message}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}