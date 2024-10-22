package com.example.lottery.lotteries.repository

import com.example.lottery.lotteries.entities.LotteryRound
import org.springframework.data.jpa.repository.JpaRepository

interface LotteryRoundRepository : JpaRepository<LotteryRound, Int> {
    fun findByRound(round: Int): LotteryRound?
    fun existsByRound(round: Int): Boolean
}