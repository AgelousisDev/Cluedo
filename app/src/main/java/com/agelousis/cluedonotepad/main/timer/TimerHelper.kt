package com.agelousis.cluedonotepad.main.timer

import android.os.Handler
import android.os.SystemClock

class TimerHelper(private val timerListener: TimerListener): Runnable {

    private var handler: Handler? = null
    private var startTime: Long = 0L

    init {
        startTime = SystemClock.uptimeMillis()
        handler = Handler()
        handler?.postDelayed(this, 0)
    }

    override fun run() {
        val millisSecondTime = SystemClock.uptimeMillis() - startTime
        var seconds = (millisSecondTime.toInt() / 1000)
        val minutes = seconds / 60
        seconds %= 60
        val milliSeconds = (millisSecondTime.toInt() % 1000)
        timerListener.onTimeUpdate(time = "${String.format("%02d", minutes)}:${String.format("%02d", seconds)}:${String.format("%03d", milliSeconds)}")
        if (minutes % 5 == 0)
            timerListener.onFiveMinutes()
        handler?.postDelayed(this, 0)
    }

}