package twittertest.bassem.com.twittertest.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import twittertest.bassem.com.twittertest.Models.Follower;
import twittertest.bassem.com.twittertest.R;
import twittertest.bassem.com.twittertest.fragments.FragmentUserFollowers;

/**
 * Created by Bassem Samy on 10/21/2016.
 */

public class UserFollowersAdapter extends RecyclerView.Adapter<UserFollowersAdapter.ViewHolder> {
    private ArrayList<Follower> mDataset;
    private Context mContext;
    private FragmentUserFollowers.onFollowersItemClickListener mOnItemClickListener;

    public UserFollowersAdapter(ArrayList<Follower> dataset, Context context, FragmentUserFollowers.onFollowersItemClickListener onItemClickListener) {
        mContext = context;
        mDataset = dataset;
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_follower, parent, false);
        v.setOnClickListener(mOnItemClickListener);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.nameTextView.setText(mDataset.get(position).getName());
        holder.handleTextView.setText(mDataset.get(position).getScreenName());
        holder.bioTextView.setText(mDataset.get(position).getBio());
        if (mDataset.get(position).getProfileImageUrl() != null && mDataset.get(position).getProfileImageUrl().isEmpty() == false) {
            Glide.with(mContext).load(mDataset.get(position).getFinalProfileImageUrl()).
                    diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.profileImageView);
        } else {
            holder.profileImageView.setImageResource(android.R.color.transparent);
        }
    }

    @Override
    public int getItemCount() {
        if (mDataset != null)
            return mDataset.size();
        return 0;
    }

    public ArrayList<Follower> getDataset() {
        return mDataset;
    }

    public void setmDataset(ArrayList<Follower> followers) {
        mDataset = followers;
    }

    public Follower getItemByPosition(int position) {
        return mDataset.get(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImageView;
        TextView nameTextView;
        TextView handleTextView;
        TextView bioTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            profileImageView = (ImageView) itemView.findViewById(R.id.img_follower_profile);
            nameTextView = (TextView) itemView.findViewById(R.id.txt_follower_name);
            bioTextView = (TextView) itemView.findViewById(R.id.txt_follower_bio);
            handleTextView = (TextView) itemView.findViewById(R.id.txt_follower_handle);
        }
    }


}
