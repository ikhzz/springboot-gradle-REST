package com.ikhz.simpleCrudApplication.services

import com.ikhz.simpleCrudApplication.dto.EditProductResponse
import com.ikhz.simpleCrudApplication.dto.EditProductValidator
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
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ProductService(
    private val productRepo: ProductRepo,

    private val helper: EncryptionHelper,

    private val userRepo: UserRepo,

    private val transactionRepo: TransactionRepo
) {
    // method to get all product data
    fun findAll(search: String?): ResponseEntity<Any> {
        // validation if data is empty
        val result: List<Product> = if(search == null){
            productRepo.findAll()
        } else {
            productRepo.findByProductNameLike(search)
        }
        if (result.isEmpty()){
            val error = ErrorResponse("Data not found")
            error.message.add("Product data Not available")
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error)
        }

        return ResponseEntity.ok(result)
    }

    fun create(product: Product, token: String): ResponseEntity<Any>{
        // decrypt token find the id
        val id = helper.tokenDecryption(token)
        val user = userRepo.findById(id)
        // validation if user not exist
        if (user.isEmpty){
            val error = ErrorResponse("User not found")
            error.message.add("Suplier id is Not valid")
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error)
        }

        product.suplierId = user.get().id
        val result = productRepo.save(product)
        // create transaction object
        val transaction = Transaction(
            id = null,
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

    // buy product method
    fun buy(product: EditProductValidator, token: String): ResponseEntity<Any> {
        // setup error variable, decrypt token and find the id
        val error = ErrorResponse("Buy Product Failed")
        val id = helper.tokenDecryption(token)
        val user = userRepo.findById(id)
        // validation if user not exist
        if (user.isEmpty){
            error.message.add("Admin id is Not valid")
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error)
        }
        // get the product data
        val result = productRepo.findById(product.productId)
        // validation if it's not exist
        if(result.isEmpty){
            error.message.add("Product id is not valid")
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error)
        }
        // validation if amount requested is more than stock
        if(result.get().productStock < product.amount){
            error.message.add("Product stock is not available")
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error)
        }
        // reduce the stock
        result.get().productStock = result.get().productStock - product.amount
        // set the transaction object
        val transaction = Transaction(
            id = null,
            amount = product.amount,
            total = result.get().productPrice * product.amount,
            transactionType = TransactionType.BUY,
            productId = result.get().id!!,
            createId = user.get().id!!
        )
        // save transaction and product object
        transactionRepo.save(transaction)
        productRepo.save(result.get())
        // set the response object
        val productResponse = EditProductResponse(
            "Success",
            "Buy Product",
            result.get().productName,
            product.amount,
            product.amount * result.get().productPrice
        )

        return ResponseEntity.ok(productResponse)
    }
    // add product method
    fun add(product: EditProductValidator, token: String): ResponseEntity<Any> {
        // setup error variable, decrypt token and find the id
        val error = ErrorResponse("Add Product Failed")
        val id = helper.tokenDecryption(token)
        val user = userRepo.findById(id)
        // validation if it's not exist
        if (user.isEmpty){
            error.message.add("Suplier id is Not valid")
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error)
        }
        // get the product data
        val result = productRepo.findById(product.productId)
        // validation if it's not exist
        if(result.isEmpty){
            error.message.add("Product id is not valid")
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error)
        }
        // add the stock
        result.get().productStock = result.get().productStock + product.amount
        // set the transaction object
        val transaction = Transaction(
            id = null,
            amount = product.amount,
            total = result.get().productPrice * product.amount,
            transactionType = TransactionType.ADD,
            productId = result.get().id!!,
            createId = user.get().id!!
        )
        // save transaction and product object
        transactionRepo.save(transaction)
        productRepo.save(result.get())
        // set the response object
        val productResponse = EditProductResponse(
            "Success",
            "Add Product",
            result.get().productName,
            product.amount,
            product.amount * result.get().productPrice
        )
        return ResponseEntity.ok(productResponse)
    }
}