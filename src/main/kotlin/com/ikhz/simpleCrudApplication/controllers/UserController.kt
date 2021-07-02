package com.ikhz.simpleCrudApplication.controllers

import com.ikhz.simpleCrudApplication.models.User
import com.ikhz.simpleCrudApplication.services.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/user")
class UserController(val userService: UserService) {

    @GetMapping
    fun findAll(): ResponseEntity<Any> {
        return userService.findAll();
    }

    @PostMapping("/signup")
    fun signUp(@Valid @RequestBody user: User, errors: Error): ResponseEntity<Any> {

        return userService.signUp(user)
    }
}