package cn.scau.mouzhi.atys;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import cn.scau.mouzhi.aty.R;
import cn.scau.mouzhi.bean.Activities;

public class ActivitiesDetail extends Activity {
	private ImageView organization_logo, organization_image;
	private TextView activities_list_organization, parttime_listItem_now_date, activities_list_title,
			activities_list_centent;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activities_detail);
		
		initView();

		// 安卓4.0以后不支持主线程请求HTTP
		if (Build.VERSION.SDK_INT >= 11) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites()
					.detectNetwork().penaltyLog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects()
					.detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
		}

		setView();
	}

	private void initView() {
		organization_logo = (ImageView) findViewById(R.id.organization_logo);
		organization_image = (ImageView) findViewById(R.id.organization_image);
		activities_list_organization = (TextView) findViewById(R.id.activities_list_organization);
		parttime_listItem_now_date = (TextView) findViewById(R.id.parttime_listItem_now_date);
		activities_list_title = (TextView) findViewById(R.id.activities_list_title);
		activities_list_centent = (TextView) findViewById(R.id.activities_list_centent);
	}
	
	private void setView() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		Activities ac = (Activities) intent.getSerializableExtra("Activities");
		if("".equals(ac.getAvatar_url())){
			organization_logo.setImageResource(R.drawable.widget_dface);
		}else{
			changeImage(ac.getAvatar_url(), organization_logo);
		}
		changeImage(ac.getImagurl(), organization_image);
		activities_list_organization.setText(ac.getNickname());
		parttime_listItem_now_date.setText(ac.getTime().toString().substring(5,10));
		activities_list_title.setText(ac.getTitle());
		activities_list_centent.setText(ac.getContent());
	}

	void changeImage(String url, final ImageView image) {
		// 创建网络请求对象
		AsyncHttpClient client = new AsyncHttpClient();
		// client.get("http://121.42.189.168"+url, new
		// AsyncHttpResponseHandler() {
		client.get(url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				if (statusCode == 200) {
					// 创建工厂对象
					BitmapFactory bitmapFactory = new BitmapFactory();
					// 工厂对象的decodeByteArray把字节转换成Bitmap对象
					Bitmap bitmap = bitmapFactory.decodeByteArray(responseBody, 0, responseBody.length);
					// 设置图片
					image.setImageBitmap(bitmap);
					// LayoutParams laParams = (LayoutParams)
					// image.getLayoutParams();
					// laParams.height = 90;
					// laParams.width = 90;
					// image.setLayoutParams(laParams);
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
				error.printStackTrace();
			}
		});

	}

}
