package activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;

// TODO Condsider what logging can be put in here

/**
 * This abstract class encapsulates all the difficulties of implementing a
 * scrollable activity. To get this functionality, simply extend this class and
 * implement the various abstract methods.
 * 
 * The main purpose of this class is to hide the multiple internal classes.
 * 
 * @author Robrowski
 *
 */
public abstract class ScrollableActivity extends ActionBarActivity {

	/** Handles page swiping */
	protected SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	protected ViewPager mViewPager;

	/**
	 * Gets the number of pages in held in the scrollable activity
	 * 
	 * @return
	 */
	abstract public int getPageCount();

	/**
	 * Gets page titles by position. This is used by the SectionsPagerAdapter
	 * 
	 * @param position
	 * @return
	 */
	abstract public CharSequence getPageTitles(int position);

	/**
	 * Get the resource ID for the pager used here
	 * 
	 * @return
	 */
	abstract protected int getPagerID();

	/**
	 * Getter for the main layout ID used in this framework
	 * 
	 * @return
	 */
	abstract protected int getMainLayoutId();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getMainLayoutId());

		// Create the adapter that will return a fragment for each of the
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(getPagerID());
		mViewPager.setAdapter(mSectionsPagerAdapter);
	}

	/**
	 * Get a new instance of a "PlaceholderFragment"
	 * 
	 * @param position
	 * @return
	 */
	abstract protected Fragment getFragments(int position);

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
			return getFragments(position);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return getPageTitles(position);
		}

		@Override
		public int getCount() {
			return getPageCount();
		}
	}

}
