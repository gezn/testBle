package com.saferme.obsidian.ble

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattServer
import android.bluetooth.BluetoothGattServerCallback
import android.util.Log

/**
 * call back from opening a gatt server to  advertise our service
 */
class GattServerCharacteristicCallBack(val mGattServer: BluetoothGattServer?) : BluetoothGattServerCallback() {

    val TAG = "GattSrvrCharCallBack"

    /**
     * called when a blueetooth device connected to us to read the uuid
     */
    override fun onConnectionStateChange(device: BluetoothDevice, status: Int, newState: Int) {
        super.onConnectionStateChange(device, status, newState)
        Log.i(
            TAG, "connection state change:  " +
                    SafermeBleConstants.getStatusDescription(status) +
                    " " +
                    SafermeBleConstants.getStateDescription(newState)
        )
    }

    /**
     *  called when trying to read the uuid of the device. it sends the uuid of the device back
     */
    override fun onCharacteristicReadRequest(
        device: BluetoothDevice,
        requestId: Int,
        offset: Int,
        characteristic: BluetoothGattCharacteristic
    ) {
        super.onCharacteristicReadRequest(device, requestId, offset, characteristic)
        Log.d(TAG, "Read called" + characteristic.uuid.toString())

        if (SafermeBleConstants.READ_CHAR.equals(characteristic.uuid)) {
            mGattServer!!.sendResponse(
                device,
                requestId,
                BluetoothGatt.GATT_SUCCESS,
                0,
                SafermeBleConstants.SAFERME_USER_ID_SERVICE_PATTERN.toByteArray() // todo: return correct uuid if still need on ios
            )
        }
    }
}
