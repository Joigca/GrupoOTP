package com.grupootp.Fragment.Fragment_Encuentranos;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jose Igualada
 *            joseigualda@gmail.com
 */

public class FragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

    List<Fragment> fragments;

    public FragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        this.fragments = new ArrayList<Fragment>();
    }

    public void addFragment(Fragment fragment) {
        this.fragments.add(fragment);

    }

    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }
}
