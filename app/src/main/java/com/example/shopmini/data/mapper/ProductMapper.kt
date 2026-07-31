package com.example.shopmini.data.mapper

import com.example.shopmini.data.local.entity.ProductEntity
import com.example.shopmini.data.model.Product

//Veri dönüştürme işlemini kısaltmak için bu fonksiyonu kullanıyoruz
fun ProductEntity.toDomainModel(): Product{
    return Product(
    id = this.id,
    title = this.title,
    description = this.description,
    price = this.price,
    thumbnail = this.thumbnail,
    discountPercentage = this.discountPercentage,
    category = this.category,
    reviews = null
    )
}

fun Product.toEntity() : ProductEntity{
    return ProductEntity(
        id = this.id,
        title = this.title,
        description = this.description,
        price = this.price,
        thumbnail = this.thumbnail,
        discountPercentage = this.discountPercentage,
        category = this.category)

}