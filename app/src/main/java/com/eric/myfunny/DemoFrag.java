package com.eric.myfunny;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eric.baselib.ioc.BindView;
import com.eric.baselib.ioc.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * author : Eric
 * e-mail : yuanshuai@bertadata.com
 * time   : 2017/10/12
 * desc   : 测试Fragment
 * version: 1.0
 */

public class DemoFrag extends Fragment {
    private Context mContext;
    @BindView(R.id.recyclerView)
    private RecyclerView mRecyclerView;
    private List<String> mockData = new ArrayList<>();

    public static DemoFrag newInstance() {
        return new DemoFrag();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_demo, container, false);
        ViewUtils.inject(view, this);
        for (int i = 0; i < 100; i++) {
            mockData.add(getTag()+"--> mock data :" + i);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(new MyAdapter());

    }

    private class MyAdapter extends RecyclerView.Adapter<VH> {
        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_frag_demo, parent, false);
            return new VH(view);
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {
            holder.tv.setText(mockData.get(position));
        }

        @Override
        public int getItemCount() {
            return mockData.size();
        }
    }

    private static class VH extends RecyclerView.ViewHolder {
        @BindView(R.id.tv)
        TextView tv;

        public VH(View itemView) {
            super(itemView);
            ViewUtils.inject(itemView, this);
        }
    }
}
