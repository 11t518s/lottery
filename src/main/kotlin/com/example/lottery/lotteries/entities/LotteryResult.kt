package com.example.lottery.lotteries.entities

import javax.persistence.*

@Entity
@Table(name = "lottery_result")
class LotteryResult(
    val round: Int,
    val ranking: Int,
    val uid: Long,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}