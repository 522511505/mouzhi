package cn.scau.mouzhi.atys;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.Toast;
import cn.scau.mouzhi.adapter.FragmentAdapter;
import cn.scau.mouzhi.aty.R;
import cn.scau.mouzhi.config.Config;
import cn.scau.mouzhi.frag.SettingFragment;

public class MainActivity extends FragmentActivity implements OnClickListener {
	public static final int TAB_NEWS = 0;
	public static final int TAB_PARTTIME = 1;
	public static final int TAB_ACTIVITIES = 2;
	public static final int TAB_SETTING = 3;
	private static final int IMAGE_SELECT = 1;
	private static final int PHOTO_REQUEST_GALLERY = 2;
	private static final int PHOTO_REQUEST_CUT = 3;

	private ViewPager viewPager;
	private FragmentAdapter adapter;
	private RadioButton main_tab_news, main_tab_parttime, main_tab_activities, main_tab_setting;
	public static int flag = 0;
	private String imageUrl;
	public static String picturePath;
	public static Drawable drawable;
	public static Bitmap bitmap;

	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.fragmentmain);

		initView();

		addListener();
	}

	private void initView() {
		// TODO Auto-generated method stub
		viewPager = (ViewPager) findViewById(R.id.viewpager);

		main_tab_news = (RadioButton) findViewById(R.id.main_tab_news);
		main_tab_parttime = (RadioButton) findViewById(R.id.main_tab_parttime);
		main_tab_activities = (RadioButton) findViewById(R.id.main_tab_activities);
		main_tab_setting = (RadioButton) findViewById(R.id.main_tab_setting);

		adapter = new FragmentAdapter(getSupportFragmentManager());
		viewPager.setAdapter(adapter);

		handler = new Handler() {
			public void handleMessage(Message msg) {
				SettingFragment.setImage();
				super.handleMessage(msg);
			}
		};
	}

	@SuppressWarnings("deprecation")
	private void addListener() {
		// TODO Auto-generated method stub
		main_tab_news.setOnClickListener(this);
		main_tab_parttime.setOnClickListener(this);
		main_tab_activities.setOnClickListener(this);
		main_tab_setting.setOnClickListener(this);

		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int id) {
				// TODO Auto-generated method stub
				switch (id) {
				case TAB_NEWS:
					main_tab_news.setChecked(true);
					break;
				case TAB_PARTTIME:
					main_tab_parttime.setChecked(true);
					break;
				case TAB_ACTIVITIES:
					main_tab_activities.setChecked(true);
					break;
				case TAB_SETTING:
					main_tab_setting.setChecked(true);
					break;
				default:
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.main_tab_news:
			viewPager.setCurrentItem(TAB_NEWS);
			break;
		case R.id.main_tab_parttime:
			viewPager.setCurrentItem(TAB_PARTTIME);
			break;
		case R.id.main_tab_activities:
			viewPager.setCurrentItem(TAB_ACTIVITIES);
			break;
		case R.id.main_tab_setting:
			viewPager.setCurrentItem(TAB_SETTING);
			break;
		default:
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1:
			startPhotoZoom(data.getData());
			break;
		case 3:
			if (data != null) {
				setPicToView(data);
			}
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	public void getUrl(final CallBackListener callback) {
		callback.getPicturePath(imageUrl, drawable);
	}

	public interface CallBackListener {
		public void getPicturePath(String picturePath, Drawable drawable);
	}

	// ur是选中图片的地址
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);

		// 获得图片的本地路劲
		Uri selectedImage = uri;
		String[] filePathColumn = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
		cursor.moveToFirst();
		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		picturePath = cursor.getString(columnIndex);
		cursor.close();

		startActivityForResult(intent, 3);
	}

	private void setPicToView(Intent picdata) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			drawable = new BitmapDrawable(photo);

			String path = "http://121.42.189.168/mouzhi/upload/upload";

			File myFile = new File(picturePath);

			RequestParams params = new RequestParams();

			try {
				params.put("file", myFile);

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
								SettingFragment.atMainActivity = 1;
								SettingFragment sf = SettingFragment.getInstance();
								sf.changeImage(imageUrl);
//								SettingFragment.FLAG = 1;
							} else {
								Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
							}
						}
					}
				});
			} catch (FileNotFoundException e) {

			}
			
			Config.cacheAvatart_url(this, imageUrl);
			Message msg = new Message();  
	        handler.sendMessage(msg);
		}
	}

//	public Handler getHandler() {
//		return this.handler;
//	}
	
	// public static Drawable tintDrawable(Drawable drawable, ColorStateList
	// colors) {
	// final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
	// DrawableCompat.setTintList(wrappedDrawable, colors);
	// return wrappedDrawable;
	// }
}
