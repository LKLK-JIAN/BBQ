package com.example.benetech.bbq.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * 主页适配器
 *
 * @author Administrator
 *
 */
public class MyViewPagerAdapter extends FragmentPagerAdapter {
	private List<Fragment> fragments;

	public MyViewPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	public MyViewPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
		super(fm);
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int arg0) {
		return fragments.get(arg0);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}
}