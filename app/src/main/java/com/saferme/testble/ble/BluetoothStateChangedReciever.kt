package com.saferme.obsidian.ble

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BluetoothStateChangedReciever : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent!!.action
        if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
            if (intent!!.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)
                == BluetoothAdapter.STATE_ON
            ) {
                GattServer(mCtx = context!!).enableBLe()
            } else if (intent!!.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)
                == BluetoothAdapter.STATE_OFF
            ) {
                GattServer(mCtx = context!!).shutdownServer()
            }
        }
    }
}
