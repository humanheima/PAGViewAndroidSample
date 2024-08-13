package com.tencent.libpag.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tencent.libpag.sample.libpag_sample.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by p_dmweidu on 2023/2/6
 * Desc:
 */
public class RecyclerViewActivity extends AppCompatActivity {

    private RecyclerView rv_gold_voice_list;
    private LinearLayout rlExpandAll;
    private int maxCount = 1;

    private List<String> dataList = new ArrayList<>();
    private RvAdapter adapter;

    public static void launch(Context context) {
        Intent starter = new Intent(context, RecyclerViewActivity.class);
        context.startActivity(starter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataList.add("one");
        dataList.add("two");
        setContentView(R.layout.activity_recycler_view);
        rv_gold_voice_list = findViewById(R.id.rv_gold_voice_list);
        rlExpandAll = findViewById(R.id.ll_expand_all_voice);
        setAdapter();
    }

    private void setAdapter() {
        int size = dataList.size();
        if (size > maxCount) {
            List<String> limitedList = new ArrayList<>();
            for (int i = 0; i < maxCount; i++) {
                limitedList.add(dataList.get(i));
            }
            setUpAdapter(limitedList);
            rlExpandAll.setVisibility(View.VISIBLE);
            rlExpandAll.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    setUpAdapter(dataList);
                }
            });

        } else {
            rlExpandAll.setVisibility(View.GONE);
        }
    }

    private void setUpAdapter(List<String> limitedList) {
        if (adapter == null) {
            rv_gold_voice_list.setLayoutManager(new LinearLayoutManager(this));
            adapter = new RvAdapter();
            adapter.setDatas(limitedList);
            rv_gold_voice_list.setAdapter(adapter);
        } else {
            adapter.setDatas(limitedList);
            adapter.notifyDataSetChanged();
        }

    }
}