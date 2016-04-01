package com.mathiasluo.designer.presenter;

import com.mathiasluo.designer.model.IModel.UserModel;
import com.mathiasluo.designer.model.UserModelImpl;
import com.mathiasluo.designer.view.IView.IPersonFragment;
import com.mathiasluo.designer.view.fragment.PersonFragment;

/**
 * Created by MathiasLuo on 2016/3/21.
 */
public class PersonPresenter extends BasePresenter<PersonFragment> {

    private UserModel mUserModel;
    private IPersonFragment mPersonFragment;


    public PersonPresenter(IPersonFragment mPersonFragment) {
        this.mPersonFragment = mPersonFragment;
        mUserModel = UserModelImpl.getInstance();

    }
}
