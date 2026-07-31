/**
 * DATA Katmanı .
 * ProductRepository sözleşmesini uygular.
 * Single Source of Truth: Önce Room'a bakar, yoksa API'den çeker ve Room'a kaydeder.
 */
package com.example.shopmini.data.repository

import com.example.shopmini.data.local.dao.ProductDao
import com.example.shopmini.data.mapper.toDomainModel
import com.example.shopmini.data.mapper.toEntity
import com.example.shopmini.data.model.Product
import com.example.shopmini.data.remote.ShopMiniApi
import com.example.shopmini.domain.repository.ProductRepository
import javax.inject.Inject
//Ürünleri almak için repository sınıfı
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
                   it.toDomainModel()
                }

            }
        }

        val remoteProducts = api.getProducts(limit, skip).products
        dao.insertProducts(remoteProducts.map {
            it.toEntity()
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
     * Çalışma Mantığı: Önce API'a  bakar.
     * Eğer ürün API'da varsa oradan döndürür, yoksa ROOM'dan çeker.
     */
    override suspend fun getProductById(id: Int): Product {
        return try {

            val remoteProduct = api.getProductById(id)
            remoteProduct
        } catch (e: Exception) {

            val localProduct = dao.getProductById(id)

            if (localProduct != null) {

                Product(
                    id = localProduct.id,
                    title = localProduct.title,
                    description = localProduct.description,
                    price = localProduct.price,
                    thumbnail = localProduct.thumbnail,
                    discountPercentage = localProduct.discountPercentage,
                    category = localProduct.category,
                    reviews = null
                )
            } else {

                throw Exception("Ürün bulunamadı ve internet bağlantısı yok.")
            }
        }

    }


}
