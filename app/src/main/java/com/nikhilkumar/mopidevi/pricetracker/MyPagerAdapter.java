package com.nikhilkumar.mopidevi.pricetracker;

import android.support.v4.app.*;

/**
 * Created by NIKHIL on 10-Mar-15.
 */

public class MyPagerAdapter extends FragmentPagerAdapter {

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return (position == 0)? "List" : "Add" ;
    }
    @Override
    public int getCount() {
        return 2;
    }
    @Override
    public Fragment getItem(int position) {

        return (position == 0)? new ListTab() : new AddTab() ;
    }

}

