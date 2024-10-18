

# 播放在线的多个 PAG 文件

从网络上加载多个 PAG 文件，然后播放。

```kotlin 
private fun playManyPagUrls(
    pagView: PAGView?,
    urlList: List<String?>?,
    repeat: Boolean = false) {
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

```


# 播放本地的多个 PAG 文件，从 asset 中加载

```kotlin

/**
 * 从 asset 中加载
 */
private fun playManyLocalFile(
    pagView: PAGView, localPathList: List<String>,
    repeat: Boolean = false) {

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
```