package com.example.lottery.user.controller

import com.example.lottery.user.dtos.CreateUserRequest
import com.example.lottery.user.dtos.CreateUserResponse
import com.example.lottery.user.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user") // 기본 경로 설정
class UserController(
    private val userService: UserService
) {
    @PostMapping
    fun createUser(@RequestBody body: CreateUserRequest): ResponseEntity<CreateUserResponse> {
        val createdUser = userService.createUser(name = body.name)

        return ResponseEntity.ok(CreateUserResponse(data = createdUser))
    }

}