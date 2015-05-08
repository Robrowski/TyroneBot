package activity.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Locale;

import control.IControlFragment;
import fragment.EchoFragment;
import fragment.MeasuringFragment;

/**
 * Created by Dabrowski on 5/8/2015.
 */
public class ControlPagerAdapter extends FragmentPagerAdapter {
    private static final String TAG = "ControlPagerAdapter";

    private int pages;
    private IControlFragment mFrags[];

    public ControlPagerAdapter(FragmentManager fm /** stuff to specify a configuration of fragments */) {
        super(fm);

        pages = 2;
        mFrags = new IControlFragment[pages];
        mFrags[0] = EchoFragment.newInstance(); // TODO cheating by instantiating here
        mFrags[1] = MeasuringFragment.newInstance();


    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        return (Fragment) mFrags[position];
    }

    @Override
    public int getCount() {
        return pages;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        return mFrags[position].getPageTitle().toLowerCase(l);
    }
}
