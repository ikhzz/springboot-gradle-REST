package com.ikhz.simpleCrudApplication.dto

import javax.validation.constraints.NotEmpty

data class SignInValidator(
    @NotEmpty(message = "User email is required")
    val userEmail: String,

    @NotEmpty
    val userPassword: String
)