package com.tencent.libpag.sample.libpag_sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.libpag.PAGComposition;
import org.libpag.PAGFile;
import org.libpag.PAGView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by p_dmweidu on 2023/2/6
 * Desc:在一个PAGView 中，循环播放多个 PAG 文件
 */
public class PlayManyPagFileActivity extends AppCompatActivity {

    private static final String TAG = "PlayManyPagFileActivity";

    private PAGView pagView;

    /**
     * 在线的PAG文件地址
     */
    private List<String> pagPathList = new ArrayList<>();
    /**
     * 在线的PAG文件
     */
    private List<PAGFile> pagFileList = new ArrayList<>();


    /**
     * 本地的PAG文件地址
     */
    private List<String> pagLocalPathList = new ArrayList<>();

    /**
     * 本地的PAG文件
     */
    private List<PAGFile> localPagFileList = new ArrayList<>();

    public static void launch(Context context) {
        Intent starter = new Intent(context, PlayManyPagFileActivity.class);
        context.startActivity(starter);
    }

    private int currentIndex = 0;

    private int localCurrentIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_many_pagfile);
        pagPathList.add("https://imgservices-1252317822.image.myqcloud.com/coco/s09272024/ee265fb8.negbe4.pag");
        pagPathList.add("https://imgservices-1252317822.image.myqcloud.com/coco/s09182024/c9c73e1c.3yqf2a.pag");

//        pagPathList.add("https://imgservices-1252317822.image.myqcloud.com/coco/s09182024/c9c73e1c.3yqf2a.pag");
//        pagPathList.add("https://imgservices-1252317822.image.myqcloud.com/coco/s09272024/ee265fb8.negbe4.pag");
//
//        pagPathList.add("https://imgservices-1252317822.image.myqcloud.com/coco/s09182024/c9c73e1c.3yqf2a.pag");
//        pagPathList.add("https://imgservices-1252317822.image.myqcloud.com/coco/s09272024/ee265fb8.negbe4.pag");
//
//        pagPathList.add("https://imgservices-1252317822.image.myqcloud.com/coco/s09182024/c9c73e1c.3yqf2a.pag");
//        pagPathList.add("https://imgservices-1252317822.image.myqcloud.com/coco/s09272024/ee265fb8.negbe4.pag");

        pagView = findViewById(R.id.pag_view);

        //currentIndex = 0;
        loadPagFile(pagPathList.get(0));

        //loadLocalFile();

    }

    private void loadPagFile(String path) {
        PAGFile.LoadAsync(path, new PAGFile.LoadListener() {
            @Override
            public void onLoad(PAGFile pagFile) {
                if (pagFile != null) {
                    pagFileList.add(pagFile);
                    Log.i(TAG, "onLoad: pagFile = " + pagFile + " width = " + pagFile.width() + " height = " + pagFile.height() + " currentIndex = " + currentIndex);
                }

                currentIndex += 1;
                Log.d(TAG, "onLoad: currentIndex = " + currentIndex);
                if (currentIndex < pagPathList.size()) {
                    loadPagFile(pagPathList.get(currentIndex));
                } else {
                    long startTime = 0;
                    PAGFile first = pagFileList.get(0);
                    PAGComposition composition = PAGComposition.Make(first.width(), first.height());
                    for (PAGFile file : pagFileList) {
                        file.setStartTime(startTime);
                        composition.addLayer(file);
                        startTime += file.duration();
                    }
                    pagView.setComposition(composition);
                    pagView.setRepeatCount(0);
                    pagView.play();

                }

            }
        });
    }

    private void loadLocalFile() {
        pagLocalPathList.add("pag/stage1.pag");
        pagLocalPathList.add("pag/from1_to_2.pag");
        pagLocalPathList.add("pag/stage2.pag");
        pagLocalPathList.add("pag/from2_to_3.pag");
        pagLocalPathList.add("pag/stage3.pag");
        pagLocalPathList.add("pag/from3_to_4.pag");
        pagLocalPathList.add("pag/stage4.pag");
        pagLocalPathList.add("pag/from4_to_1.pag");

        long currentTime = System.currentTimeMillis();
        Log.i(TAG, "loadLocalFile: start at ");
        for (String s : pagLocalPathList) {
            PAGFile file = PAGFile.Load(getAssets(), s);
            if (file != null) {
                localPagFileList.add(file);
            }
        }
        Log.i(TAG, "loadLocalFile: end 耗时 " + (System.currentTimeMillis() - currentTime));
        if (!localPagFileList.isEmpty()) {
            long startTime = 0;
            PAGFile first = localPagFileList.get(0);
            PAGComposition composition = PAGComposition.Make(first.width(), first.height());
            for (PAGFile file : localPagFileList) {
                file.setStartTime(startTime);
                composition.addLayer(file);
                startTime += file.duration();
            }
            pagView.setComposition(composition);
            pagView.setRepeatCount(0);
            pagView.play();

        }
    }

}