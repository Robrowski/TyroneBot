package bluetooth;

import java.util.Set;

import main.MainActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class BluetoothCommunications {
	private final static String APP_TAG = "AndroidBluetooth";
	private final static int REQUEST_ENABLE_BT = 29384; // Code for bluetooth
	private Set<BluetoothDevice> pairedDevices;

	// Create a BroadcastReceiver for ACTION_FOUND
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			// When discovery finds a device
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// Get the BluetoothDevice object from the Intent
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				pairedDevices.add(device);
			}
		}
	};

	/**
	 * Constructor
	 * 
	 */
	public BluetoothCommunications() {

	}

	/**
	 * Should show a different view when this fails
	 * 
	 * @return
	 */
	public boolean initialize(Context c) {
		// Set up Bluetooth
		BluetoothAdapter mBA = BluetoothAdapter.getDefaultAdapter();
		if (mBA == null) {
			// Device does not support Bluetooth
			Log.e(APP_TAG, "Bluetooth not supported on this device.");
			return false;
		}

		// Enable Bluetooth if it isn't already
		pairedDevices = mBA.getBondedDevices();
		if (!mBA.isEnabled()) {
			Intent enableBtIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			// / TODO: Make this not hacky so that I don't have to cast but can
			// still encapsulate out the bluetooth
			((MainActivity) c).startActivityForResult_public(enableBtIntent,
					REQUEST_ENABLE_BT);
			if (!mBA.isEnabled()) {
				return false;
			}
		}

		// If there are paired devices, print their names and MAC addressses
		if (pairedDevices.size() > 0) {
			for (BluetoothDevice device : pairedDevices) {
				Log.w(APP_TAG, device.getName() + ": " + device.getAddress());
			}
		}

		// Register the BroadcastReceiver
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		c.registerReceiver(mReceiver, filter); // Don't forget to unregister
												// during onDestroy
		// Ready to start looking for bluetooth devices

		return true;
	}

}
