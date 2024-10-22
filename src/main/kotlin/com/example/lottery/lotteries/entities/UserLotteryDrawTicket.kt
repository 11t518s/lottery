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

    @Type(type = "json")
    @Column(columnDefinition = "text", name = "numbers")
    val numbers: List<Int> = numbers.sorted()

    @CreationTimestamp
    var createdAt: Instant = Instant.now()

    var ranking: Int? = null
    var isReceiveReward: Boolean = false
}