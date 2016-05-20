package com.gallery;

import android.app.Activity;
import android.os.Bundle;

public class ActivityMain extends Activity {
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        setContentView(R.layout.layout_gallery);
        
        Integer[] images = { R.drawable.hawana_0, 
                R.drawable.huanyinghei_6, R.drawable.wulonghui_24, R.drawable.wulonghui_28,
                R.drawable.xuanfenghei_10};
        
        ImageAdapter adapter = new ImageAdapter(this, images);
        adapter.createReflectedImages();

        GalleryFlow galleryFlow = (GalleryFlow) findViewById(R.id.Gallery01);
        galleryFlow.setAdapter(adapter);
        
}
}