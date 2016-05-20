package cn.scau.mouzhi.atys;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import cn.scau.mouzhi.frag.SettingFragment;
import cn.scau.mouzhi.net.NetUtil;

public class ActivitiesPublish extends Activity implements OnClickListener {
	private static final int IMAGE_SELECT = 1;
	private EditText activities_publish_title, activityies_publish_detail;
	private Button acticities_bt_publish;
	private ImageView activities_publish_add_image;
	private String imageUrl;
	private int authorid;
	private URL url = null;
	private String picturePath;
	private Bundle bundle;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		requestWindowFeature(Window.FEATURE_CONTENT_TRANSITIONS);
		getWindow().setEnterTransition(new Slide());
		
		setContentView(R.layout.activities_publish);
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

	private void initView() {
		activities_publish_add_image = (ImageView) findViewById(R.id.activities_publish_add_image);
		activities_publish_title = (EditText) findViewById(R.id.activities_publish_title);
		activityies_publish_detail = (EditText) findViewById(R.id.activityies_publish_detail);
		acticities_bt_publish = (Button) findViewById(R.id.acticities_bt_publish);

		activities_publish_add_image.setOnClickListener(this);
		acticities_bt_publish.setOnClickListener(this);
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
							JSONObject json = null;
							try {
								json = new JSONObject(result);
								callBackCode = json.getInt("error_code");
								imageUrl = json.getString("fileUrl");
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if (0 == callBackCode) {
//								changeImage(imageUrl);
								activities_publish_add_image.setImageBitmap(BitmapFactory.decodeFile(picturePath));

								LayoutParams laParams = (LayoutParams) activities_publish_add_image.getLayoutParams();
								laParams.height = 220;
								laParams.width = 220;
								activities_publish_add_image.setLayoutParams(laParams);
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
		case R.id.activities_publish_add_image:
			Intent intent = new Intent(Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(intent, IMAGE_SELECT);
			break;
		case R.id.acticities_bt_publish:
			if(contentIsEmpty()){
				Toast.makeText(getApplicationContext(), "请填写完整信息", Toast.LENGTH_SHORT).show();
				break;
			}
			String title = activities_publish_title.getText().toString().trim();
			String content = activityies_publish_detail.getText().toString().trim();
			if ("".equals(imageUrl)) {
				Toast.makeText(ActivitiesPublish.this, "请选择图片", Toast.LENGTH_SHORT).show();
				break;
			} else {
				Map map = new HashMap();
				map.put("title", title);
				map.put("content", content);
				map.put("authorid", authorid);
				map.put("imageurl", imageUrl);
				try {
					url = new URL("http://121.42.189.168/mouzhi/activity/addact");
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String str = NetUtil.submitPostData(url, map);
				int callBack = str.charAt(str.indexOf("error_code") + 12) - 48;
				if (0 == callBack) {
					Toast.makeText(getApplicationContext(), "添加活动成功，待管理员审核后即可显示", Toast.LENGTH_SHORT).show();
					Intent i = new Intent(ActivitiesPublish.this, MainActivity.class);
					i.putExtra("bundle", bundle);
					SettingFragment.atMainActivity = 0;
					startActivity(i);
				}
			}
			break;
		}
	}

	private boolean contentIsEmpty() {
		// TODO Auto-generated method stub
		boolean isEmpty = false;
		if (activities_publish_title.getText().toString().equals("") || activityies_publish_detail.getText().toString().equals(""))
			isEmpty = true;
		return isEmpty;
	}

	void changeImage(String url) {
		// 创建网络请求对象
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				if (statusCode == 200) {
					// 创建工厂对象
					BitmapFactory bitmapFactory = new BitmapFactory();
					// 工厂对象的decodeByteArray把字节转换成Bitmap对象
					Bitmap bitmap = bitmapFactory.decodeByteArray(responseBody, 0, responseBody.length);
					// 设置图片
					activities_publish_add_image.setImageBitmap(bitmap);
					LayoutParams laParams = (LayoutParams) activities_publish_add_image.getLayoutParams();
					laParams.height = 200;
					laParams.width = 200;
					activities_publish_add_image.setLayoutParams(laParams);
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
				error.printStackTrace();
			}

		});

	}
}
