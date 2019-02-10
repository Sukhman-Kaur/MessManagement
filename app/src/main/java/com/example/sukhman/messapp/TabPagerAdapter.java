package com.example.sukhman.messapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabPagerAdapter extends FragmentPagerAdapter {
    public int tabCount;

    public TabPagerAdapter(FragmentManager fm, int noOfTabs) {
        super(fm);
        tabCount = noOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new First();
                return fragment;
            //return first.newInstance(position + 1, getIntent().getExtras().getString("btnId"));

            case 1:
                Second tab2 = new Second();
                return tab2;
            case 2:
                Third tab3 = new Third();
                return tab3;
            case 3:
                Fourth tab4 = new Fourth();
                return tab4;
            case 4:
                fragment = new MTech();
                return fragment;
            case 5:
                fragment = new Diploma();
                return fragment;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return tabCount;
    }
}

