package com.eric.myfunny.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.eric.baselib.ioc.BindView;
import com.eric.baselib.ioc.NetCheck;
import com.eric.baselib.ioc.OnClicked;
import com.eric.baselib.ioc.ViewUtils;
import com.eric.myfunny.R;
import com.eric.myfunny.view.ExpandableTextView;
import com.eric.myfunny.view.QQStepView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private List<Fragment> fragments = new ArrayList<>();

    @BindView(R.id.qq_step)
    private QQStepView qqStepView;
    @BindView(R.id.expand_tv)
    private ExpandableTextView expandableTextView;
    private float mSteps = 65;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_myview);
        ViewUtils.inject(this);
//        fragments.add(DemoFrag.newInstance());
//        fragments.add(DemoFrag.newInstance());
//        fragments.add(DemoFrag.newInstance());
//        //viewpager.setAdapter(new MainViewPagerAdapter(getSupportFragmentManager()));
//        onClick1(null);

        qqStepView.setMaxSteps(300);
        qqStepView.setDuration(1000);
        qqStepView.updateSteps(mSteps);
        qqStepView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSteps = mSteps + 10;
                if (mSteps > 300)
                    mSteps = 300;
                qqStepView.updateSteps(mSteps);
            }
        });
    }

    @OnClicked(R.id.btn_click)
    private void changeText(){
        expandableTextView.setText(expandableTextView.getText()+" 我是后来添加的");
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
    private void showHeader() {
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
