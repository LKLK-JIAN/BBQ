package com.example.benetech.bbq.channel;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.benetech.bbq.R;
import com.example.benetech.bbq.adapter.MyViewPagerAdapter;
import com.example.benetech.bbq.view.MyViewPager;

import java.util.ArrayList;
import java.util.List;

public class FragmentContent extends Fragment {

    private View v;
    private MyViewPager myvp_fragment_content;
    private MyViewPagerAdapter pagerAdapter;
    private List<Fragment> fragments = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_content, null);
        myvp_fragment_content=v.findViewById(R.id.myvp_fragment_content);
        fragments = new ArrayList<Fragment>();
        pagerAdapter = new MyViewPagerAdapter(getChildFragmentManager(),
                fragments);
        myvp_fragment_content.setOffscreenPageLimit(2);
        myvp_fragment_content.setAdapter(pagerAdapter);
        fragments.add(new ChanneloneFragment());
        fragments.add(new ChanneltwoFragment());
//        fragments.add(new ChannelthreeFragment());
//        fragments.add(new ChannelfourFragment());
        pagerAdapter.notifyDataSetChanged();
        setCurrentPage(0);
        return v;
    }

    public void setCurrentPage(int pageIndex) {
        myvp_fragment_content.setCurrentItem(pageIndex);
    }
}
