package twittertest.bassem.com.twittertest.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Bassem Samy on 10/21/2016.
 */

public class GetUserFollowersResponse implements Parcelable {
    private String next_cursor;
    private String previous_cursor;

    @SerializedName("users")
    private ArrayList<Follower> followers;

    protected GetUserFollowersResponse(Parcel in) {
        next_cursor = in.readString();
        previous_cursor = in.readString();
        in.readTypedList(followers, Follower.CREATOR);
    }

    public ArrayList<Follower> getFollowers() {
        return followers;
    }

    public void setFollowers(ArrayList<Follower> followers) {
        this.followers = followers;
    }

    public String getNext_cursor() {
        return next_cursor;
    }

    public void setNext_cursor(String next_cursor) {
        this.next_cursor = next_cursor;
    }

    public String getPrevious_cursor() {
        return previous_cursor;
    }

    public void setPrevious_cursor(String previous_cursor) {
        this.previous_cursor = previous_cursor;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GetUserFollowersResponse> CREATOR = new Creator<GetUserFollowersResponse>() {
        @Override
        public GetUserFollowersResponse createFromParcel(Parcel in) {
            return new GetUserFollowersResponse(in);
        }

        @Override
        public GetUserFollowersResponse[] newArray(int size) {
            return new GetUserFollowersResponse[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(next_cursor);
        dest.writeString(previous_cursor);
        dest.writeTypedList(getFollowers());
    }

}
