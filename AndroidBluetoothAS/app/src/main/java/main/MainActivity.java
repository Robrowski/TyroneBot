package main;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import com.example.androidbluetooth.R;
import com.example.settings.SettingsActivity;

import java.util.Locale;

import activity.ScrollableActivity;
import bluetooth.DeviceListActivity;
import logging.Log;
import logging.view.LogSupportFragment;

public class MainActivity extends ScrollableActivity {
	private final static String APP_TAG = "AndroidBluetooth";
	private final static int num_pages = 4;

	// Intent request codes // TODO are these arbitrary?
	private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
	private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
	private static final int REQUEST_ENABLE_BT = 3;

	private BluetoothAdapter mBA;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.w(APP_TAG, "Handling menu item..");
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.action_settings: {
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			return true;
		}
		case R.id.secure_connect_scan: {
			// Launch the DeviceListActivity to see devices and do scan
			Intent serverIntent = new Intent(this, DeviceListActivity.class);
			startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
			return true;
		}
		case R.id.insecure_connect_scan: {
			// Launch the DeviceListActivity to see devices and do scan
			Intent serverIntent = new Intent(this, DeviceListActivity.class);
			startActivityForResult(serverIntent,
					REQUEST_CONNECT_DEVICE_INSECURE);
			return true;
		}
		case R.id.discoverable: {
			// Ensure this device is discoverable by others
			ensureDiscoverable();
			return true;
		}
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        mBA = BluetoothAdapter.getDefaultAdapter();
		// On screen logging via a fragment with a TextView.
		LogSupportFragment logFragment = (LogSupportFragment) getSupportFragmentManager()
				.findFragmentById(R.id.log_fragment2);
		Log.setLast(logFragment.getLogView());
	}

	/**
	 * Makes this device discoverable.
	 */
	private void ensureDiscoverable() {
		if (mBA.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			Intent discoverableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(
					BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			startActivity(discoverableIntent);
		}
	}

	@Override
	public int getPageCount() {
		return num_pages;
	}

	@Override
	public CharSequence getPageTitles(int position) {
		// TODO: What is this for?
		Locale l = Locale.getDefault();
		switch (position % num_pages) {
		case 0:
			return getString(R.string.title_section1).toUpperCase(l);
		case 1:
			return getString(R.string.title_section2).toUpperCase(l);
		case 2:
			return getString(R.string.title_section3).toUpperCase(l);
		case 3:
			return getString(R.string.title_section4).toUpperCase(l);
		}
		return null;
	}

	@Override
	protected int getPagerID() {
		return R.id.pager;
	}

	@Override
	protected int getMainLayoutId() {
		return R.layout.activity_main;
	}

	@Override
	protected Fragment getFragments(int position) {
		// switch (position) {
		// case 1:
		// return (Fragment) new LogSupportFragment();
		//
		// }
		return new PlaceholderFragment(position);

	}


    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.w(APP_TAG, "Activity resumed!");
    }
}
