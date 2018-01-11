package com.example.rayku.firebasetutorialone;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class AdapterSectionsPager extends FragmentPagerAdapter {

    private ArrayList<String> forumIDs, forumTitles;

    AdapterSectionsPager(FragmentManager fm, ArrayList<String> forumIDs, ArrayList<String> forumTitles) {
        super(fm);
        this.forumIDs = forumIDs;
        this.forumTitles = forumTitles;
    }

    @Override
    public Fragment getItem(int position) { return PlaceholderFragment.newInstance(); }

    @Override
    public int getCount() { return forumIDs.size(); }

    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment createdFragment = (Fragment) super.instantiateItem(container, position);

        Bundle args = new Bundle();
        args.putString("forumID", forumIDs.get(position));
        createdFragment.setArguments(args);

        return createdFragment;
    }

    @Override
    public CharSequence getPageTitle(int position) { return forumTitles.get(position); }

    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() { }

        public static Fragment newInstance() {
            return new FragmentForum();
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_forum, container, false);
        }
    }
}