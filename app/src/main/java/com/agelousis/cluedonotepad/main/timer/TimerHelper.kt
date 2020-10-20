package com.agelousis.cluedonotepad.main.timer

import android.os.Handler
import android.os.Looper
import android.os.SystemClock

class TimerHelper(private val timerListener: TimerListener): Runnable {

    private var handler: Handler? = null
    private var startTime: Long = 0L

    init {
        startTime = SystemClock.uptimeMillis()
        handler = Handler(Looper.getMainLooper())
        handler?.postDelayed(this, 0)
    }

    override fun run() {
        val millisSecondTime = SystemClock.uptimeMillis() - startTime
        var seconds = (millisSecondTime.toInt() / 1000)
        val minutes = seconds / 60
        seconds %= 60
        timerListener.onTimeUpdate(time = "${String.format("%02d", minutes)}:${String.format("%02d", seconds)}")
        if (minutes > 0 && minutes % 1 == 0 && seconds == 0)
            timerListener.onEveryMinute()
        handler?.postDelayed(this, 1000)
    }

}