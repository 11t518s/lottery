package com.example.lottery.lotteries.entities

import com.vladmihalcea.hibernate.type.json.JsonStringType
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import java.time.Instant
import javax.persistence.*


@Entity
@Table(name = "user_lottery_draw_ticket")
@TypeDef(name = "json", typeClass = JsonStringType::class)
class UserLotteryDrawTicket(
    numbers: List<Int>,
    val round: Int,
    val uid: Long,

) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    // numbers 필드를 오름차순으로 정렬하여 저장
    @Type(type = "json")
    @Column(columnDefinition = "text", name = "numbers")
    val numbers: List<Int> = numbers.sorted()  // 정렬 후 저장

    @CreationTimestamp
    var createdAt: Instant = Instant.now()

    // 결과 정보
    var ranking: Int? = null  // 결과가 없을 때는 null
    var isReceiveReward: Boolean = false  // 보상 수령 여부
}