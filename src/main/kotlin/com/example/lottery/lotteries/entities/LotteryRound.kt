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

    // numbers 필드를 오름차순으로 정렬하여 저장
    @Type(type = "json")
    @Column(columnDefinition = "text", name = "numbers")
    val numbers: List<Int> = numbers.sorted()  // 정렬 후 저장
}