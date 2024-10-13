package com.example.lottery.lotteries.entities

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.Instant
import java.time.LocalDate
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")  // JSON 직렬화 시 포맷 지정
    val createdAt: Instant = Instant.now()
}