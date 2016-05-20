package com.gallery;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import cn.scau.mouzhi.aty.R;
import cn.scau.mouzhi.bean.Driver;
import cn.scau.mouzhi.net.NetUtil;

public class FamousPeople extends Activity{
	private static int FLAG = 0;
	private static Bitmap[] bitmap;
	private static int driversNum;
	
	public void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState); 
        
        setContentView(R.layout.layout_gallery); 
        Integer[] images = { R.drawable.famous1, 
                R.drawable.famou2, R.drawable.famou3, R.drawable.famous4, 
                R.drawable.famous5,R.drawable.famous6,R.drawable.famous7,R.drawable.famous8,R.drawable.famous9,R.drawable.famous10,}; 
//        Driver[] drivers = getDriver();
        
//        getBitmap(drivers);
        
        ImageAdapter adapter = new ImageAdapter(this, images); 
        adapter.createReflectedImages();
        GalleryFlow galleryFlow = (GalleryFlow) findViewById(R.id.Gallery01); 
        galleryFlow.setAdapter(adapter);
	}

	private Driver[] getDriver() {
		// TODO Auto-generated method stub
		
		URL url = null;
		Map map;
		String str = null;
		try {
			url = new URL("http://121.42.189.168/mouzhi/fame/getFame");
			map = new HashMap();
			map.put("pageNumber", 1);
			map.put("pageSize", 1);
			str = NetUtil.submitPostData(url, map);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		int totalPage = 0;

		try {
			JSONObject json = new JSONObject(str);
			totalPage = json.getInt("totalPage");
			driversNum = totalPage;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Driver[] drivers = new Driver[totalPage];

		for (int i = 1; i < totalPage; i++) {
			URL url2 = null;
			Map map2;
			String str2 = null;
			try {
				url = new URL("http://121.42.189.168/mouzhi/fame/getFame");
				map = new HashMap();
				map.put("pageNumber", i);
				map.put("pageSize", 1);
				str2 = NetUtil.submitPostData(url, map);
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			List listOfCourse = null;
			drivers[i - 1] = new Driver();
			try {
				JSONObject json = new JSONObject(str2);

				JSONArray dataArray = json.getJSONArray("data");
				JSONObject data = (JSONObject) dataArray.opt(0);

				String name = data.getString("name");
				int id = data.getInt("id");
				String avatar = data.getString("avatar");


				drivers[i - 1].setName(name);
				drivers[i - 1].setAvatar(avatar);
				drivers[i - 1].setId(id);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return drivers;
	}
	
//	private void getBitmap(Driver[] drivers){
//		bitmap = new Bitmap[drivers.length];
//		for(int i = 0;i < drivers.length;i++){
//			getBitmaps(drivers[i].getAvatar(),i);
//		}
//	}
	
//	private void getBitmaps(String imageUrl,final int i){
//		AsyncHttpClient client = new AsyncHttpClient();
//		client.get(imageUrl, new AsyncHttpResponseHandler() {
//			@Override
//			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//				if (statusCode == 200) {
//					BitmapFactory bitmapFactory = new BitmapFactory();
//					Bitmap image = bitmapFactory.decodeByteArray(responseBody, 0, responseBody.length);
//					
//					FamousPerson.bitmap[i] = image;
//					if(i >= driversNum - 1){
//						FamousPerson.FLAG = 1;
//					}
//				}
//			}
//
//			@Override
//			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//				error.printStackTrace();
//			}
//		});
//	}
}
