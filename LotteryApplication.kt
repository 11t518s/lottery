package com.example.lottery

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(
	basePackages = [
		"com.example.lottery",
	]
)
class LotteryApplication

fun main(args: Array<String>) {
	runApplication<LotteryApplication>(*args)
}
