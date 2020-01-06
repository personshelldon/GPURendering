package com.don11995.receivers

import android.content.Context
import android.content.Intent

interface Receiver {
    fun onReceive(context: Context, intent: Intent)
}
