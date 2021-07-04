package com.ikhz.simpleCrudApplication.repositories

import com.ikhz.simpleCrudApplication.models.Product
import org.springframework.data.mongodb.repository.MongoRepository

interface ProductRepo : MongoRepository<Product, String> {
    fun findByProductNameLike(search: String) : List<Product>
}