package com.example.lottery.user.repository

import org.springframework.stereotype.Repository


@Repository
class UserPointRepository {
    private val pointsStorage: MutableMap<Long, Int> = mutableMapOf()

    fun findByUid(uid: Long): Int {
        return pointsStorage.getOrPut(uid) { 0 }  // 없으면 0 포인트로 새로 생성
    }

    fun addPoints(uid: Long, points: Int) {
        val currentPoints = findByUid(uid)  // 현재 포인트 조회, 없으면 0
        pointsStorage[uid] = currentPoints + points  // 포인트 추가
    }
}