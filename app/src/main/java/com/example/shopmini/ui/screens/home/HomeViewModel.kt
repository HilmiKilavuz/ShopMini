/**
 * UI Katmanı (Garson).
 * HomeScreen'in mantığını yönetir.
 * Repository'den veriyi ister ve sonucu HomeUiState olarak ekrana iletir.
 */
package com.example.shopmini.ui.screens.home


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopmini.data.local.entity.SearchHistoryEntity
import com.example.shopmini.data.model.CategoryDto
import com.example.shopmini.domain.usecase.category.GetCategoriesUseCase
import com.example.shopmini.domain.usecase.product.GetProductByCategoryUseCase
import com.example.shopmini.domain.usecase.product.GetProductsUseCase
import com.example.shopmini.domain.usecase.searchHistory.DeleteSearcHistoryUseCase
import com.example.shopmini.domain.usecase.searchHistory.GetRecentSearchesUseCase
import com.example.shopmini.domain.usecase.searchHistory.InsertSearchUseCase
import com.example.shopmini.domain.usecase.searchHistory.SearchProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject
//Ana ekran için oluşturulmuş ViewModel
@HiltViewModel
class HomeViewModel @Inject constructor(


    private val getProductsUseCase: GetProductsUseCase,
    private val getProductsByCategoryUseCase: GetProductByCategoryUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getRecentSearchesUseCase: GetRecentSearchesUseCase,
    private val deleteSearchesUseCase: DeleteSearcHistoryUseCase,
    private val searchProductsUseCase: SearchProductsUseCase,
    private val insertSearchUseCase: InsertSearchUseCase
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

    val searchHistory = getRecentSearchesUseCase()

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

                val products = getProductsUseCase()
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

                _categories.value = getCategoriesUseCase()

            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error("Categories cannot be loaded")

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
                    getProductsUseCase()

                } else {
                    getProductsByCategoryUseCase(slug)

                }
                currentSkip = products.size
                _uiState.value = HomeUiState.Success(products)

            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e.message ?: "Category error")


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
                    getProductsUseCase(skip = currentSkip, limit = limit)

                } else {

                    getProductsByCategoryUseCase(
                        _selectedCategory.value!!,
                        skip = currentSkip,
                        limit = limit
                    )

                }
                currentSkip += newProducts.size

                _uiState.value = HomeUiState.Success(currentProduct + newProducts)


            } catch (e: Exception) {
                //// hata yazısı yerine hata mesajı gösterilebilir uygulama çökmesin

                _uiState.value = HomeUiState.Error(e.message ?: "Page load error")


            } finally {
                _isLoadingNextPage.value = false
            }


        }
    }

    /**Arama geçmişini silen fonksiyon
     **/
    fun deleteSearchHistory(entity: SearchHistoryEntity) {
        viewModelScope.launch {
            deleteSearchesUseCase(entity)
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
                        val products = searchProductsUseCase(query)
                        _uiState.value = HomeUiState.Success(products)

                    } catch (e: Exception) {
                        _uiState.value = HomeUiState.Error(e.message ?: "Search Error")
                    }
                }
        }
    }

    // Arama sorgusunu kaydeden fonksiyon
    fun saveSearchQuery(query: String) {
        if (query.isNotBlank()) {
            viewModelScope.launch {
                insertSearchUseCase(
                    SearchHistoryEntity(
                        id = 0,
                        query = query,
                        timestamp = System.currentTimeMillis()
                    )
                )
            }
        }
    }


}
