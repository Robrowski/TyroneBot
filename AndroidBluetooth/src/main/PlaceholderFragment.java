package main;

import logging.Log;
import activity.AbstractPlaceholderFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidbluetooth.R;

/**
 * Implementation of an AbstractPlaceholderFragment
 * 
 * @author Robrowski
 *
 */
public class PlaceholderFragment extends AbstractPlaceholderFragment {
	private final static String APP_TAG = "AndroidBluetooth";

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static PlaceholderFragment newInstance(int sectionNumber) {
		PlaceholderFragment fragment = new PlaceholderFragment();
		bundleArguments(sectionNumber, fragment);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.e(APP_TAG, "Section " + getPageNum());
		switch (getPageNum()) {
		// TODO: This mapping of page numbers to sections/xml pages is dumb.
		case 0:
			Log.e(APP_TAG, "Making the squirt fragment");
			return inflater.inflate(R.layout.squirt_control, container, false);
		default:
			return inflater.inflate(R.layout.fragment_main, container, false);
		}
	}
}
