package com.saferme.testble

import android.Manifest
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.saferme.obsidian.ble.BleScanner
import com.saferme.obsidian.ble.GattServer

import kotlinx.android.synthetic.main.activity_main.*
import pub.devrel.easypermissions.EasyPermissions
import android.widget.Toast
import android.Manifest.permission
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import androidx.annotation.NonNull





class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks  {

    // the server data
    private val server by lazy {
        GattServer(this@MainActivity)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        //use this line to test scanning

        val perms =
            arrayOf<String>(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE)
        if (EasyPermissions.hasPermissions(this, *perms)) {
            //  server.enableBLe()
            BleScanner(this@MainActivity).scan()
        } else {
            EasyPermissions.requestPermissions(
                this, "We need permissions because this and that",
                123, *perms
            )
        }
        // use this line to test advertising
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onStop() {
        // use this to stop advertising
        server.shutdownServer()
        super.onStop()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
      //  server.enableBLe()
        // uncomment to s scan instead
        BleScanner(this@MainActivity).scan()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {

    }
}

