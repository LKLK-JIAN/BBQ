package com.example.benetech.bbq.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.benetech.bbq.base.mBaseActivity;

public abstract class BaseFragment extends Fragment {

    protected View mView;
    protected Bundle savedInstanceState;
    private boolean isPrepare;
    private boolean isVisible;
    private Boolean isFirstInit;
    protected mBaseActivity mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mContext = (mBaseActivity) context;
        } catch (Exception e) {
            throw new IllegalArgumentException("这个fragment的父activity必须继承BaseActivity");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
        mView = inflater.inflate(setFragmentLayoutRes(), null, false);
        initView();
        isPrepare = true;
        isVisible = true;
        isFirstInit = true;
        return mView;
    }

    protected abstract int setFragmentLayoutRes();

    protected abstract void initView();

}
