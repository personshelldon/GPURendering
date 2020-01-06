package com.don11995.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ReceiverWrapper(private val mReceiver: Receiver) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        mReceiver.onReceive(context, intent)
    }
}
