package com.ikhz.simpleCrudApplication.services

import com.ikhz.simpleCrudApplication.dto.ErrorResponse
import com.ikhz.simpleCrudApplication.dto.SignInValidator
import com.ikhz.simpleCrudApplication.dto.TokenResponse
import com.ikhz.simpleCrudApplication.dto.UserDto
import com.ikhz.simpleCrudApplication.models.User
import com.ikhz.simpleCrudApplication.repositories.UserRepo
import com.ikhz.simpleCrudApplication.utility.EncryptionHelper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService(private val userRepo: UserRepo, private val helper: EncryptionHelper) {

    fun findAll(): ResponseEntity<Any> {
        val result = userRepo.findAll()

        if(result.isEmpty()){
            val error = ErrorResponse("Data not found")
            error.message.add("User data Not available")
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error)
        }
        val findall = mutableListOf<UserDto>()
        for (user in result) findall.add(user.toUserDTO())
        return ResponseEntity.ok(findall)
    }

    fun signUp(user: User): ResponseEntity<Any> {
        val validEmail = userRepo.findByUserEmail(user.userEmail)
        if(validEmail.isNotEmpty()){
            val error = ErrorResponse("Sign Up Failed")
            error.message.add("Email is already exist")
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error)
        }
        user.userPassword = helper.encryption(user.userPassword)
        val result = userRepo.save(user)
        val response = TokenResponse("Sign Up Success", result.id!!, helper.tokenSecret)
        return ResponseEntity.ok(response)
    }

    fun signIn(user: SignInValidator): ResponseEntity<Any> {
        val validEmail = userRepo.findByUserEmail(user.userEmail)
        val error = ErrorResponse("Sign In Failed")

        if(validEmail.isEmpty()){
            error.message.add("Email is Not exist")
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error)
        }

        val validUser: User = (validEmail[0] as User)
        val password: String = helper.decryption(validUser.userPassword)

        if (user.userPassword != password){
            error.message.add("Wrong Password")
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error)
        }

        val response = TokenResponse("Sign In Success", validUser.id!!, helper.tokenSecret)
        return ResponseEntity.ok(response)
    }

    private fun User.toUserDTO() = UserDto(
        userName, userEmail
    )
}