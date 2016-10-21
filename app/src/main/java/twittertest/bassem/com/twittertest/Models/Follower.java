package twittertest.bassem.com.twittertest.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mina Samy on 10/21/2016.
 */

public class Follower implements Parcelable {
    @SerializedName("id_str")
    private String id;

    @SerializedName("description")
    private String bio;

    @SerializedName("name")
    private String name;
    @SerializedName("screen_name")
    private String screenName;
    @SerializedName("profile_image_url")
    private String profileImageUrl;
    @SerializedName("profile_banner_url")
    private String bannerBackgroundUrl;

    protected Follower(Parcel in) {
        id = in.readString();
        bio = in.readString();
        name = in.readString();
        screenName = in.readString();
        profileImageUrl = in.readString();
        bannerBackgroundUrl = in.readString();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getBannerBackgroundUrl() {
        return bannerBackgroundUrl;
    }

    public void setBannerBackgroundUrl(String bannerBackgroundUrl) {
        this.bannerBackgroundUrl = bannerBackgroundUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Follower> CREATOR = new Creator<Follower>() {
        @Override
        public Follower createFromParcel(Parcel in) {
            return new Follower(in);
        }

        @Override
        public Follower[] newArray(int size) {
            return new Follower[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(bio);
        dest.writeString(name);
        dest.writeString(screenName);
        dest.writeString(profileImageUrl);
        dest.writeString(bannerBackgroundUrl);
    }
}
