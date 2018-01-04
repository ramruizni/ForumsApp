package com.example.rayku.firebasetutorialone;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ForumFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    ArrayList<String> chatsArrayList;
    View rootView;
    ListView listView;

    RecyclerView recyclerView;
    public ForumFragment(){ }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatsArrayList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            chatsArrayList.add("A forum title omfg");
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_forum, container, false);
        Bundle args = getArguments();


        /*
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, chatsArrayList);
        listView = rootView.findViewById(R.id.listView);
        TextView textView = rootView.findViewById(R.id.forumTitle);
        textView.setText(Integer.toString(args.getInt("pos")));
        listView.setAdapter(adapter);
        */

        recyclerView = (RecyclerView) rootView.findViewById(R.id.listView);
        recyclerView.setAdapter(LargeAdapter.newInstance(getContext()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));



        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else { throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener { }
}
