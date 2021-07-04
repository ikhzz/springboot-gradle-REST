package com.ikhz.simpleCrudApplication.controllers

import com.ikhz.simpleCrudApplication.services.TransactionService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/trans")
class TransactionController(private val transaction: TransactionService) {

    fun findAll(): ResponseEntity<Any> {
        return transaction.findAll()
    }
}