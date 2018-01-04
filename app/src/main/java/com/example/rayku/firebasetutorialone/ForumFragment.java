package com.example.rayku.firebasetutorialone;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
    ListView listView;
    View rootView;


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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, chatsArrayList);
        listView = rootView.findViewById(R.id.listView);

        TextView textView = rootView.findViewById(R.id.forumTitle);
        textView.setText(Integer.toString(args.getInt("position")));


        listView.setAdapter(adapter);

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
