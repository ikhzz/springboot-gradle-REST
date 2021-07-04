package com.ikhz.simpleCrudApplication.controllers

import com.ikhz.simpleCrudApplication.dto.EditProductValidator
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
    fun findAll(@RequestParam(required = false) search: String?): ResponseEntity<Any>{
        return productService.findAll(search)
    }

    @PostMapping("/create")
    fun create(@Valid @RequestBody product: Product, errors: Errors, @RequestHeader headers: HttpHeaders): ResponseEntity<Any> {
        // get token from header and setup error variable
        val token: String? = headers.getFirst(HttpHeaders.AUTHORIZATION)
        val error = ErrorResponse("Cannot create product")
        // token validator
        if(token.isNullOrEmpty()){
            error.message.add("Authorization token is required")
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error)
        }
        // split the token string
        val split = token.split(Regex(" "), 2)
        // check object validation error
        if(errors.hasErrors()){
            for(err in errors.allErrors){
                error.message.add(err.defaultMessage!!)
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error)
        }
        return productService.create(product, split[1])
    }
    // probably just set the type in validator for handle redundant, but i don't like setup logic in controller
    @PostMapping("buy")
    fun buy(@Valid @RequestBody product: EditProductValidator, errors: Errors,@RequestHeader headers: HttpHeaders): ResponseEntity<Any> {
        // get token from header and setup error variable
        val token: String? = headers.getFirst(HttpHeaders.AUTHORIZATION)
        val error = ErrorResponse("Cannot Buy product")
        // token validator
        if(token.isNullOrEmpty()){
            error.message.add("Authorization token is required")
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error)
        }
        // split the token string
        val split = token.split(Regex(" "), 2)
        // check object validation error
        if(errors.hasErrors()){
            for(err in errors.allErrors){
                error.message.add(err.defaultMessage!!)
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error)
        }
        return productService.buy(product, split[1])
    }

    @PostMapping("add")
    fun add(@Valid @RequestBody product: EditProductValidator, errors: Errors,@RequestHeader headers: HttpHeaders): ResponseEntity<Any> {
        // get token from header and setup error variable
        val token: String? = headers.getFirst(HttpHeaders.AUTHORIZATION)
        val error = ErrorResponse("Cannot create product")
        // token validator
        if(token.isNullOrEmpty()){
            error.message.add("Authorization token is required")
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error)
        }
        // split the token string
        val split = token.split(Regex(" "), 2)
        // check object validation error
        if(errors.hasErrors()){
            for(err in errors.allErrors){
                error.message.add(err.defaultMessage!!)
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error)
        }
        return productService.add(product, split[1])
    }
}