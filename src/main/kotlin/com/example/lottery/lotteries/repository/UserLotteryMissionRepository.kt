package com.example.lottery.lotteries.repository

import com.example.lottery.lotteries.entities.UserLotteryMission
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface UserLotteryMissionRepository : JpaRepository<UserLotteryMission, Long> {
    fun findAllByUidAndCreatedAt(uid: Long, createdAt: LocalDate): List<UserLotteryMission>?
}