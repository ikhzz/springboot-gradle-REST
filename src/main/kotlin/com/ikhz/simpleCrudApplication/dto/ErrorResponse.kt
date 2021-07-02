package com.ikhz.simpleCrudApplication.dto

data class ErrorResponse( val error: String) {
    val message: Array<String> = arrayOf()
}