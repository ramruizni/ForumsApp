package com.example.rayku.firebasetutorialone;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    Context context;
    ArrayList<String> userForums;

    SectionsPagerAdapter(Context context, FragmentManager fm, ArrayList<String> userForums) {
        super(fm);
        this.context = context;
        this.userForums = userForums;
    }

    @Override
    public Fragment getItem(int position) { return PlaceholderFragment.newInstance(); }

    @Override
    public int getCount() { return userForums.size(); }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment createdFragment = (Fragment) super.instantiateItem(container, position);

        Bundle args = new Bundle();
        args.putString("forumTitle", userForums.get(position));
        createdFragment.setArguments(args);

        return createdFragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return userForums.get(position);
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