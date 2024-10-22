package com.example.lottery.lotteries.entities

import com.vladmihalcea.hibernate.type.json.JsonStringType
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "lottery_round")
@TypeDef(name = "json", typeClass = JsonStringType::class)
class LotteryRound(
    numbers: List<Int> = listOf(),
    val round: Int,
    val bonus: Int,
) {
    @Id
    val id: Int = round

    @Type(type = "json")
    @Column(columnDefinition = "text", name = "numbers")
    val numbers: List<Int> = numbers.sorted()
}