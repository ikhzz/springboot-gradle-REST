package com.ikhz.simpleCrudApplication.services

import com.ikhz.simpleCrudApplication.dto.ErrorResponse
import com.ikhz.simpleCrudApplication.models.Product
import com.ikhz.simpleCrudApplication.models.Transaction
import com.ikhz.simpleCrudApplication.models.TransactionType
import com.ikhz.simpleCrudApplication.repositories.ProductRepo
import com.ikhz.simpleCrudApplication.repositories.TransactionRepo
import com.ikhz.simpleCrudApplication.repositories.UserRepo
import com.ikhz.simpleCrudApplication.utility.EncryptionHelper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class ProductService(
    private val productRepo: ProductRepo,

    private val helper: EncryptionHelper,

    private val userRepo: UserRepo,

    private val transactionRepo: TransactionRepo
) {

    fun findAll(): ResponseEntity<Any> {
        val result = productRepo.findAll()

        if (result.isEmpty()){
            val error = ErrorResponse("Data not found")
            error.message.add("Product data Not available")
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error)
        }

        return ResponseEntity.ok(result)
    }

    fun create(product: Product, token: String): ResponseEntity<Any>{
        val id = helper.tokenDecryption(token)
        val user = userRepo.findById(id)
        // check if user is exist
        if (user.isEmpty){
            val error = ErrorResponse("User not found")
            error.message.add("Suplier id is Not valid")
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error)
        }

        product.suplierId = user.get().id
        val result = productRepo.save(product)
        // create transaction object
        val transaction = Transaction(
            amount = product.productStock,
            total = product.productPrice * product.productStock,
            transactionType = TransactionType.ADD,
            productId = result.id!!,
            createId = user.get().id!!
        )
        // save transaction object
        transactionRepo.save(transaction)
        return ResponseEntity.ok(result)
    }
    // model mapper object
    private fun Transaction(
        amount: Long,
        total: Long,
        transactionType: TransactionType,
        productId: String,
        createId: String
    ): Transaction {
        return Transaction(amount, total, transactionType, productId, createId)
    }
}