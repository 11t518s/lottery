package com.example.lottery.lotteries.repository

import com.example.lottery.lotteries.entities.UserLotteryInfo
import org.springframework.data.jpa.repository.JpaRepository

interface UserLotteryInfoRepository : JpaRepository<UserLotteryInfo, Long>