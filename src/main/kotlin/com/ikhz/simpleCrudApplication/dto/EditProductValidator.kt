package com.ikhz.simpleCrudApplication.dto

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class EditProductValidator(
    @NotEmpty
    val productId: String,

    @NotNull
    val amount: Long,

    val price: Long
)