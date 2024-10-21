package com.example.lottery.lotteries.repository

import com.example.lottery.lotteries.entities.LotteryMission
import org.springframework.data.jpa.repository.JpaRepository

interface LotteryMissionRepository : JpaRepository<LotteryMission, Long> {
    fun findAllByEnabledTrue(): List<LotteryMission>
}