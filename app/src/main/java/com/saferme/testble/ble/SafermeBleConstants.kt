package com.saferme.obsidian.ble

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothProfile
import java.util.UUID

object SafermeBleConstants {

    // Service UUID to expose our UART characteristics
    val SAFERME_SERVICE = UUID.fromString("d5db4ce5-ac62-4f98-befd-984b3b445b9a")
    val SCAN_DURATION: Long = 1 * 60 * 1000
    val SAFERME_USER_ID_SERVICE_PATTERN = "53F37113-0000-0000-0000-000000000000"
    val READ_CHAR = UUID.fromString("24fd719e-2008-4c9e-9ea2-e19402dc51e2")

    fun getStateDescription(state: Int): String {
        when (state) {
            BluetoothProfile.STATE_CONNECTED -> return "Connected"
            BluetoothProfile.STATE_CONNECTING -> return "Connecting"
            BluetoothProfile.STATE_DISCONNECTED -> return "Disconnected"
            BluetoothProfile.STATE_DISCONNECTING -> return "Disconnecting"
            else -> return "Unknown State $state"
        }
    }

    fun getStatusDescription(status: Int): String {
        when (status) {
            BluetoothGatt.GATT_SUCCESS -> return "SUCCESS"
            else -> return "Unknown Status $status"
        }
    }
}
