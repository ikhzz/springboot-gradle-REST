package com.ikhz.simpleCrudApplication.dto

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import lombok.Data

@Data
class TokenResponse(val status: String, private val id: String)