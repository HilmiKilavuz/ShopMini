/**
 * UI Katmanı (Garson).
 * HomeScreen'in mantığını yönetir.
 * Repository'den veriyi ister ve sonucu HomeUiState olarak ekrana iletir.
 */
package com.example.shopmini.ui.screens.home


import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopmini.data.local.SearchHistoryEntity
import com.example.shopmini.data.model.CategoryDto
import com.example.shopmini.domain.repository.CategoryRepository
import com.example.shopmini.domain.repository.ProductRepository
import com.example.shopmini.domain.repository.SearchHistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productrepository: ProductRepository,
    private val categoryRepository: CategoryRepository,
    private val searchHistoryRepository: SearchHistoryRepository
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

    private val _searchQuery = MutableStateFlow<String>("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

     val searchHistory = searchHistoryRepository.getRecentSearches()

    private var currentSkip = 0
    private val limit = 20


    init {
        loadProducts()
        loadCategories()
        searchProducts()



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
                    productrepository.getProductsByCategory(
                        _selectedCategory.value!!,
                        skip = currentSkip,
                        limit = limit
                    )

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
/**Arama geçmişini silen fonksiyon
**/    fun deleteSearchHistory(entity: SearchHistoryEntity){
        viewModelScope.launch {
            searchHistoryRepository.deleteSearch(entity)
        }
    }

    // Arama sorgusunu güncelleyen fonksiyon
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        if (query.isBlank()) {
            loadProducts()
        }
    }
    //Arama sorgusunu çalıştırmak için kullanılan fonksiyon
    fun searchProducts() {
        viewModelScope.launch {
            _searchQuery
                .debounce(500)
                .filter { it.isNotBlank() }
                .collect { query ->
                    try {
                        val products = searchHistoryRepository.searchProducts(query)
                        _uiState.value = HomeUiState.Success(products)

                    } catch (e: Exception) {
                        _uiState.value = HomeUiState.Error(e.message ?: "Arama hatası")
                    }
                }
        }
    }
// Arama sorgusunu kaydeden fonksiyon
    fun saveSearchQuery(query: String) {
        if (query.isNotBlank()) {
            viewModelScope.launch {
                searchHistoryRepository.insertSearch(
                    SearchHistoryEntity(id = 0, query = query, timestamp = System.currentTimeMillis())
                )
            }
        }
    }




}
