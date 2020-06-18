package com.saferme.obsidian.ble

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattServer
import android.bluetooth.BluetoothManager
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.bluetooth.le.BluetoothLeAdvertiser
import android.content.Context
import android.content.Context.BLUETOOTH_SERVICE
import android.content.Intent
import android.os.ParcelUuid
import android.util.Log
import java.util.ArrayList

/**
 * Saferme Llc
 */

class GattServer(private val mCtx: Context) {
    private var mBluetoothManager: BluetoothManager? = null
    private var mBluetoothAdapter: BluetoothAdapter? = null
    private var mBluetoothLeAdvertiser: BluetoothLeAdvertiser? = null
    private var mGattServer: BluetoothGattServer? = null
    private var mConnectedDevices: ArrayList<BluetoothDevice>? = null

    /*
     * Callback handles events from the framework describing
     * if we were successful in starting the advertisement requests.
     */
    private val mAdvertiseCallback = object : AdvertiseCallback() {
        override fun onStartSuccess(settingsInEffect: AdvertiseSettings) {
            Log.i(TAG, "Peripheral Advertise Started.")
        }

        override fun onStartFailure(errorCode: Int) {
            // todo: report error on raygun it is not integrated in obsidian yet
            Log.w(TAG, "Peripheral Advertise Failed: $errorCode")
        }
    }

    /* Callback handles all incoming requests from GATT clients.
     * From connections to read/write requests.
     */
    private val mGattServerCallback = GattServerCharacteristicCallBack(mGattServer)

    init {
        mConnectedDevices = ArrayList()
        mBluetoothManager = mCtx.getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        mBluetoothAdapter = mBluetoothManager!!.adapter
    }

    fun enableBLe() {
        /*
         * Make sure bluettoth is enabled and if possible ask the user to enable it.
         */
        if (mBluetoothAdapter == null || !mBluetoothAdapter!!.isEnabled) {
            // Bluetooth is disabled
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            mCtx.startActivity(enableBtIntent)
            return
        }
        /*
         * Check for advertising support.
         */
        if (!mBluetoothAdapter!!.isMultipleAdvertisementSupported) {
            // probably we won't need to take care of it because only few devices that are old enough to drop 5.0 and below
            return
        }

        //  If everything is okay then start
        initServer()
        startAdvertising()
    }

    /*
     * Create the GATT server instance, attaching all services and
     * characteristics that should be exposed
     */
    private fun initServer() {
        mBluetoothLeAdvertiser = mBluetoothAdapter!!.bluetoothLeAdvertiser
        mGattServer = mBluetoothManager!!.openGattServer(mCtx, mGattServerCallback)
        mGattServer!!.addService(GattService(serviceUUIDString = SafermeBleConstants.SAFERME_SERVICE.toString()).gattService)
        // todo: raplace with thor uuid solution
        mGattServer!!.addService(GattService(serviceUUIDString = SafermeBleConstants.SAFERME_USER_ID_SERVICE_PATTERN).gattService)
    }

    /*
     * Initialize the advertiser
     */
    private fun startAdvertising() {
        if (mBluetoothLeAdvertiser == null) return

        val settings = AdvertiseSettings.Builder()
            .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED)
            .setConnectable(true)
            .setTimeout(0) // this setup an infinite broadcast
            .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM)
            .build()

        val data = AdvertiseData.Builder()
            .setIncludeDeviceName(true)
            .addServiceUuid(ParcelUuid(SafermeBleConstants.SAFERME_SERVICE))
            .build()

        mBluetoothLeAdvertiser!!.startAdvertising(settings, data, mAdvertiseCallback)
    }

    /*
     * Terminate the server and any callback
     */
    public fun shutdownServer() {

        if (mGattServer == null) return

        mGattServer!!.close()
    }

    companion object {
        private val TAG = "SafermeBLeServer"

        protected val hexArray = "0123456789ABCDEF".toCharArray()

        // Helper function converts byte array to hex string
        // for priting
        fun ByteArray.toHexString() = joinToString("") { (0xFF and it.toInt()).toString(16).padStart(2, '0') }

        // Helper function converts hex string into
        // byte array
        fun hexStringToByteArray(s: String): ByteArray {
            val len = s.length
            val data = ByteArray(len / 2)
            var i = 0
            while (i < len) {
                data[i / 2] =
                    ((Character.digit(s[i], 16) shl 4) + Character.digit(s[i + 1], 16)).toByte()
                i += 2
            }
            return data
        }
    }
}
