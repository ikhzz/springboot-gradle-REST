package com.ikhz.simpleCrudApplication.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import java.sql.Timestamp
import java.util.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import kotlin.collections.ArrayList

@Document
data class Product(

    @Id
    val id: String?,

    @NotEmpty(message = "Product name is required")
    val productName: String,

    @NotNull(message = "Product price is required")
    val productPrice: Long,

    @NotNull(message = "Product stock is required")
    val productStock: Long,

    val createdAt: Date = Timestamp(System.currentTimeMillis()),

    val updatedAt: Date = Timestamp(System.currentTimeMillis()),
    // manual query relation by object id
    var suplierId: String?
)