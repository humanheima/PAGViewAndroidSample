package com.tencent.libpag.sample.libpag_sample;

import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import org.libpag.PAGComposition;
import org.libpag.PAGFile;
import org.libpag.PAGView;

/**
 * Created by p_dmweidu on 2023/2/7
 * Desc:
 */
public class CustomView extends RelativeLayout {

    private static final String BLACK_PAG_FILE_NAME = "assets://pag/sound_wave_black.pag";
    //private static final String BLACK_PAG_FILE_NAME = "assets://10.pag";

    private static final String TAG = "CustomView";


    private PAGView pagView;

    private PAGComposition pagComposition;


    private ImageView iv_play_pause;

    public CustomView(Context context) {
        this(context, null);
    }

    public CustomView(Context context, AttributeSet attrs) {
        this(context, null, -1);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(Context context) {
        View.inflate(context, R.layout.view_gold_voice, this);
        pagView = findViewById(R.id.pag_view_rhythm);
        pagView.setRepeatCount(-1);
        pagView.setComposition(pagComposition);
        pagView.setPath(BLACK_PAG_FILE_NAME);

        iv_play_pause = findViewById(R.id.iv_play_pause);

        iv_play_pause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: " + this.hashCode() + "pagView = " + pagView.hashCode() + " isPlaying = " + pagView
                        .isPlaying());
                if (!pagView.isPlaying()) {
                    pagView.play();
                } else {
                    pagView.stop();
                }
            }
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.i(TAG,
                "onAttachedToWindow: " + this.hashCode() + "pagView = " + pagView.hashCode() + " isPlaying = " + pagView
                        .isPlaying());
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.i(TAG,
                "onDetachedFromWindow: " + this.hashCode() + "pagView = " + pagView.hashCode() + " isPlaying = " + pagView
                        .isPlaying());
    }


}
