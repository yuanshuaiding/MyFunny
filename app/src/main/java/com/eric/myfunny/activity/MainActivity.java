package com.eric.myfunny.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.eric.baselib.ioc.BindView;
import com.eric.baselib.ioc.ViewUtils;
import com.eric.myfunny.R;

public class MainActivity extends BaseActivity {

    @BindView(R.id.tv)
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtils.inject(this);

        mTextView.setText("Binded by Annotation");
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("哈哈");
            }
        });
    }
}
