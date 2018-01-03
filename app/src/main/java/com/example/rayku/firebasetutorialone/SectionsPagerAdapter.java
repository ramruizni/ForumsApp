package com.example.rayku.firebasetutorialone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private BlankFragment[] frags;

    SectionsPagerAdapter(FragmentManager fm, BlankFragment[] frags) {
        super(fm);
        this.frags = frags;
    }

    @Override
    public Fragment getItem(int position) { return PlaceholderFragment.newInstance(position + 1); }

    @Override
    public int getCount() { return 5; }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment createdFragment = (Fragment) super.instantiateItem(container, position);

        Bundle args = new Bundle();
        args.putInt("pos", position);

        switch(position){
            case 0:
                frags[0] = (BlankFragment) createdFragment;
                frags[0].setArguments(args);
                break;
            case 1:
                frags[1] = (BlankFragment) createdFragment;
                frags[1].setArguments(args);
                break;
            case 2:
                frags[2] = (BlankFragment) createdFragment;
                frags[2].setArguments(args);
                break;
            case 3:
                frags[3] = (BlankFragment) createdFragment;
                frags[3].setArguments(args);
                break;
            case 4:
                frags[4] = (BlankFragment) createdFragment;
                frags[4].setArguments(args);
                break;
        }

        return createdFragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Integer.toString(position);
    }

    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() { }

        public static Fragment newInstance(int sectionNumber) {
            switch (sectionNumber){
                case 1: return new BlankFragment();
                case 2: return new BlankFragment();
                case 3: return new BlankFragment();
                case 4: return new BlankFragment();
                case 5: return new BlankFragment();
            }
            return null;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_blank, container, false);
        }
    }
}