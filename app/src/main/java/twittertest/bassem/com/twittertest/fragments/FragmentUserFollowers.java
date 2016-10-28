package twittertest.bassem.com.twittertest.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;

import twittertest.bassem.com.twittertest.ActivityMain;
import twittertest.bassem.com.twittertest.Models.Follower;
import twittertest.bassem.com.twittertest.Models.GetUserFollowersResponse;
import twittertest.bassem.com.twittertest.R;
import twittertest.bassem.com.twittertest.Services.UserFollowersService;
import twittertest.bassem.com.twittertest.adapters.UserFollowersAdapter;
import twittertest.bassem.com.twittertest.helpers.Constants;
import twittertest.bassem.com.twittertest.helpers.DatabaseHelper;
import twittertest.bassem.com.twittertest.helpers.MyUtilities;
import twittertest.bassem.com.twittertest.helpers.TwitterHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentUserFollowers extends Fragment {
    private static final String SAVEDRESPONSE = "saved_response";
    private static final String SAVEDFOLLOWERS = "saved_followers";
    ArrayList<Follower> mFollowers;
    RecyclerView followersRecyclerView;
    SwipeRefreshLayout swipeContainer;
    ProgressBar scrollingProgressBar;
    int visibleItemCount;
    int totalItemCount;
    int pastVisiblesItems;
    private static final String RESPONSE_EXTRA = "response";
    public boolean infiniteScrollingLoading = false;
    GetUserFollowersReceiver mReceiver;
    GetUserFollowersResponse userFollowersResponse;
    LinearLayoutManager linearLayoutManager;
    UserFollowersAdapter mAdapter;
    final static int PAGESIZE = 45;
    Intent followersIntent;
    private onFollowersItemClickListener onFollowersItemClickListener = new onFollowersItemClickListener();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_followers, container, false);
        scrollingProgressBar = (ProgressBar) view.findViewById(R.id.prgrs_scrolling);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        followersRecyclerView = (RecyclerView) view.findViewById(R.id.rclr_followers);
        followersRecyclerView.setHasFixedSize(false);
        swipeContainer.setOnRefreshListener(swipeContainerOnRefreshListener);
        swipeContainer.setColorSchemeResources(R.color.ColorBlue,
                R.color.ColorGreen,
                R.color.ColorRed,
                R.color.ColorYellow);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mFollowers == null)
            mFollowers = new ArrayList<Follower>();
        if (MyUtilities.checkForInternet(getContext()) == false)
            showOfflineToast();
        mReceiver = new GetUserFollowersReceiver();
        IntentFilter filter = new IntentFilter(GetUserFollowersReceiver.PROCESS_RESPONSE);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        getContext().registerReceiver(mReceiver, filter);
        Log.e("on created", "true");

        followersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        linearLayoutManager = (LinearLayoutManager) followersRecyclerView.getLayoutManager();
        followersRecyclerView.addOnScrollListener(followersRecyclerViewOnScrollListener);
        mAdapter = new UserFollowersAdapter(mFollowers, getContext(), onFollowersItemClickListener);
        followersRecyclerView.setAdapter(mAdapter);
        if (savedInstanceState != null) {

            retainState(savedInstanceState);

        }
    }


    public void getFollowers() {

        if (mFollowers == null)
            mFollowers = new ArrayList<Follower>();

        followersIntent = new Intent(Intent.ACTION_SYNC, null, getActivity(), UserFollowersService.class);
        followersIntent.putExtra(Constants.USER_ID_EXTRA, TwitterHelper.GetCurrentUserId());
        followersIntent.putExtra(Constants.PAGESIZE_EXTRA, PAGESIZE);
        followersIntent.putExtra(Constants.CURRENTUSERSCOUNT_EXTRA, mFollowers.size());
        if (userFollowersResponse != null)
            followersIntent.putExtra(Constants.CURSOR_EXTRA, userFollowersResponse.getNext_cursor());
        else {
            showSwipeRefreshLoading();
            followersIntent.putExtra(Constants.CURSOR_EXTRA, "-1");
        }
        getContext().startService(followersIntent);
    }

    private SwipeRefreshLayout.OnRefreshListener swipeContainerOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            if (MyUtilities.checkForInternet(getContext()) == false) {
                showOfflineToast();
                swipeContainer.setRefreshing(false);
            } else {
                prepareRefreshFollowers();
            }
        }
    };

    private void prepareRefreshFollowers() {
        userFollowersResponse = null;
        mFollowers.clear();
        mAdapter.notifyDataSetChanged();
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        try {
            dbHelper.clearFollowerTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        visibleItemCount = 0;
        totalItemCount = 0;
        pastVisiblesItems = 0;
        getFollowers();
    }

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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (userFollowersResponse != null)
            outState.putParcelable(SAVEDRESPONSE, userFollowersResponse);
        if (mFollowers != null)
            if (mFollowers.size() > 0)
                outState.putParcelableArrayList(SAVEDFOLLOWERS, mFollowers);
    }

    @Override
    public void onStop() {
        if (mReceiver != null) {
            getContext().unregisterReceiver(mReceiver);
            mReceiver = null;
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if (mReceiver != null) {
            getContext().unregisterReceiver(mReceiver);
            mReceiver = null;
        }
        super.onDestroy();
    }
    @Override
    public void onResume() {
        // UserFollowersService.shouldContinue = true;

        super.onResume();

        if (mFollowers == null) {
            showSwipeRefreshLoading();
            getFollowers();
        } else {
            if (mFollowers.size() == 0) {
                showSwipeRefreshLoading();
                getFollowers();
            }
        }
    }

    private void retainState(Bundle savedInstanceState) {
        if (userFollowersResponse == null) {
            userFollowersResponse = savedInstanceState.getParcelable(SAVEDRESPONSE);
        }
        if (mFollowers != null) {
            if (mFollowers.size() == 0) {
                mFollowers = savedInstanceState.getParcelableArrayList(SAVEDFOLLOWERS);
            }
        }
        if (mAdapter != null) {
            if (mAdapter.getDataset().size() == 0) {
                mAdapter.setmDataset(mFollowers);
                mAdapter.notifyDataSetChanged();


            }
        }
    }

    public class GetUserFollowersReceiver extends BroadcastReceiver {

        public static final String PROCESS_RESPONSE = "twittertest.bassem.com.twittertest.intent.action.PROCESS_RESPONSE";

        @Override
        public void onReceive(Context context, Intent intent) {
            swipeContainer.setRefreshing(false);

            GetUserFollowersResponse res = (GetUserFollowersResponse) intent.getExtras().getParcelable(Constants.RESULT_EXTRA);
            if (res.getFollowers().size() > 0) {
                userFollowersResponse = (GetUserFollowersResponse) res;
                updateLayout();
            } else {
                Toast.makeText(getContext(), R.string.error_msg, Toast.LENGTH_SHORT).show();
                scrollingProgressBar.setVisibility(View.GONE);
                swipeContainer.setRefreshing(false);
                infiniteScrollingLoading = false;

            }
        }
    }

    void updateLayout() {
        if (userFollowersResponse != null && userFollowersResponse.getFollowers() != null) {
            for (int i = 0; i < userFollowersResponse.getFollowers().size(); i++) {
                //replace if exists
                mFollowers.add(userFollowersResponse.getFollowers().get(i));
            }
            mAdapter = new UserFollowersAdapter(mFollowers, getContext(), onFollowersItemClickListener);
            followersRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            if (pastVisiblesItems != 0)
                linearLayoutManager.scrollToPosition(pastVisiblesItems + 1);
            else
                linearLayoutManager.scrollToPosition(0);
            // followersRecyclerView.setVisibility(View.VISIBLE);
            swipeContainer.setRefreshing(false);
            infiniteScrollingLoading = false;
            scrollingProgressBar.setVisibility(View.GONE);
        }
    }

    private void showOfflineToast() {
        Toast.makeText(getContext(), R.string.no_internet_connection_getting_offlineData, Toast.LENGTH_SHORT).show();

    }

    public class onFollowersItemClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Follower follower = mAdapter.getItemByPosition(followersRecyclerView.getChildLayoutPosition(v));
            startUserInformation(follower);
        }
    }

    void showSwipeRefreshLoading() {
        swipeContainer.post(new Runnable() {
            @Override
            public void run() {
                swipeContainer.setRefreshing(true);
            }
        });
    }

    private void startUserInformation(Follower follower) {
        ActivityMain activityMain = (ActivityMain) getActivity();
        if (activityMain != null) {
            activityMain.loadFollowerInformation(follower);
        }
    }

}
