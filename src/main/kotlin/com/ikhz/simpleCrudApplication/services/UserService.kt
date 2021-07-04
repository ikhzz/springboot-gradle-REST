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
    // method get all user
    fun findAll(): ResponseEntity<Any> {
        val result = userRepo.findAll()
        // user validator if data is empty
        if(result.isEmpty()){
            val error = ErrorResponse("Data not found")
            error.message.add("User data Not available")
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error)
        }
        val findall = mutableListOf<UserDto>()
        // set all data to DTO
        for (user in result) findall.add(user.toUserDTO())
        return ResponseEntity.ok(findall)
    }
    // service method for signup
    fun signUp(user: User): ResponseEntity<Any> {
        val validEmail = userRepo.findByUserEmail(user.userEmail)
        // email validation
        if(validEmail.isNotEmpty()){
            val error = ErrorResponse("Sign Up Failed")
            error.message.add("Email is already exist")
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error)
        }
        // encrypt password
        user.userPassword = helper.encryption(user.userPassword)
        val result = userRepo.save(user)
        // send token response
        val response = TokenResponse("Sign Up Success", result.id!!, helper.tokenSecret)
        return ResponseEntity.ok(response)
    }
    // service method for signin
    fun signIn(user: SignInValidator): ResponseEntity<Any> {
        val validEmail = userRepo.findByUserEmail(user.userEmail)
        val error = ErrorResponse("Sign In Failed")
        // email validation if already exist
        if(validEmail.isEmpty()){
            error.message.add("Email is Not exist")
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error)
        }
        // get the user object and decrypt the password
        val validUser: User = (validEmail[0] as User)
        val password: String = helper.decryption(validUser.userPassword)
        // password validation
        if (user.userPassword != password){
            error.message.add("Wrong Password")
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error)
        }
        // send token response
        val response = TokenResponse("Sign In Success", validUser.id!!, helper.tokenSecret)
        return ResponseEntity.ok(response)
    }
    // method to set DTO as response
    private fun User.toUserDTO() = UserDto(
        userName, userEmail
    )
}