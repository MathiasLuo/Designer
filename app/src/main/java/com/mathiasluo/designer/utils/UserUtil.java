package com.mathiasluo.designer.utils;

import com.mathiasluo.designer.bean.User;

/**
 * Created by MathiasLuo on 2016/3/6.
 */
public class UserUtil {

    public final static boolean enqual(User nUser, User mUser) {
        if (nUser == null || mUser == null) {
            return true;
        }
        return nUser.getBucketsCount() == mUser.getBucketsCount()
                && nUser.getAvatarUrl() == mUser.getAvatarUrl()
                && nUser.getLikesCount() == mUser.getLikesCount()
                && nUser.getLikesReceivedCount() == mUser.getLikesReceivedCount()
                && nUser.getLikesUrl() == mUser.getLikesUrl()
                && nUser.getLocation() == mUser.getLocation()
                && nUser.getName() == mUser.getName()
                && nUser.getHtmlUrl() == mUser.getHtmlUrl()
                && nUser.getBucketsUrl() == mUser.getBucketsUrl()
                && nUser.getFollowersCount() == mUser.getFollowersCount()
                && nUser.getFollowingsCount() == mUser.getFollowingsCount();
    }

}
