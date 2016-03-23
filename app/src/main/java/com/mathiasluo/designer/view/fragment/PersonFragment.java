package com.mathiasluo.designer.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.mathiasluo.designer.presenter.PersonPresenter;

/**
 * Created by MathiasLuo on 2016/3/21.
 */
public class PersonFragment extends BaseFragment<PersonFragment, PersonPresenter> {


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected PersonPresenter creatPresenter() {
        return new PersonPresenter();
    }
}
