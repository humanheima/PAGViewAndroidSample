
可以修改的图层：PAG 的渲染图层，PAG 是一个树状结构，PAGLayer 相当于树状结构中的叶子节点。
PAG 对外暴露的渲染图层包括 PAGImageLayer （图片图层）、 PAGTextLayer （文本图层）、PAGSolidLayer（实色图层），
只有这些图层可以二次改编辑。

1. 比如修改图层类型为 LayerTypeSolid 的图层的颜色

```kotlin
/**
 * 比如修改图层类型为 LayerTypeSolid 的图层的颜色
 */
fun updatePagColor(color: Int) {
    //获取类型为 LayerTypeSolid 可编辑图层
    val editableIndices = pagFile?.getEditableIndices(PAGTextLayer.LayerTypeSolid) ?: return
    for (editableIndex in editableIndices) {
        val pagLayers =
            pagFile?.getLayersByEditableIndex(editableIndex, PAGTextLayer.LayerTypeSolid)
                ?: break
        for (pagLayer in pagLayers) {
            if (pagLayer is PAGSolidLayer) {
                pagLayer.setSolidColor(color)
            }
        }
    }
}
```
2. 如果知道图层明层，根据名称获取图层后修改。

```kotlin
fun updatePagColor(color: Int) {
    val layers: Array<PAGLayer>? = pagFile?.getLayersByName("深 绿色 纯色 1")
    Log.d(TAG, "updatePagColor: layers = ${layers?.size}")
    layers?.forEach {
        if (it is PAGSolidLayer) {
            it.setSolidColor(color)
        }
    }
}
```

* [常用 API 解读](https://pag.art/docs/api-instructions.html)
* [API 参考](https://pag.art/apis/android/reference/org/libpag/PAGFile.html)
