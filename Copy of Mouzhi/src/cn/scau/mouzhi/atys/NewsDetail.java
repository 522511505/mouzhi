package cn.scau.mouzhi.atys;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import cn.scau.mouzhi.aty.R;
import cn.scau.mouzhi.bean.News;
import cn.scau.mouzhi.frag.NewsFragment;

public class NewsDetail extends Activity {
	private ImageView news_image;
	private TextView news_title, news_content;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		requestWindowFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//		getWindow().setEnterTransition(new Slide());
		setContentView(R.layout.news_detail);

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
		news_image = (ImageView) findViewById(R.id.news_image);
		news_title = (TextView) findViewById(R.id.news_title);
		news_content = (TextView) findViewById(R.id.news_content);
	}

	private void setView() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		News news = (News) intent.getSerializableExtra("News");
		// changeImage(news.getImageurl(), news_image);

//		byte[] bis = intent.getByteArrayExtra("bitmap");
//		Bitmap bitmap = BitmapFactory.decodeByteArray(bis, 0, bis.length);
//		news_image.setImageBitmap(bitmap);

//		Bundle bundle = intent.getBundleExtra("bundle");
//		Bitmap bitmap = bundle.getParcelable("image");
		news_image.setImageBitmap(NewsFragment.bitmap);
		
		news_title.setText(news.getTitle());
		news_content.setText(news.getContent());
	}

	private void changeImage(String imageurl, final ImageView imageView) {
		// TODO Auto-generated method stub
		AsyncHttpClient client = new AsyncHttpClient();
		// client.get("http://121.42.189.168/" + imageurl, new
		// AsyncHttpResponseHandler() {
		client.get(imageurl, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				if (statusCode == 200) {
					// 创建工厂对象
					BitmapFactory bitmapFactory = new BitmapFactory();
					// 工厂对象的decodeByteArray把字节转换成Bitmap对象
					Bitmap bitmap = bitmapFactory.decodeByteArray(responseBody, 0, responseBody.length);
					// 设置图片
					// Bitmap nowImage =
					// Bitmap.createBitmap(imageView.getDrawingCache());
					// BitmapDrawable changeBefore = new
					// BitmapDrawable(getContext().getResources(),
					// nowImage);
					// BitmapDrawable changeAfter = new
					// BitmapDrawable(getContext().getResources(), bitmap);
					// Drawable[] layers = new Drawable[2];
					// layers[0] = changeBefore;
					// layers[1] = changeAfter;
					// TransitionDrawable transitionDrawable = new
					// TransitionDrawable(layers);
					//
					// transitionDrawable.startTransition(500);
					// imageView.setImageDrawable(transitionDrawable);
					imageView.setImageBitmap(bitmap);
				} else {

				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
				error.printStackTrace();
			}
		});
	}
}
