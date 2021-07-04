package com.ikhz.simpleCrudApplication.services

import com.ikhz.simpleCrudApplication.dto.ErrorResponse
import com.ikhz.simpleCrudApplication.repositories.TransactionRepo
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class TransactionService(private val transactionRepo: TransactionRepo) {
    fun findAll(): ResponseEntity<Any> {
        val result = transactionRepo.findAll()
        if (result.isEmpty()){
            val error = ErrorResponse("Data not found")
            error.message.add("Transaction data Not available")
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error)
        }

        return ResponseEntity.ok(result)
    }
}