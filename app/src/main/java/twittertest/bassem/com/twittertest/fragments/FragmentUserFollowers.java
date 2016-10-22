package twittertest.bassem.com.twittertest.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

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
    ArrayList<Follower> mFollowers = new ArrayList<Follower>();
    RecyclerView followersRecyclerView;
    LinearLayoutManager linearLayoutManager;
    SwipeRefreshLayout swipeContainer;
    ProgressBar scrollingProgressBar;
    ProgressBar mainProgressBar;
    int visibleItemCount;
    int totalItemCount;
    int pastVisiblesItems;
    private static final String RESPONSE_EXTRA = "response";
    public boolean infiniteScrollingLoading = false;
    GetUserFollowersReceiver mReceiver = new GetUserFollowersReceiver(new Handler());
    GetUserFollowersResponse userFollowersResponse;
    UserFollowersAdapter mAdapter;
    final static int PAGESIZE = 50;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_followers, container, false);
        mainProgressBar = (ProgressBar) view.findViewById(R.id.prgrs_main);
        scrollingProgressBar = (ProgressBar) view.findViewById(R.id.prgrs_scrolling);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        followersRecyclerView = (RecyclerView) view.findViewById(R.id.rclr_followers);
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
        mReceiver.setReceiver(this);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        followersRecyclerView.setLayoutManager(linearLayoutManager);
        followersRecyclerView.addOnScrollListener(followersRecyclerViewOnScrollListener);
        mAdapter = new UserFollowersAdapter(mFollowers, getContext());
        followersRecyclerView.setAdapter(mAdapter);
        mainProgressBar.setVisibility(View.VISIBLE);
        getFollowers();
    }

    public void getFollowers() {
        Intent intent = new Intent(Intent.ACTION_SYNC, null, getContext(), UserFollowersService.class);
        intent.putExtra(Constants.RECEIVER_EXTRA, mReceiver);
        intent.putExtra(Constants.USER_ID_EXTRA, TwitterHelper.GetCurrentUserId());
        intent.putExtra(Constants.PAGESIZE_EXTRA, PAGESIZE);
        intent.putExtra(Constants.CURRENTUSERSCOUNT_EXTRA, mFollowers.size());
        if (userFollowersResponse != null)
            intent.putExtra(Constants.CURSOR_EXTRA, userFollowersResponse.getNext_cursor());
        else
            intent.putExtra(Constants.CURSOR_EXTRA, "-1");
        getContext().startService(intent);
    }

    private SwipeRefreshLayout.OnRefreshListener swipeContainerOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            swipeContainer.setRefreshing(false);
        }
    };

    RecyclerView.OnScrollListener followersRecyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (dy > 0) {
                visibleItemCount = linearLayoutManager.getChildCount();
                totalItemCount = linearLayoutManager.getItemCount();
                pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();
                if (!infiniteScrollingLoading) {
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        infiniteScrollingLoading = true;
                        if (userFollowersResponse != null && userFollowersResponse.getNext_cursor().equalsIgnoreCase("0")) {
                            return;
                        }
                        getFollowers();
                        scrollingProgressBar.setVisibility(View.VISIBLE);
                    }
                }
            }
        }

    };

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        if (resultCode == Constants.STATUS_FINISHED) {
            if (mainProgressBar.getVisibility() == View.VISIBLE)
                mainProgressBar.setVisibility(View.GONE);
            userFollowersResponse = resultData.getParcelable(Constants.RESULT_EXTRA);
            if (userFollowersResponse != null && userFollowersResponse.getFollowers() != null) {
                for (int i = 0; i < userFollowersResponse.getFollowers().size(); i++)
                {
                    //replace if exists
                    mFollowers.add(userFollowersResponse.getFollowers().get(i));
                }
            }
            mAdapter.notifyDataSetChanged();
            infiniteScrollingLoading = false;
            scrollingProgressBar.setVisibility(View.GONE);
        } else {
            if (resultCode == Constants.STATUS_ERROR) {
                String errorMsg = resultData.getString(Constants.ERROR_MSG);
                if (errorMsg != null)
                    Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getContext(), R.string.error_msg, Toast.LENGTH_SHORT).show();
                scrollingProgressBar.setVisibility(View.GONE);
                mainProgressBar.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
