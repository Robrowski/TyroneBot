package main;

import java.util.Set;

import logging.Log;
import logging.impl.LogWrapper;
import logging.impl.MessageOnlyLogFilter;
import logging.view.LogFragment;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;

import com.example.androidbluetooth.R;

//TODO pass bluetooth communications on to next stage. 
//// I SHOULD do 'BluetoothAdapter.getDefaultAdapter()" anywhere...

/**
 * View to show of the bluetooth setup
 * 
 * @author Robrowski
 *
 */
public class LoadingBluetoothActivity extends Activity {
	private Set<BluetoothDevice> pairedDevices;
	private BluetoothAdapter mBA;
	private final static int REQUEST_ENABLE_BT = 29384; // Code for bluetooth
	private final static String APP_TAG = "AndroidBluetooth";

	/** Need to find this */
	private final static String arduinoMACAddress = "00:11:22:AA:BB:CC";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading_bluetooth);
		initializeLogging();

		Log.i(APP_TAG, "Loading bluetooth...");
		// Set up Bluetooth
		mBA = BluetoothAdapter.getDefaultAdapter();
		if (mBA == null) {
			// Device does not support Bluetooth
			Log.e(APP_TAG, "Bluetooth not supported on this device.");
			return;
		}

		// Get the already bonded devices
		pairedDevices = mBA.getBondedDevices();
		Log.i(APP_TAG, "Aquired bonded devices");

		enableBluetooth();

		// If there are paired devices, print their names and MAC addresses
		if (pairedDevices.size() > 0) {
			for (BluetoothDevice device : pairedDevices) {
				Log.w(APP_TAG, device.getName() + ": " + device.getAddress());
			}
		}

		Log.i(APP_TAG, "waiting to finish bluetooth...");
	}

	/**
	 * Enable Bluetooth if it isn't already....
	 * 
	 * 
	 */
	protected void enableBluetooth() {
		// Enable Bluetooth if it isn't already
		if (!mBA.isEnabled()) {
			Log.i(APP_TAG, "Enabling bluetooth...");
			Intent BtI = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(BtI, REQUEST_ENABLE_BT);
		} else {
			// This is a hack in the off chance BT is already enabled
			onActivityResult(REQUEST_ENABLE_BT, RESULT_OK, null);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Only looking to handle Bluetooth enable results
		if (requestCode == REQUEST_ENABLE_BT) {
			if (resultCode == RESULT_CANCELED) {
				Log.i(APP_TAG, "Bluetooth denied.");
				enableBluetooth();
				return;
			}
			Log.i(APP_TAG, "Bluetooth enabled.");

			// Try to connect to TyroneBot here

			Log.i(APP_TAG, "Bluetooth loaded and connected successfully!");

			// Change the view over to MainActivity
			if (bluetoothReady()) {
				startActivity(new Intent(this, MainActivity.class));
				finish();
			}
		}
	}

	/**
	 * Return true if all aspects of the Bluetooth setup are ready
	 * 
	 * @return
	 */
	protected boolean bluetoothReady() {
		return !mBA.equals(null) && mBA.isEnabled() && this.foundArduino()
				&& arduinoConnectionEstablished();
	}

	/**
	 * Return true if a connection with the arduino is fully established
	 * 
	 * @return
	 */
	private boolean arduinoConnectionEstablished() {
		return true;
	}

	/**
	 * Check the list of paired devices for the Arduino... also check to see if
	 * it is currently connected?
	 * 
	 * @return
	 */
	public boolean foundArduino() {
		// TODO
		for (BluetoothDevice pD : pairedDevices) {
			if (pD.getAddress().equals(arduinoMACAddress)) {
				return true;
			}
		}
		return true; // TODO: Once I know this can work, change this to false
	}

	/** Create a chain of targets that will receive log data */
	public void initializeLogging() {
		// Wraps Android's native log framework.
		Log.setLast(new LogWrapper());

		// Filter strips out everything except the message text.
		Log.setLast(new MessageOnlyLogFilter());

		// On screen logging via a fragment with a TextView.
		LogFragment logFragment = (LogFragment) getFragmentManager()
				.findFragmentById(R.id.log_fragment);
		Log.setLast(logFragment.getLogView());

		Log.i(APP_TAG, "Ready");
	}

}
