package com.saferme.obsidian.ble

import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import java.util.UUID
import kotlin.properties.Delegates

class GattService constructor(serviceUUIDString: String) {

    private var serviceUUID = UUID.fromString(serviceUUIDString)

    var gattService: BluetoothGattService by Delegates.notNull()

    private var characteristicV2: BluetoothGattCharacteristic

    /*
     * init a gatt service object that can be broadcast by default we advertise
     *  the same characteristic on all our service to send the uuid
     */
    init {
        gattService = BluetoothGattService(serviceUUID, BluetoothGattService.SERVICE_TYPE_PRIMARY)
        characteristicV2 = BluetoothGattCharacteristic(
            SafermeBleConstants.READ_CHAR,
            BluetoothGattCharacteristic.PROPERTY_READ,
            BluetoothGattCharacteristic.PERMISSION_READ
        )
        gattService.addCharacteristic(characteristicV2)
    }
}
