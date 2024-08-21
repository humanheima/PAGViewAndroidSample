package com.tencent.libpag.sample.libpag_sample

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import org.libpag.PAGComposition
import org.libpag.PAGFile
import org.libpag.PAGLayer
import org.libpag.PAGSolidLayer
import org.libpag.PAGView
import org.libpag.PAGView.PAGViewListener

/**
 * Created by p_dmweidu on 2023/2/7
 * Desc:
 */
class CustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val BLACK_PAG_FILE_NAME = "assets://pag/sound_wave_black.pag"
        private const val BLACK_PAG_FILE_NAME2 = "assets://pag/sound_wave2.pag"
        private const val BLACK_PAG_FILE_NAME3 = "assets://pag/sound_microphone.pag"

        //private static final String BLACK_PAG_FILE_NAME = "assets://10.pag";
        private const val TAG = "CustomView"
    }

    private var pagView: PAGView? = null
    private var pagView2: PAGView? = null
    private var pagView3: PAGView? = null
    private val pagComposition: PAGComposition? = null
    private var iv_play_pause: ImageView? = null

    private var pagFile: PAGFile? = null


    init {
        init(context)
    }

    fun init(context: Context) {
        inflate(context, R.layout.view_gold_voice, this)
        pagView = findViewById(R.id.pag_view_rhythm)
        pagView?.setRepeatCount(-1)
        pagView2 = findViewById(R.id.pag_view_rhythm2)
        pagView2?.setRepeatCount(-1)
        pagView3 = findViewById(R.id.pag_view_rhythm3)
        pagView3?.setRepeatCount(-1)


        pagFile = PAGFile.Load(context.assets, "pag/voice_play.pag")


        pagView?.setComposition(pagFile)

        //pagView?.setPath(BLACK_PAG_FILE_NAME)
        pagView2?.setPath(BLACK_PAG_FILE_NAME2)
        pagView3?.setPath(BLACK_PAG_FILE_NAME3)
        iv_play_pause = findViewById(R.id.iv_play_pause)
        pagView3?.addListener(object : PAGViewListener {
            override fun onAnimationStart(pagView: PAGView) {
                Log.d(TAG, "onAnimationStart: \$pagView")
            }

            override fun onAnimationEnd(pagView: PAGView) {
                Log.d(TAG, "onAnimationEnd: \$pagView")
            }

            override fun onAnimationCancel(pagView: PAGView) {
                Log.d(TAG, "onAnimationCancel: \$pagView")
            }

            override fun onAnimationRepeat(pagView: PAGView) {
                Log.d(TAG, "onAnimationRepeat: \$pagView")
            }

            override fun onAnimationUpdate(pagView: PAGView) {
                Log.d(TAG, "onAnimationUpdate: \$pagView")
            }
        })
        iv_play_pause?.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View) {
                Log.i(
                    TAG,
                    "onClick: " + this.hashCode() + "pagView = " + pagView.hashCode() + " isPlaying = " + pagView?.isPlaying
                )
                if (pagView?.isPlaying == false) {
                    pagView?.play()
                } else {
                    pagView?.stop()
                }
                if (pagView2?.isPlaying == true) {
                    pagView2?.stop()
                } else {
                    pagView2?.play()
                }
                if (pagView3?.isPlaying == true) {
                    pagView3?.stop()
                } else {
                    pagView3?.play()
                }
            }
        })
    }

    /**
     * 比如修改图层类型为 LayerTypeSolid 的图层的颜色
     */
    fun updatePagColor(color: Int) {
        val layers: Array<PAGLayer>? = pagFile?.getLayersByName("深 绿色 纯色 1")
        Log.d(TAG, "updatePagColor: layers = ${layers?.size}")
        layers?.forEach {
            if (it is PAGSolidLayer) {
                it.setSolidColor(color)
            }
        }

//        val editableIndices = pagFile?.getEditableIndices(PAGTextLayer.LayerTypeSolid) ?: return
//        for (editableIndex in editableIndices) {
//            val pagLayers =
//                pagFile?.getLayersByEditableIndex(editableIndex, PAGTextLayer.LayerTypeSolid)
//                    ?: break
//            for (pagLayer in pagLayers) {
//                if (pagLayer is PAGSolidLayer) {
//                    pagLayer.setSolidColor(color)
//                }
//            }
//        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        //        Log.i(TAG,
//                "onAttachedToWindow: " + this.hashCode() + "pagView = " + pagView.hashCode() + " isPlaying = " + pagView
//                        .isPlaying());
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        //        Log.i(TAG,
//                "onDetachedFromWindow: " + this.hashCode() + "pagView = " + pagView.hashCode() + " isPlaying = "
//                        + pagView
//                        .isPlaying());
    }

}
