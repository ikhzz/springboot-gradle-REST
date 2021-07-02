package com.ikhz.simpleCrudApplication.repositories

import com.ikhz.simpleCrudApplication.models.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepo : MongoRepository<User, String> {
    fun findByUserEmail(email: String): List<Any>
}