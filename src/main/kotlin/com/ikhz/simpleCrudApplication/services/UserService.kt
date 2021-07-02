package com.ikhz.simpleCrudApplication.services

import com.ikhz.simpleCrudApplication.dto.ErrorResponse
import com.ikhz.simpleCrudApplication.dto.TokenResponse
import com.ikhz.simpleCrudApplication.models.User
import com.ikhz.simpleCrudApplication.repositories.UserRepo
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService(private val userRepo: UserRepo) {

    fun findAll(): ResponseEntity<Any> {
        val result = userRepo.findAll();
        if(result.isEmpty()){
            val error = ErrorResponse("Data not found")
            error.message.plus("Data Not available")
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error)
        }
        return ResponseEntity.ok("tes")
    }

    fun signUp(user: User): ResponseEntity<Any> {
        val validEmail = userRepo.findByUserEmail(user.userEmail)
        if(validEmail.isNotEmpty()){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("wadoh")
        }
        val result = userRepo.save(user)
        // this need 2 old dependency
        val token : String = Jwts.builder().setSubject("a").signWith(SignatureAlgorithm.HS256, "secret").compact()

        return ResponseEntity.ok(token)
    }
}