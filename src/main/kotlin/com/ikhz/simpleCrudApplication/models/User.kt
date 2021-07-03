package com.ikhz.simpleCrudApplication.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import javax.validation.constraints.NotEmpty

@Document
data class User (

    @Id
    val id: String?,

    @NotEmpty(message = "User Name is required")
    val userName: String,

    @NotEmpty(message = "User Email is required")
    val userEmail: String,

    @NotEmpty(message = "User Password is required")
    var userPassword: String,

    val role: Role = Role.GUEST,
)