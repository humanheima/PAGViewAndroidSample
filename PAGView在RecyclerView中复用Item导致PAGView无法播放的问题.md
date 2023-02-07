有问题的版本：`com.tencent.tav:libpag:4.0.5.11`。
低于这个版本一下应该都有问题。我们项目中的版本是 `com.tencent.tav:libpag:4.0.5.10`。


操作步骤
1. RecyclerView 中 先加载一个数据。 然后通过点击 `展开所有的item`。调用 notifyDataSetChanged 展开全部两条数据。
2. 然后点击第一条数据里面的播放按钮，发现 PAGView 动画无法执行。


没有问题的版本：版本号大于 `com.tencent.tav:libpag:4.0.5.17` 的版本都可以。

### 大概的原因分析

对比了两个版本 PAGView
 
 
`com.tencent.tav:libpag:4.0.5.17`版本没问题的 PAGView 部分代码
 
 ```java
public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
    this.pagPlayer.setSurface((PAGSurface)null);
    if (this.mListener != null) {
        this.mListener.onSurfaceTextureDestroyed(surface);
    }

    if (this.pagSurface != null) {
        this.pagSurface.freeCache();
    }

    boolean surfaceDestroy = true;
    if (g_PAGViewHandler != null && surface != null) {
        SendMessage(1, surface);
        surfaceDestroy = false;
    }

    if (VERSION.SDK_INT >= 26) {
        synchronized(g_HandlerLock) {
            DestroyHandlerThread();
        }
    }

    return surfaceDestroy;
}
```


```java
protected void finalize() throws Throwable {
    super.finalize();
    this.animator.removeUpdateListener(this.mAnimatorUpdateListener);
}
```


`com.tencent.tav:libpag:4.0.5.11` 版本有问题的 PAGView 部分代码。


```java
@Override
public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
    pagPlayer.setSurface(null);
    if(mListener != null) {
        mListener.onSurfaceTextureDestroyed(surface);
    }
    if(pagSurface != null) {
        pagSurface.freeCache();
    }
    //注释1处
    animator.removeUpdateListener(mAnimatorUpdateListener);
    boolean surfaceDestroy = true;
    if(g_PAGViewHandler != null && surface != null) {
        SendMessage(MSG_SURFACE_DESTROY, surface);
        surfaceDestroy = false;
    }
    if(Build.VERSION.SDK_INT >= ANDROID_SDK_VERSION_O) {
        synchronized(g_HandlerLock) {
            DestroyHandlerThread();
        }
    }
    return surfaceDestroy;
}
```

注释1处，移除了 mAnimatorUpdateListener。 这个一个监听动画进度更新的回调。

而PAGView正是通过这个监听回调来监听属性动画的更新并执行PAGView的动画的。所以移除了就会有问题。

```java
private final ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            PAGView.this.currentPlayTime = animation.getCurrentPlayTime();
            NeedsUpdateView(PAGView.this);
        }
    };

```

结论：

1. 调用 notifyDataSetChanged 展开所有数据的时候，第一条数据的 view 会先 detachedFromWindow，会调用 PAGView 的 onDetachedFromWindow 方法。

```java
protected void onDetachedFromWindow() {
    this.isAttachedToWindow = false;
    super.onDetachedFromWindow();
    if(this.pagSurface != null) {
        this.pagSurface.release();
        this.pagSurface = null;
    }
    //注释1处
    this.pauseAnimator();
    if(VERSION.SDK_INT < 26) {
        synchronized(g_HandlerLock) {
            DestroyHandlerThread();
        }
    }
    this.animator.removeListener(this.mAnimatorListenerAdapter);
}
```

注释1处，把属性动画暂停了。

onDetachedFromWindow 会导致 PAGView 的 Surface 被销毁。 回调 onSurfaceTextureDestroyed 方法。

``java
@Override
public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
    pagPlayer.setSurface(null);
    if(mListener != null) {
        mListener.onSurfaceTextureDestroyed(surface);
    }
    if(pagSurface != null) {
        pagSurface.freeCache();
    }
    //注释1处
    animator.removeUpdateListener(mAnimatorUpdateListener);
    boolean surfaceDestroy = true;
    if(g_PAGViewHandler != null && surface != null) {
        SendMessage(MSG_SURFACE_DESTROY, surface);
        surfaceDestroy = false;
    }
    if(Build.VERSION.SDK_INT >= ANDROID_SDK_VERSION_O) {
        synchronized(g_HandlerLock) {
            DestroyHandlerThread();
        }
    }
    return surfaceDestroy;
}
```

有问题的版本在注释1处移除了 mAnimatorUpdateListener 。后续再启动动画等操作都没有重新添加 mAnimatorUpdateListener 监听属性动画。所以PAGView 动画就执行不了了。




 
 
