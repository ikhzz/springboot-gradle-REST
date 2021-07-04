package com.ikhz.simpleCrudApplication.dto

data class EditProductResponse(
    val status: String,
    val action: String,
    val productName: String,
    val amount: Long,
    val total: Long,
)