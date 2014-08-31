//2014-8-7
package com.kubeiwu.commontool.volley.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.kubeiwu.commontool.volley.cache.disk.DiskLruImageCache;
import com.kubeiwu.commontool.volley.cache.disk.util.Md5;
import com.kubeiwu.commontool.volley.cache.memory.LruMemoryCache;

/**
 * @author  cgpllx1@qq.com (www.kubeiwu.com)
 * @date    2014-8-7
 */
public class BitmapCache implements ImageCache {
	public Context context;
	public String uniqueName = "kubeiwu";
	public int diskCacheSize = 1024 * 1024 * 1024;   

	public BitmapCache(Context context) {
		this.context = context;
		mDiskLruImageCache = new DiskLruImageCache(context, uniqueName, diskCacheSize);
		mLruMemoryCachem = new LruMemoryCache();
	}

	LruMemoryCache mLruMemoryCachem;
	DiskLruImageCache mDiskLruImageCache;

	@Override
	public Bitmap getBitmap(String urlkey) {
		urlkey = Md5.generate(urlkey);
		if (TextUtils.isEmpty(urlkey))
			throw new IllegalStateException("image url is not right");
		Bitmap bitmap = mLruMemoryCachem.getBitmap(urlkey);
		if (bitmap == null) {
			bitmap = mDiskLruImageCache.getBitmap(urlkey);
			if (bitmap != null && !bitmap.isRecycled()) {
				mLruMemoryCachem.putBitmap(urlkey, bitmap);
			}
		}
		return bitmap;
	}

	@Override
	public void putBitmap(String urlkey, Bitmap bitmap) {
		urlkey = Md5.generate(urlkey);
		if (TextUtils.isEmpty(urlkey))
			return;
		mLruMemoryCachem.putBitmap(urlkey, bitmap);
		if (!mDiskLruImageCache.containsKey(urlkey))
			mDiskLruImageCache.putBitmap(urlkey, bitmap);
	}

}
