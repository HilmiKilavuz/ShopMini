package com.example.shopmini.ui.screens.home


import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopmini.data.model.CategoryDto
import com.example.shopmini.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()
    private val _categories = MutableStateFlow<List<CategoryDto>>(emptyList())
    val categories: StateFlow<List<CategoryDto>> = _categories.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()


    init {
        loadProducts()
        loadCategories()
    }

    fun loadProducts() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            _isRefreshing.value = true
            try {
                val products = repository.getProducts()
                _uiState.value = HomeUiState.Success(products)
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e.message ?: "Unknown error")
            } finally {
                _isRefreshing.value = false

            }


        }


    }

    private fun loadCategories() {


        viewModelScope.launch {

            try {
                _categories.value = repository.getCategories()

            } catch (e: Exception) {

            }
        }
    }

    fun onCategorySelected(slug: String?) {
        _selectedCategory.value = slug
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                val products = if (slug == null) {
                    repository.getProducts()

                } else {
                    repository.getProductsByCategory(slug)

                }

                _uiState.value = HomeUiState.Success(products)

            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e.message ?: "Hata")


            } finally {
                _isRefreshing.value = false
            }

        }


    }

}

