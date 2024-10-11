package com.tencent.libpag.sample.libpag_sample;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tencent.libpag.sample.RecyclerViewActivity;

public class MainActivity extends AppCompatActivity implements SimpleListAdapter.ItemClickListener {

    private static final String[] items = new String[]{
            "A Simple PAG Animation",
            "Text Replacement",
            "Image Replacement",
            "Render Multiple PAG Files on A PAGView",
            "Create PAGSurface through texture ID",
            "Render an interval of the pag file",
            "测试在RecyclerView中使用低于4.0.5.17版本，在View复用的时候，导致PAGView无法播放",
            "测试顺序播放多个PAGFile",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        RecyclerView rv = findViewById(R.id.rv_);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(new SimpleListAdapter(items, this));
    }

    @Override
    public void onItemClick(int position) {
        switch (position) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 5:
                goToAPIsDetail(position);
                break;
            case 4:
                goToTestDetail(position);
                break;
            case 6:
                RecyclerViewActivity.launch(this);
                break;
            case 7:
                PlayManyPagFileActivity.launch(this);
                break;
            default:
                break;
        }
    }

    private void goToAPIsDetail(int position) {
        Intent intent = new Intent(MainActivity.this, APIsDetailActivity.class);
        intent.putExtra("API_TYPE", position);
        startActivity(intent);
    }

    private void goToTestDetail(int position) {
        Intent intent = new Intent(MainActivity.this, TextureDemoActivity.class);
        intent.putExtra("API_TYPE", position);
        startActivity(intent);
    }
}
