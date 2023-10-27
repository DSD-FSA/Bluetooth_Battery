package arun.pkg.bluetoothbattery

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private lateinit var batteryTextView: TextView // новое

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        batteryTextView = findViewById(R.id.txt_battery) // новое

        val adapter = BluetoothAdapter.getDefaultAdapter()
        val iterator = adapter.bondedDevices.iterator()
        var status = ""
        while (iterator.hasNext()) {
            val device = iterator.next()
            updateBatteryLevel(device) // новое
        }
    }

    private fun updateBatteryLevel(device: BluetoothDevice) { // новое
        val battery = getBatteryLevelReflection(device)
        if (battery != -1) {
            val currentStatus = batteryTextView.text.toString()
            val updatedStatus = "$currentStatus\n${device.name} -\t $battery"
            runOnUiThread {
                batteryTextView.text = updatedStatus
            }
        }
    }

    private fun getBatteryLevelReflection(pairedDevice: BluetoothDevice?): Int {
        return pairedDevice?.let { bluetoothDevice ->
            (bluetoothDevice.javaClass.getMethod("getBatteryLevel")).invoke(pairedDevice) as Int
        } ?: -1
    }
}
