package com.tencent.libpag.sample.libpag_sample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.tencent.libpag.sample.libpag_sample.databinding.ActivityPlayManyPagfileBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.libpag.PAGComposition
import org.libpag.PAGFile
import org.libpag.PAGView

/**
 * Created by p_dmweidu on 2023/2/6
 * Desc:在一个PAGView 中，循环播放多个 PAG 文件
 */
class PlayManyPagFileActivity : AppCompatActivity() {

    /**
     * 在线的PAG文件地址
     */
    private val pagPathList: MutableList<String> = ArrayList()

    /**
     * 在线的PAG文件
     */
    private val pagFileList: MutableList<PAGFile> = ArrayList()


    /**
     * 本地的PAG文件地址
     */
    private val pagLocalPathList: MutableList<String> = ArrayList()

    /**
     * 本地的PAG文件
     */
    private val localPagFileList: MutableList<PAGFile> = ArrayList()

    private var currentIndex = 0

    private val localCurrentIndex = 0


    private lateinit var binding: ActivityPlayManyPagfileBinding

    companion object {
        private const val TAG = "PlayManyPagFileActivity"

        @JvmStatic
        fun launch(context: Context) {
            val starter = Intent(context, PlayManyPagFileActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlayManyPagfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pagPathList.add("https://imgservices-1252317822.image.myqcloud.com/coco/s10172024/66824df9.6g6gy2.pag")
        pagPathList.add("https://imgservices-1252317822.image.myqcloud.com/coco/s10172024/33b92c1e.n3fvof.pag")

        pagPathList.add("https://imgservices-1252317822.image.myqcloud.com/coco/s10172024/b670f636.z42d56.pag")
        pagPathList.add("https://imgservices-1252317822.image.myqcloud.com/coco/s10172024/ee4f6d7d.20hdey.pag")

        pagPathList.add("https://imgservices-1252317822.image.myqcloud.com/coco/s10172024/8904465c.revcxo.pag")
        pagPathList.add("https://imgservices-1252317822.image.myqcloud.com/coco/s10172024/48924ecc.r0tarn.pag")

        pagPathList.add("https://imgservices-1252317822.image.myqcloud.com/coco/s10172024/297c4111.mfi47t.pag")
        pagPathList.add("https://imgservices-1252317822.image.myqcloud.com/coco/s10172024/abdd79f6.4gci59.pag")


        //本地的文件
        pagLocalPathList.add("pag/stage1.pag")
        pagLocalPathList.add("pag/from1_to_2.pag")
        pagLocalPathList.add("pag/stage2.pag")
        pagLocalPathList.add("pag/from2_to_3.pag")
        pagLocalPathList.add("pag/stage3.pag")
        pagLocalPathList.add("pag/from3_to_4.pag")
        pagLocalPathList.add("pag/stage4.pag")
        //pagLocalPathList.add("pag/from4_to_1.pag")


        binding.btnPlayManyUrls.setOnClickListener {
            //播放在线的url
            playManyPagUrls(binding.pagView, pagPathList, true)

        }

        binding.btnPlayLocalFiles.setOnClickListener {
            //播放本地的
            playManyLocalFile(binding.pagView, pagLocalPathList, false)
        }

    }

    /**
     * 从 asset 中加载
     */
    private fun playManyLocalFile(
        pagView: PAGView, localPathList: List<String>,
        repeat: Boolean = false,
    ) {

        if (localPathList.isNullOrEmpty()) {
            return
        }
        val pagFileList = mutableListOf<PAGFile>()

        GlobalScope.launch {

            val current = System.currentTimeMillis()

            //加载网络的pag文件，在后台线程加载，加载完成后，在主线程播放
            localPathList.forEach { pagUrl ->
                //注释1处，唯一的区别，就是调用的 PAGFile 的 Load 方法，这里是从 asset 中加载
                val pagFile = PAGFile.Load(assets, pagUrl)
                if (pagFile != null) {
                    pagFileList.add(pagFile)
                }
            }


            val duration = System.currentTimeMillis() - current
            Log.i(
                TAG,
                "current thread ${Thread.currentThread().name} ，加载本地文件耗时 = $duration"
            )

            withContext(Dispatchers.Main) {
                Log.i(TAG, "切换到主线程，pagFileList.size = ${pagFileList.size}")
                var startTime: Long = 0
                val first = pagFileList[0]
                val composition = PAGComposition.Make(first.width(), first.height())
                for (file in pagFileList) {
                    file.setStartTime(startTime)
                    composition.addLayer(file)
                    val duration = file.duration()
                    Log.i(TAG, "onLoad: duration = $duration")
                    startTime += duration
                }
                pagView.composition = composition
                if (repeat) {
                    //设置循环播放
                    pagView.setRepeatCount(0)
                } else {
                    //只播放一次
                    pagView.setRepeatCount(1)
                }
                pagView.play()
            }
            Log.i(TAG, "pagFileList.size = ${pagFileList.size}")
        }

    }

    private fun playManyPagUrls(
        pagView: PAGView?,
        urlList: List<String?>?,
        repeat: Boolean = false,
    ) {
        if (pagView == null || urlList.isNullOrEmpty()) {
            return
        }
        val pagFileList = mutableListOf<PAGFile>()

        GlobalScope.launch {

            //加载网络的pag文件，在后台线程加载，加载完成后，在主线程播放
            urlList.forEach { pagUrl ->
                val pagFile = PAGFile.Load(pagUrl)
                if (pagFile != null) {
                    pagFileList.add(pagFile)
                }
            }

            withContext(Dispatchers.Main) {
                Log.i(TAG, "切换到主线程，pagFileList.size = ${pagFileList.size}")
                var startTime: Long = 0
                val first = pagFileList[0]
                val composition = PAGComposition.Make(first.width(), first.height())
                for (file in pagFileList) {
                    file.setStartTime(startTime)
                    composition.addLayer(file)
                    val duration = file.duration()
                    Log.i(TAG, "onLoad: duration = $duration")
                    startTime += duration
                }
                pagView.composition = composition
                if (repeat) {
                    //设置循环播放
                    pagView.setRepeatCount(0)
                } else {
                    //只播放一次
                    pagView.setRepeatCount(1)
                }
                pagView.play()
            }
            Log.i(TAG, "pagFileList.size = ${pagFileList.size}")
        }
    }

}