package com.example.lottery.lotteries.launcher

import org.springframework.batch.core.Job
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.stereotype.Component

@Component
class LotteryJobLauncher(
    private val jobLauncher: JobLauncher,
    private val lotteryJob: Job
) {

    fun runLotteryJob() {
        jobLauncher.run(lotteryJob, org.springframework.batch.core.JobParameters())
    }
}
