package twittertest.bassem.com.twittertest.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import twittertest.bassem.com.twittertest.Models.Follower;
import twittertest.bassem.com.twittertest.R;
import twittertest.bassem.com.twittertest.helpers.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentFollowerInformation extends Fragment {
    TextView titleTextView;
    Follower mFollower;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_follower_information, container, false);
        titleTextView = (TextView) view.findViewById(R.id.txt_title);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getArguments()!=null)
            mFollower = getArguments().getParcelable(Constants.FOLLOWER_EXTRA);
        populateFollowerLayout();
    }

    private void populateFollowerLayout() {
        if (mFollower != null) {
            titleTextView.setText(mFollower.getName());
        }
    }

    public class FollowerInfoBroadcastReceiver extends BroadcastReceiver {

        public static final String PROCESS_USER_INFO_RESPONSE = "twittertest.bassem.com.twittertest.intent.action.PROCESS_USER_INFO_RESPONSE";

        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }
}
