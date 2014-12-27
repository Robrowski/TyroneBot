package bluetooth;

import java.util.Set;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.util.Log;

public class BluetoothCommunications {
	private final static String APP_TAG = "AndroidBluetooth";
	private Set<BluetoothDevice> pairedDevices;
	private BluetoothAdapter mBA;

	/** Need to find this */
	private final static String arduinoMACAddress = "00:11:22:AA:BB:CC";

	/**
	 * Constructor
	 * 
	 */
	public BluetoothCommunications() {
		// Set up Bluetooth
		mBA = BluetoothAdapter.getDefaultAdapter();
		if (mBA == null) {
			// Device does not support Bluetooth
			Log.e(APP_TAG, "Bluetooth not supported on this device.");
			return;
		}

		// Get the already bonded devices
		pairedDevices = mBA.getBondedDevices();
		Log.d(APP_TAG, "Aquired bonded devices");
	}

}
