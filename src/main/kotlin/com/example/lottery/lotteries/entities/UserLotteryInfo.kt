package com.example.lottery.lotteries.entities

import javax.persistence.*

@Entity
@Table(name = "user_lottery_info")
class UserLotteryInfo(
    @Id
    val uid: Long = 0L
) {
    var totalScore: Int = 0
}