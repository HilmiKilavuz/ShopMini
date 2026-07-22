package com.example.shopmini.data.repository

import com.example.shopmini.data.local.ProductDao
import com.example.shopmini.data.local.ProductEntity
import com.example.shopmini.data.model.Product
import com.example.shopmini.data.remote.ShopMiniApi
import com.example.shopmini.domain.repository.ProductRepository
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val api: ShopMiniApi,
    private val dao: ProductDao
): ProductRepository {
    override suspend fun getProducts(): List<Product> {
        val localProducts = dao.getAllProducts()
        if(localProducts.isNotEmpty()) {
            return localProducts.map {
                Product(
                    id = it.id,
                    title = it.title,
                    description = it.description,
                    price = it.price,
                    thumbnail = it.thumbnail,
                    discountPercentage = it.discountPercentage
                )
            }

            }
        val remoteProducts = api.getProducts().products
        dao.insertProducts(remoteProducts.map {
            ProductEntity(
                id = it.id,
                title = it.title,
                description = it.description,
                price = it.price,
                thumbnail = it.thumbnail,
                discountPercentage = it.discountPercentage
            )
        })
        return remoteProducts


    }


}