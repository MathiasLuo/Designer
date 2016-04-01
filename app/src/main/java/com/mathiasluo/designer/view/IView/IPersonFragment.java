package com.mathiasluo.designer.view.IView;

import com.mathiasluo.designer.bean.User;

/**
 * Created by mathiasluo on 16-3-23.
 */
 public  interface IPersonFragment {

    void showCurrentUser(User user);

    void showOtherUser(User user);

}
