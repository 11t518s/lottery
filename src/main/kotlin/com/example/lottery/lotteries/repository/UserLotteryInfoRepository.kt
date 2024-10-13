package com.example.lottery.lotteries.repository

import com.example.lottery.lotteries.entities.UserLotteryInfo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface UserLotteryInfoRepository : JpaRepository<UserLotteryInfo, Long> {
    @Modifying
    @Query("UPDATE user_lottery_info SET total_score = total_score + :scoreToAdd WHERE uid = :uid", nativeQuery = true)
    fun incrementTotalScore(uid: Long, scoreToAdd: Int)
}