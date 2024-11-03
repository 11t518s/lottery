package com.example.lottery.lotteries.domain

class LotteryNumbers(numbers: Set<Int>) {
    val numbers: Set<Int> = numbers.sorted().toSet()

    init {
        require(numbers.size == 6) { "Numbers must contain exactly 6 unique values." }
        require(numbers.all { it in 1..45 }) { "All numbers must be between 1 and 45." }
    }
}
