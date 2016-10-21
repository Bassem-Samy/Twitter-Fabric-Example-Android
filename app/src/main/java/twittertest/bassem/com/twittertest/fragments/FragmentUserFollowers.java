package twittertest.bassem.com.twittertest.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.os.ResultReceiver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import twittertest.bassem.com.twittertest.ActivityMain;
import twittertest.bassem.com.twittertest.Models.GetUserFollowersResponse;
import twittertest.bassem.com.twittertest.R;
import twittertest.bassem.com.twittertest.Services.UserFollowersService;
import twittertest.bassem.com.twittertest.helpers.Constants;
import twittertest.bassem.com.twittertest.helpers.TwitterHelper;
import twittertest.bassem.com.twittertest.receivers.GetUserFollowersReceiver;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentUserFollowers extends Fragment implements GetUserFollowersReceiver.Receiver {
    Button openUserFollowersButton;
    GetUserFollowersReceiver mReceiver = new GetUserFollowersReceiver(new Handler());
    GetUserFollowersResponse userFollowersResponse;

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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // set result rec
        mReceiver.setReceiver(this);
        getFollowers();
    }

    public void getFollowers() {
        Intent intent = new Intent(Intent.ACTION_SYNC, null, getContext(), UserFollowersService.class);
        intent.putExtra(Constants.USER_ID_EXTRA, TwitterHelper.GetCurrentUserId());
        intent.putExtra(Constants.CURSOR_EXTRA, "1500097983506504661");//-1);
        intent.putExtra(Constants.PAGESIZE_EXTRA, 10);
        intent.putExtra(Constants.RECEIVER_EXTRA, mReceiver);
        intent.putExtra(Constants.CURRENTUSERSCOUNT_EXTRA, 1);
        getContext().startService(intent);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        if (resultCode == 1) {
            userFollowersResponse = resultData.getParcelable(Constants.RESULT_EXTRA);
        }

    }
}
