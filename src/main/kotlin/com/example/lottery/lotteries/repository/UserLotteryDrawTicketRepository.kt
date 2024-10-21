package com.example.lottery.lotteries.repository

import com.example.lottery.lotteries.entities.UserLotteryDrawTicket
import org.springframework.data.jpa.repository.JpaRepository

interface UserLotteryDrawTicketRepository : JpaRepository<UserLotteryDrawTicket, Long> {
    fun findAllByUidAndRound(uid: Long, round: Int): List<UserLotteryDrawTicket>?
}