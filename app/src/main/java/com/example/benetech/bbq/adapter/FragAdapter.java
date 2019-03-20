package com.example.benetech.bbq.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.example.benetech.bbq.fragment.RecordFragment;
import com.example.benetech.bbq.fragment.TempFragment;

import java.util.List;

public class FragAdapter extends FragmentPagerAdapter {

    private final int PAGER_COUNT = 4;
    private RecordFragment mRecordFragment = null;
    private TempFragment mTempFragment = null;


    public FragAdapter(FragmentManager fm) {
        super(fm);
        mRecordFragment = new RecordFragment();
        mTempFragment = new TempFragment();
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Object instantiateItem(ViewGroup vg, int position) {
        return null;
        //return super.instantiateItem(vg, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        System.out.println("position Destory" + position);
        super.destroyItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 1:
                fragment = mRecordFragment;
                break;
            case 2:
                fragment = mTempFragment;
                break;
        }
        return fragment;
    }
}



