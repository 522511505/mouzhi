package cn.scau.mouzhi.atys;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.transition.Slide;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import cn.scau.mouzhi.aty.R;
import cn.scau.mouzhi.bean.News;
import cn.scau.mouzhi.frag.SettingFragment;
import cn.scau.mouzhi.net.NetUtil;

public class NewsPublish extends Activity implements OnClickListener {
	private EditText news_publish_title, news_publish_outline, news_publish_detail;
	private ImageView news_add_image;
	private Button bt_news_publish;
	private static final int IMAGE_SELECT = 1;
	private News news;
	private int authorid;
	private String imageUrl = "";
	private String picturePath;
	private Bundle bundle;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		requestWindowFeature(Window.FEATURE_CONTENT_TRANSITIONS);
		getWindow().setEnterTransition(new Slide());
		setContentView(R.layout.news_public);
		// 安卓4.0以后不支持主线程请求HTTP
		if (Build.VERSION.SDK_INT >= 11) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites()
					.detectNetwork().penaltyLog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects()
					.detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
		}

		Intent intent = getIntent();
		bundle = intent.getBundleExtra("bundle");
		authorid = bundle.getInt("uid");

		initView();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	private void initView() {
		news_publish_title = (EditText) findViewById(R.id.news_publish_title);
		news_publish_outline = (EditText) findViewById(R.id.news_publish_outline);
		news_publish_detail = (EditText) findViewById(R.id.news_publish_detail);
		bt_news_publish = (Button) findViewById(R.id.bt_news_publish);
		news_add_image = (ImageView) findViewById(R.id.news_add_image);

		bt_news_publish.setOnClickListener(this);
		news_add_image.setOnClickListener(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == IMAGE_SELECT && resultCode == RESULT_OK && null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			picturePath = cursor.getString(columnIndex);
			cursor.close();

			String path = "http://121.42.189.168/mouzhi/upload/upload";

			File myFile = new File(picturePath);

			RequestParams params = new RequestParams();

			try {
				params.put("binary", myFile);

				AsyncHttpClient client = new AsyncHttpClient();

				client.post(path, params, new AsyncHttpResponseHandler() {

					@Override
					public void onFailure(int arg0, org.apache.http.Header[] arg1, byte[] arg2, Throwable arg3) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(int status, org.apache.http.Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method stub
						if (status == 200) {
							String result = new String(arg2);

							int callBackCode = 1;
							try {
								JSONObject json = new JSONObject(result);
								imageUrl = json.getString("fileUrl");
								callBackCode = json.getInt("error_code");
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if (0 == callBackCode) {
								news_add_image.setImageBitmap(BitmapFactory.decodeFile(picturePath));

								LayoutParams laParams = (LayoutParams) news_add_image.getLayoutParams();
								laParams.height = 220;
								laParams.width = 220;
								news_add_image.setLayoutParams(laParams);

							} else {
								Toast.makeText(getApplicationContext(), "你选择的图片过大，请重新选择", Toast.LENGTH_LONG).show();
							}
						}
					}
				});
			} catch (FileNotFoundException e) {
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.news_add_image:
			Intent intent = new Intent(Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(intent, IMAGE_SELECT);
			break;
		case R.id.bt_news_publish:
			URL url = null;
			try {
				url = new URL("http://121.42.189.168/mouzhi/news/addnews");
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (contentIsEmpty()) {
				Toast.makeText(NewsPublish.this, "请填写完整信息", Toast.LENGTH_SHORT).show();
				break;
			}else if ("".equals(imageUrl)) {
				Toast.makeText(NewsPublish.this, "请选择图片", Toast.LENGTH_SHORT).show();
			} else {
				Map map = new HashMap();
				map.put("authorid", authorid);
				map.put("title", news_publish_title.getText().toString());
				map.put("shortContent", news_publish_outline.getText().toString());
				map.put("content", news_publish_detail.getText().toString());
				map.put("imageurl", imageUrl);

				String str = NetUtil.submitPostData(url, map);

				JSONObject json;
				// try {
				// json = new JSONObject(str);
				// // callBackCode = json.getInt("error_code");
				// } catch (JSONException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				int callBack = str.charAt(str.indexOf("error_code") + 12) - 48;
				if (0 == callBack) {
					Toast.makeText(getApplicationContext(), "添加新闻成功，待管理员审核后即可显示", Toast.LENGTH_SHORT).show();
					Intent i = new Intent(NewsPublish.this, MainActivity.class);
					i.putExtra("intentName", "NewsPublish");
					i.putExtra("bundle", bundle);
					SettingFragment.atMainActivity = 0;
					startActivity(i);
					finish();
				} else {

				}
			}
			// Intent i = new Intent(NewsPublish.this,MainActivity.class);
			// i.putExtra("news", news);
			// i.putExtra("intentName", "NewsPublish");
			// startActivity(i);
			break;
		}
	}

	private boolean contentIsEmpty() {
		// TODO Auto-generated method stub
		boolean isEmpty = false;
		if (news_publish_title.getText().toString().equals("") || news_publish_outline.getText().toString().equals("")
				|| news_publish_detail.getText().toString().equals("")) {
			isEmpty = true;
		}
		return isEmpty;
	}

}
