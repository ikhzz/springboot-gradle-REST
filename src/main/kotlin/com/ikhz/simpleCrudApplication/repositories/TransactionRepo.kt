package com.ikhz.simpleCrudApplication.repositories

import com.ikhz.simpleCrudApplication.models.Transaction
import org.springframework.data.mongodb.repository.MongoRepository

interface TransactionRepo: MongoRepository<Transaction, String> {
}