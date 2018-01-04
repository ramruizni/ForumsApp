package com.example.rayku.firebasetutorialone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private ForumFragment[] frags;

    SectionsPagerAdapter(FragmentManager fm, ForumFragment[] frags) {
        super(fm);
        this.frags = frags;
    }

    @Override
    public Fragment getItem(int position) { return PlaceholderFragment.newInstance(); }

    @Override
    public int getCount() { return frags.length; }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment createdFragment = (Fragment) super.instantiateItem(container, position);

        Bundle args = new Bundle();
        args.putInt("pos", position);

        frags[position] = (ForumFragment) createdFragment;
        frags[position].setArguments(args);

        return createdFragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Integer.toString(position);
    }

    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() { }

        public static Fragment newInstance() {
            return new ForumFragment();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_forum, container, false);
        }
    }
}