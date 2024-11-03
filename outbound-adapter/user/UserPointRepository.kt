package com.example.lottery.user

import org.springframework.stereotype.Repository


@Repository
class UserPointRepository {
    private val pointsStorage: MutableMap<Long, Int> = mutableMapOf()

    fun findByUid(uid: Long): Int {
        return pointsStorage.getOrPut(uid) { 0 }
    }

    fun addPoints(uid: Long, points: Int) {
        val currentPoints = findByUid(uid)
        pointsStorage[uid] = currentPoints + points
    }
}