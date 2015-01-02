package main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidbluetooth.R;

/**
 * A single fragment in the scrollable view
 *
 */
public class PlaceholderFragment extends Fragment {
	static final String ARG_SECTION_NUMBER = "section_number";
	private static int[] layouts = { R.layout.squirt_control,
			R.layout.activity_main, R.layout.fragment_main,
			R.layout.fragment_main };
	private int pageNum;

	public PlaceholderFragment() {

	}

	/**
	 * 
	 * @param sectionNumber
	 * @return
	 */
	public PlaceholderFragment(int sectionNumber) {
		// PlaceholderFragment pf = newInstance();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		this.setArguments(args);
	}

	/**
	 * Sets the array of layouts used in onCreateView.
	 * 
	 * @param layouts
	 */
	public static void setLayouts(int[] layouts) {
		PlaceholderFragment.layouts = layouts;
	}

	/**
	 * When creating, retrieve this instance's number from its arguments.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		pageNum = getArguments() != null ? getArguments().getInt(
				ARG_SECTION_NUMBER) : 1;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(layouts[pageNum], container, false);
	}

	public int getPageNum() {
		return pageNum;
	}
}
