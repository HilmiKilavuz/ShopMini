/**
 * DATA Katmanı .
 * ProductRepository sözleşmesini uygular.
 * Single Source of Truth: Önce Room'a bakar, yoksa API'den çeker ve Room'a kaydeder.
 */
package com.example.shopmini.data.repository

import com.example.shopmini.data.local.ProductDao
import com.example.shopmini.data.local.ProductEntity
import com.example.shopmini.data.model.CategoryDto
import com.example.shopmini.data.model.Product
import com.example.shopmini.data.remote.ShopMiniApi
import com.example.shopmini.domain.repository.ProductRepository
import javax.inject.Inject
import kotlin.String

class ProductRepositoryImpl @Inject constructor(
    private val api: ShopMiniApi,
    private val dao: ProductDao
) : ProductRepository {

    /**
     * Tüm ürünleri getirir.
     * Çalışma Mantığı (Single Source of Truth):
     * 1. Eğer ilk sayfa isteniyorsa (skip == 0), önce Room veritabanına bakar.
     * 2. Room'da veri varsa hemen onu döndürür.
     * 3. Ardından (veya Room boşsa) API'ye istek atar.
     * 4. API'den gelen güncel verileri Room veritabanına kaydeder ve listeyi döndürür.
     */
    override suspend fun getProducts(limit: Int, skip: Int): List<Product> {

        if (skip == 0) {
            val localProducts = dao.getAllProducts()
            if (localProducts.isNotEmpty()) {
                return localProducts.map {
                    Product(
                        id = it.id,
                        title = it.title,
                        description = it.description,
                        price = it.price,
                        thumbnail = it.thumbnail,
                        discountPercentage = it.discountPercentage,
                        category = it.category
                    )
                }

            }
        }

        val remoteProducts = api.getProducts(limit, skip).products
        dao.insertProducts(remoteProducts.map {
            ProductEntity(
                id = it.id,
                title = it.title,
                description = it.description,
                price = it.price,
                thumbnail = it.thumbnail,
                discountPercentage = it.discountPercentage,
                category = it.category
            )
        })
        return remoteProducts


    }

    /**
     * Kategoriye özel ürünleri getirir.
     */
    override suspend fun getProductsByCategory(slug: String, limit: Int, skip: Int): List<Product> {
        return api.getProductsByCategory(slug, limit, skip).products

    }


    /**
     * Belirli bir ürünü getirir.
     * Çalışma Mantığı: Önce Room (Yerel) veritabanına bakar.
     * Eğer ürün Room'da varsa oradan döndürür, yoksa API'den (İnternetten) çeker.
     */
    override suspend fun getProductById(id: Int): Product {
        val localProduct = dao.getProductById(id)
        if (localProduct != null) {
            return localProduct.let {
                Product(
                    id = it.id,
                    title = it.title,
                    description = it.description,
                    price = it.price,
                    thumbnail = it.thumbnail,
                    discountPercentage = it.discountPercentage,
                    category = it.category
                )
            }


        }
        val remoteProduct = api.getProductById(id)
        return remoteProduct


    }


}
