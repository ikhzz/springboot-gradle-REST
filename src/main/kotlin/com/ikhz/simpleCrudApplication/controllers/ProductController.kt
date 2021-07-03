package com.ikhz.simpleCrudApplication.controllers

import com.ikhz.simpleCrudApplication.dto.ErrorResponse
import com.ikhz.simpleCrudApplication.models.Product
import com.ikhz.simpleCrudApplication.services.ProductService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.Errors
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/product")
class ProductController(private val productService: ProductService) {

    @GetMapping
    fun findAll(): ResponseEntity<Any>{
        return productService.findAll()
    }

    @PostMapping("/create")
    fun create(@Valid @RequestBody product: Product, errors: Errors, @RequestHeader headers: HttpHeaders): ResponseEntity<Any> {
        val token: String? = headers.getFirst(HttpHeaders.AUTHORIZATION)

        if(token.isNullOrEmpty()){
            val error = ErrorResponse("Cannot create product")
            error.message.add("Authorization token is required")
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error)
        }

        val split = token.split(Regex(" "), 2)

        if(errors.hasErrors()){
            val error = ErrorResponse("Cannot create product")
            for(err in errors.allErrors){
                error.message.add(err.defaultMessage!!)
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error)
        }
        return productService.create(product, split[1])
    }
}