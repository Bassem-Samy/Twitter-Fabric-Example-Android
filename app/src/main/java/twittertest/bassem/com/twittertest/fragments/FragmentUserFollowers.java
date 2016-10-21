package twittertest.bassem.com.twittertest.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.os.ResultReceiver;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import twittertest.bassem.com.twittertest.ActivityMain;
import twittertest.bassem.com.twittertest.Models.Follower;
import twittertest.bassem.com.twittertest.Models.GetUserFollowersResponse;
import twittertest.bassem.com.twittertest.R;
import twittertest.bassem.com.twittertest.Services.UserFollowersService;
import twittertest.bassem.com.twittertest.adapters.UserFollowersAdapter;
import twittertest.bassem.com.twittertest.helpers.Constants;
import twittertest.bassem.com.twittertest.helpers.TwitterHelper;
import twittertest.bassem.com.twittertest.receivers.GetUserFollowersReceiver;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentUserFollowers extends Fragment implements GetUserFollowersReceiver.Receiver {
    private static final String RESPONSE_EXTRA = "response";
    GetUserFollowersReceiver mReceiver = new GetUserFollowersReceiver(new Handler());
    GetUserFollowersResponse userFollowersResponse;
    UserFollowersAdapter mAdapter;
    ArrayList<Follower> mFollowers = new ArrayList<Follower>();
    RecyclerView followersRecyclerView;
    LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout swipeContainer;

    public FragmentUserFollowers() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_followers, container, false);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        followersRecyclerView=(RecyclerView)view.findViewById(R.id.rclr_followers);
        followersRecyclerView.setHasFixedSize(false);
        swipeContainer.setOnRefreshListener(swipeContainerOnRefreshListener);
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // set result rec
        mReceiver.setReceiver(this);
        linearLayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        followersRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter=new UserFollowersAdapter(mFollowers,getContext());
        followersRecyclerView.setAdapter(mAdapter);
        getFollowers();

    }

    public void getFollowers() {
        Intent intent = new Intent(Intent.ACTION_SYNC, null, getContext(), UserFollowersService.class);
        intent.putExtra(Constants.USER_ID_EXTRA, TwitterHelper.GetCurrentUserId());
        //intent.putExtra(Constants.CURSOR_EXTRA, "1500097983506504661");//-1);
        intent.putExtra(Constants.CURSOR_EXTRA, -1);
        intent.putExtra(Constants.PAGESIZE_EXTRA, 10);
        intent.putExtra(Constants.RECEIVER_EXTRA, mReceiver);
        intent.putExtra(Constants.CURRENTUSERSCOUNT_EXTRA, 1);
        getContext().startService(intent);
    }

    View.OnClickListener openUserFollowersFragment = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ActivityMain parentActivity = (ActivityMain) getActivity();
            if (parentActivity != null)
                parentActivity.loadFollowerInformation();

        }
    };
    private SwipeRefreshLayout.OnRefreshListener swipeContainerOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            swipeContainer.setRefreshing(false);
        }
    };


    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        if (resultCode == 1) {
            userFollowersResponse = resultData.getParcelable(Constants.RESULT_EXTRA);
            for(int i=0;i<userFollowersResponse.getFollowers().size();i++)
                mFollowers.add(userFollowersResponse.getFollowers().get(i));
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
