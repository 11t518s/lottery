package com.example.lottery.lotteries.entities

import com.vladmihalcea.hibernate.type.json.JsonStringType
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import javax.persistence.*


@Entity
@Table(name = "user_lottery_draw_ticket")
@TypeDef(name = "json", typeClass = JsonStringType::class)
class UserLotteryDrawTicket(
    @Type(type = "json")
    @Column(columnDefinition = "text", name = "numbers")
    val numbers: List<Int>,
    val bonusNumber: Int,
    val round: Int,
    val uid: Long,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}