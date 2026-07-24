/**
 * UI Katmanı (Garson).
 * HomeScreen'in mantığını yönetir.
 * Repository'den veriyi ister ve sonucu HomeUiState olarak ekrana iletir.
 */
package com.example.shopmini.ui.screens.home


import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopmini.data.model.CategoryDto
import com.example.shopmini.domain.repository.CategoryRepository
import com.example.shopmini.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productrepository: ProductRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()
    private val _categories = MutableStateFlow<List<CategoryDto>>(emptyList())
    val categories: StateFlow<List<CategoryDto>> = _categories.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()
    private val _isLoadingNextPage = MutableStateFlow(false)
    val isLoadingNextPage: StateFlow<Boolean> = _isLoadingNextPage.asStateFlow()

    private var currentSkip = 0
    private val limit = 20




    init {
        loadProducts()
        loadCategories()
    }

    /**
     * Uygulama açıldığında veya pull-to-refresh  yapıldığında
     * ürünlerin sıfırdan yüklenmesini sağlar.
     */
    fun loadProducts() {
        viewModelScope.launch {

            _uiState.value = HomeUiState.Loading
            _isRefreshing.value = true
            try {

                val products = productrepository.getProducts()
                currentSkip = products.size
                _uiState.value = HomeUiState.Success(products)
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e.message ?: "Unknown error")
            } finally {
                _isRefreshing.value = false

            }


        }


    }

    /**
     * Kategori isimlerini  ekrana basmak için kategorileri getirir.
     */
 fun loadCategories() {


        viewModelScope.launch {

            try {

                _categories.value = categoryRepository.getCategories()

            } catch (e: Exception) {

            }
        }
    }

    /**
     * Kullanıcı üstteki yatay menüden bir kategoriye tıkladığında çalışır.
     * Seçilen kategoriye ait ürünleri filtreleyerek getirir.
     */
    fun onCategorySelected(slug: String?) {
        _selectedCategory.value = slug
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                currentSkip = 0
                val products = if (slug == null) {
                    productrepository.getProducts()

                } else {
                    productrepository.getProductsByCategory(slug)

                }
                currentSkip = products.size
                _uiState.value = HomeUiState.Success(products)

            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e.message ?: "Hata")


            } finally {
                _isRefreshing.value = false
            }

        }


    }

    /**
     * Kullanıcı sayfanın  en altına kaydırdığında
     * bir sonraki sayfayı  yüklemek için çalışır.
     */
    fun loadPage() {
        if (isLoadingNextPage.value || uiState.value !is HomeUiState.Success) {
            return
        }
        val currentProduct = (_uiState.value as HomeUiState.Success).products
        viewModelScope.launch {
            _isLoadingNextPage.value = true

            try {

                val newProducts = if (selectedCategory.value == null) {
                   productrepository.getProducts(skip = currentSkip, limit = limit)

                } else {
                    productrepository.getProductsByCategory(_selectedCategory.value!!,skip = currentSkip, limit = limit)

                }
                currentSkip += newProducts.size

                _uiState.value = HomeUiState.Success(currentProduct + newProducts)


            } catch (e: Exception) {
                //// hata yazısı yerine hata mesajı gösterilebilir uygulama çökmesin

                _uiState.value = HomeUiState.Error(e.message ?: "Hata")


            } finally {
                _isLoadingNextPage.value = false
            }


        }
    }
}
