package com.ikhz.simpleCrudApplication.dto

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm

class TokenResponse(val status: String, id: String, secret: String){
    var token: String = Jwts.builder().setSubject(id).signWith(SignatureAlgorithm.HS512, secret).compact()
}