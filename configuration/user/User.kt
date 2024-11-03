package com.example.lottery.user.entities

import javax.persistence.*

@Entity
@Table(name = "user")
class User(
    var name: String? = null,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}