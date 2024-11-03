package com.example.lottery.lotteries.entities

import java.time.LocalDate
import java.time.ZoneId
import javax.persistence.*

@Entity
@Table(name = "user_lottery_mission")
class UserLotteryMission(
    val missionId: Long,
    val amount: Int,
    val uid: Long,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    val createdAt: LocalDate = LocalDate.now(ZoneId.of("Asia/Seoul"))
}