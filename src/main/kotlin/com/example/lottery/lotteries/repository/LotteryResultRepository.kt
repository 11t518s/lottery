package com.example.lottery.lotteries.repository

import com.example.lottery.lotteries.entities.LotteryResult
import org.springframework.data.jpa.repository.JpaRepository

interface LotteryResultRepository : JpaRepository<LotteryResult, Long> {
    fun findByRoundAndUid(round: Int, uid: Long): LotteryResult?
}