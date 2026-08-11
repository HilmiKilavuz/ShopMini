package com.example.shopmini.data.repository

import com.example.shopmini.data.local.dao.CartDao
import com.example.shopmini.data.local.entity.CartEntity
import com.example.shopmini.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

//sepet için repository
class CartRepositoryImpl @Inject constructor(
    private val dao : CartDao
) : CartRepository {
    //sepeti getirir
    override fun getCartItems(): Flow<List<CartEntity>> {
       return dao.getCartItems()

    }
//sepete ekler
    override suspend fun insertItem(item: CartEntity) {
        dao.insertItem(item)
    }
//sepetten siler
    override suspend fun deleteItem(item: CartEntity) {
        dao.deleteItem(item)
    }
//sepetten miktarı günceller
    override suspend fun updateQuantity(productId: Int, newQuantity: Int) {
        dao.updateQuantity(productId, newQuantity)
    }

    override suspend fun clearCart() {
        dao.deleteAllItems()

    }
}