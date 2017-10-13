package com.eric.myfunny.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.eric.baselib.ioc.NetCheck;
import com.eric.baselib.ioc.OnClicked;
import com.eric.baselib.ioc.ViewUtils;
import com.eric.myfunny.DemoFrag;
import com.eric.myfunny.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private List<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtils.inject(this);
        fragments.add(DemoFrag.newInstance());
        fragments.add(DemoFrag.newInstance());
        fragments.add(DemoFrag.newInstance());
        //viewpager.setAdapter(new MainViewPagerAdapter(getSupportFragmentManager()));
        onClick1(null);
    }

    @OnClicked(R.id.ll_section1)
    private void onClick1(View tv) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_result, fragments.get(0), "frag1").commitAllowingStateLoss();
    }

    @OnClicked(R.id.ll_section2)
    private void onClick2(View tv) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_result, fragments.get(1), "frag2").commitAllowingStateLoss();
    }

    @OnClicked(R.id.ll_section3)
    private void onClick3(View tv) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_result, fragments.get(2), "frag3").commitAllowingStateLoss();
    }

    @OnClicked(R.id.img_head)
    @NetCheck("omg，网络断了")
    private void showHeader(){
        toast("显示大图");
    }

    private class MainViewPagerAdapter extends FragmentPagerAdapter {

        public MainViewPagerAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}
