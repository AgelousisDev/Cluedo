package com.agelousis.cluedonotepad.main.timer

interface TimerListener {
    fun onTimeUpdate(time: String)
    fun onEveryMinute()
}