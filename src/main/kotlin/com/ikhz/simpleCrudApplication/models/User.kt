package com.ikhz.simpleCrudApplication.models

import org.springframework.data.mongodb.core.mapping.Document

@Document
data class User (

    val id: String,

    val userName: String,
)