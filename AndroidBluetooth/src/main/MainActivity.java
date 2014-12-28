package main;

import java.util.Locale;

import logging.Log;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import bluetooth.DeviceListActivity;

import com.example.androidbluetooth.R;
import com.example.settings.SettingsActivity;

// TODO abstract out the parts that let this scroll and stuff

public class MainActivity extends ActionBarActivity {
	private final static String APP_TAG = "AndroidBluetooth";
	private final static int num_pages = 4;
	private final static int page_mult = 1;

	// Intent request codes // TODO are these arbitrary?
	private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
	private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
	private static final int REQUEST_ENABLE_BT = 3;

	private BluetoothAdapter mBA;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
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

	/** Handles page swiping */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mBA = BluetoothAdapter.getDefaultAdapter();

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		// mViewPager.setCurrentItem(num_pages * page_mult / 2);
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).
			int new_pos = (position % num_pages); // + num_pages * 3;
			return PlaceholderFragment.newInstance(new_pos);
		}

		@Override
		public int getCount() {
			return num_pages * page_mult;
		}

		@Override
		public CharSequence getPageTitle(int position) {
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
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";
		private int page_num;

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber % num_pages);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		/**
		 * When creating, retrieve this instance's number from its arguments.
		 */
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			page_num = getArguments() != null ? getArguments().getInt(
					ARG_SECTION_NUMBER) : 1;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			Log.e(APP_TAG, "Section " + page_num);
			switch (page_num) {
			// TODO: This mapping of page numbers to sections/xml pages is dumb.
			case 0:
			default:
				Log.e(APP_TAG, "Making the squirt fragment");
				return inflater.inflate(R.layout.squirt_control, container,
						false);
			case 1:
			case 2:
			case 3:
				return inflater.inflate(R.layout.fragment_main, container,
						false);
			}

		}
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

}
