package twittertest.bassem.com.twittertest.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import twittertest.bassem.com.twittertest.ActivityMain;
import twittertest.bassem.com.twittertest.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentUserFollowers extends Fragment {
    Button openUserFollowersButton;

    public FragmentUserFollowers() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_followers, container, false);
        openUserFollowersButton = (Button) view.findViewById(R.id.btn_openInfo);
        openUserFollowersButton.setOnClickListener(openUserFollowersFragment);
        return view;
    }

    View.OnClickListener openUserFollowersFragment = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ActivityMain parentActivity = (ActivityMain) getActivity();
            if (parentActivity != null)
                parentActivity.loadFollowerInformation();

        }
    };


}
