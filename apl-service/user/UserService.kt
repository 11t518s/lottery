package com.example.lottery.user

import com.example.lottery.user.entities.User
import com.example.lottery.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository
) {
    @Transactional
    fun createUser(name: String): User {
        val user = User(name = name)
        return userRepository.save(user)
    }
}