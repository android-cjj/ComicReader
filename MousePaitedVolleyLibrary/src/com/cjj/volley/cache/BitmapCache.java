package com.cjj.volley.cache;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.cjj.volley.toolbox.ImageLoader.ImageCache;



public class BitmapCache implements ImageCache {  
  
    private LruCache<String, Bitmap> mCache;  
  
    public BitmapCache() {  
        int maxSize = 10 * 1024 * 1024;  
        mCache = new LruCache<String, Bitmap>(maxSize) {  
            @Override  
            protected int sizeOf(String key, Bitmap bitmap) {  
                return bitmap.getRowBytes() * bitmap.getHeight();  
            }  
        };  
    }  
  
    @Override  
    public Bitmap getBitmap(String url) {  
        return mCache.get(url);  
    }  
  
    @Override  
    public void putBitmap(String url, Bitmap bitmap) {  
        mCache.put(url, bitmap);  
    }  
  
}  