package com.saferme.obsidian.ble

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.os.Handler
import android.os.ParcelUuid
import android.util.Log

class BleScanner(context: Context) {
    val bluetoothAdapter: BluetoothAdapter
    val bluetoothManager: BluetoothManager
    val scanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            if (isSafermeservice(result.scanRecord?.serviceUuids)) {
                Log.i("saferme Blueetooth", "Remote device name: " + result.getDevice().getName() + "\n rssi:" + result.txPower)
            }
        }
    }

    /*
     * initialize the Bluetooth manager and creates the adapter to scan
     */
    init {
        bluetoothManager =
            context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
    }

    /*
     *  Scan for nearby devices that has the saferme service for the a limited time.
     */
    fun scan() {
        bluetoothAdapter.bluetoothLeScanner.startScan(scanCallback)
        Handler().postDelayed({ bluetoothAdapter.bluetoothLeScanner.stopScan(scanCallback) }, SafermeBleConstants.SCAN_DURATION)
    }

    /*
     * Validate if a device is advertising the services we are looking after.
     */
    private fun isSafermeservice(uuids: List<ParcelUuid>?): Boolean {
        if (uuids == null) return false
        for (uuid in uuids) {
            if (uuid.uuid.equals(SafermeBleConstants.SAFERME_SERVICE)) return true
        }
        return false
    }
}
