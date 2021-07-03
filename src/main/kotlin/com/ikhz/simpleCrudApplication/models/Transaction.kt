package com.ikhz.simpleCrudApplication.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import java.sql.Timestamp
import java.util.*
import javax.validation.constraints.NotNull

@Document
data class Transaction(

    @Id
    val id: String?,

    @NotNull(message = "Amount is required")
    val amount: Long,

    @NotNull(message = "Total is required")
    val total: Long,

    val transactionType: TransactionType,

    val createdAt: Date = Timestamp(System.currentTimeMillis()),
    // relation to product
    var productId: String?,
    // relation to user
    var createId: String?
)