package com.example.lottery.lotteries.entities

import org.hibernate.annotations.CreationTimestamp
import java.time.Instant
import javax.persistence.*

@Entity
@Table(name = "lottery_mission")
class LotteryMission(
    val name: String = "",
    val maxCoinAmount: Int = 0,
    val dailyRepeatableCount: Int = 0,

    @Enumerated(EnumType.STRING)
    val type: Type,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    var enabled: Boolean = false

    @CreationTimestamp
    val createdAt: Instant = Instant.now()

    enum class Type {
        ATTENDANCE_CHECK, // 출석 체크
        KAKAO_SHARE, // 카카오 공유
        VISIT_COUPANG, // 쿠팡 파트너스
        VIDEO_REWARDED_AD_REWARD, // 앱로빈 동영상 광고
    }
}