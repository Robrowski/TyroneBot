package main;

import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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

		this.i(APP_TAG, "Loading bluetooth...");
		// Set up Bluetooth
		mBA = BluetoothAdapter.getDefaultAdapter();
		if (mBA == null) {
			// Device does not support Bluetooth
			Log.e(APP_TAG, "Bluetooth not supported on this device.");
			return;
		}

		// Get the already bonded devices
		pairedDevices = mBA.getBondedDevices();
		this.i(APP_TAG, "Aquired bonded devices");

		enableBluetooth();

		// If there are paired devices, print their names and MAC addresses
		if (pairedDevices.size() > 0) {
			for (BluetoothDevice device : pairedDevices) {
				Log.w(APP_TAG, device.getName() + ": " + device.getAddress());
			}
		}

		// this.discoverDevices(this);

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
			this.i(APP_TAG, "Enabling bluetooth...");
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
				this.i(APP_TAG, "Bluetooth denied.");
				enableBluetooth();
				return;
			}
			this.i(APP_TAG, "Bluetooth enabled.");

			// Try to connect to TyroneBot here

			this.i(APP_TAG, "Bluetooth loaded and connected successfully!");

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
	 * Logs the given message and displays it on the screen
	 * 
	 * @param tag
	 * @param message
	 */
	private void i(String tag, String msg) {
		Log.i(tag, msg);
		// TODO: Stop replacing the entire layout
		TextView textView = new TextView(this); // create new text view to
		textView.setTextSize(25);
		textView.setText(msg);
		setContentView(textView); // Override current layout
	}

	/**
	 * If the desired device is not found, we need to work on finding it
	 * 
	 * TODO Need bluetooth admin for this... probably don't want to bother
	 * 
	 * @param c
	 * @return
	 */
	public boolean discoverDevices(Context c) {
		this.i(APP_TAG, "Searching for devices...");

		// Create a BroadcastReceiver for ACTION_FOUND
		final BroadcastReceiver mReceiver = new BroadcastReceiver() {
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

		// Register the BroadcastReceiver
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		c.registerReceiver(mReceiver, filter); // Don't forget to unregister
												// during onDestroy
		// Ready to start looking for bluetooth devices
		mBA.startDiscovery();

		// Don't want to do this for too long
		// Want to stop when the arduino is found...
		while (!this.foundArduino()) {

		}

		mBA.cancelDiscovery();
		return false;
	}

	/**
	 * Check the list of paired devices for the Arduino... also check to see if
	 * it is currently connected?
	 * 
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

}
