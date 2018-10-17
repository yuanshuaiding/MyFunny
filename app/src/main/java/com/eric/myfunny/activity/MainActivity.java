package com.eric.myfunny.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.eric.android.view.ExpandableTextView;
import com.eric.android.view.letter_bar.LetterBar;
import com.eric.baselib.ioc.BindView;
import com.eric.baselib.ioc.NetCheck;
import com.eric.baselib.ioc.OnClicked;
import com.eric.baselib.ioc.ViewUtils;
import com.eric.myfunny.R;
import com.eric.myfunny.view.ColorTrackTextView;
import com.eric.myfunny.view.QQStepView;
import com.eric.myfunny.view.RiskRadarView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private List<Fragment> fragments = new ArrayList<>();

    @BindView(R.id.qq_step)
    private QQStepView qqStepView;
    @BindView(R.id.risk_radar)
    private RiskRadarView riskRadar;
    @BindView(R.id.expand_tv)
    private ExpandableTextView expandableTextView;
    @BindView(R.id.btn_click)
    private Button btn_click;
    @BindView(R.id.track_tv)
    private ColorTrackTextView track_tv;
    @BindView(R.id.letter_bar)
    private LetterBar letter_bar;
    @BindView(R.id.tv_letter)
    private TextView tv_letter;
    private float mSteps = 65;
    private boolean increase = true;

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
        //ViewCompat.setTranslationZ(btn_click, 10);
        ViewCompat.setElevation(btn_click, 100);
        tv_letter.setVisibility(View.GONE);
        qqStepView.setMaxSteps(300);
        qqStepView.setDuration(1000);
        qqStepView.updateSteps(mSteps);
        qqStepView.setOnClickListener(v -> {
            if (increase) {
                mSteps = mSteps + 10;
                track_tv.setDirection(ColorTrackTextView.DIRECT_LEFT_RIGHT);
            } else {
                mSteps = mSteps - 10;
                track_tv.setDirection(ColorTrackTextView.DIRECT_RIGTH_LEFT);
            }
            if (mSteps > 300) {
                mSteps = 300;
                increase = false;
            }
            if (mSteps < 0) {
                mSteps = 0;
                increase = true;
            }
            qqStepView.updateSteps(mSteps);

            //计算比例设置文字渐变色
            float rate = increase ? mSteps / 300f : (1 - mSteps / 300);
            track_tv.setProgress(rate);
        });

        riskRadar.setMaxPrograss(100);
        riskRadar.setPrograss(50);
        riskRadar.setLevel(1);

        expandableTextView.setText("物流快递费上来看大家法兰克福数量的空间发了发数量的咖啡浪费苏勒德咖啡类似的减肥啦是否类似的会计分录快递费索拉卡的附近湿漉漉的咖啡机睡了多考几分死了地方数量的减肥了深刻的缴费胜利大街方式法律思考的缴费数量的风景类似的父级类似的减肥啦父类螺蛳粉");
        letter_bar.setOnLetterChangeListener(new LetterBar.OnLetterChangeListner() {
            @Override
            public void onLetterChanged(String letter) {
                tv_letter.setVisibility(View.VISIBLE);
                tv_letter.setAlpha(1);
                tv_letter.setText(letter);
            }

            @Override
            public void onLetterChoosed(String letter) {
                tv_letter.animate().alpha(0).setDuration(500).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        tv_letter.setVisibility(View.GONE);
                    }
                });
            }
        });

        //获取schema
        Intent intent = getIntent();
        if (intent.getData() != null) {
            Uri uri = intent.getData();
            uri.getScheme();//获取scheme
            uri.getHost();//获取host
            uri.getAuthority();//获取authority
            String summaryId = uri.getQueryParameter("summaryId");//获取传递参数
            expandableTextView.setText(summaryId);
        }
    }

    @OnClicked(R.id.btn_click)
    private void changeText() {
        expandableTextView.setText(expandableTextView.getText() + " 我是后来添加的");
    }

    @OnClicked(R.id.btn_send_msg)
    private void sendMsg() {
        try {
            Uri uri = Uri.parse("smsto:" + "15050160446");
            Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
            intent.putExtra("sms_body", "bertadata.qixinbao://?summaryId=\"1234\"");
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClicked(R.id.btn_coordinator)
    private void goCoordinatorLayout() {
        CoordinatorLayoutDemoActivity.action(this);
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
