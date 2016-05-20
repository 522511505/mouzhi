package cn.scau.mouzhi.frag;

import java.io.FileInputStream;
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

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import cn.scau.mouzhi.aty.R;
import cn.scau.mouzhi.atys.About;
import cn.scau.mouzhi.atys.AtyLogin;
import cn.scau.mouzhi.atys.Feedback;
import cn.scau.mouzhi.atys.Laboratory;
import cn.scau.mouzhi.atys.MainActivity;
import cn.scau.mouzhi.atys.Search;
import cn.scau.mouzhi.config.Config;
import cn.scau.mouzhi.config.SlashActivity;
import cn.scau.mouzhi.net.NetUtil;

public class SettingFragment extends Fragment {

	public static ImageView widget_dface;
	private TableRow laboratory, school, message_warning, helping, feedback, about, exit;
	private TextView unLogin;
	private static View view;
	private RelativeLayout account_info;
	private static final int IMAGE_SELECT = 1;
	private static final int PHOTO_REQUEST_GALLERY = 2;
	private static final int PHOTO_REQUEST_CUT = 3;
	public static boolean changeText = true;
	private String activityName;
	private static Bitmap bitmap;

	public static int atMainActivity = 0;
	private static SettingFragment single = null;

	public static int FLAG = 0;

	public static SettingFragment getInstance() {
		if (atMainActivity == 1) {
			if (single == null) {
				single = new SettingFragment();
			}
			return single;
		} else {
			return new SettingFragment();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		if (view == null) {
			view = inflater.inflate(R.layout.main_setting, container, false);
		}

		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null) {
			parent.removeView(view);
		}

		initView(view);

		addListener();

		return view;
	}

	private void initView(View view) {
		// TODO Auto-generated method stub
		widget_dface = (ImageView) view.findViewById(R.id.widget_dface);
		unLogin = (TextView) view.findViewById(R.id.unLogin);
		laboratory = (TableRow) view.findViewById(R.id.laboratory);
		school = (TableRow) view.findViewById(R.id.school);
		helping = (TableRow) view.findViewById(R.id.helping);
		feedback = (TableRow) view.findViewById(R.id.feedback);
		about = (TableRow) view.findViewById(R.id.about);
		exit = (TableRow) view.findViewById(R.id.exit);
		account_info = (RelativeLayout) view.findViewById(R.id.account_info);

		Intent intent = getActivity().getIntent();
		activityName = (String) intent.getStringExtra("intentName");
		if ("AtyLogin".equals(activityName)) {
			Bundle bundle = intent.getBundleExtra("bundle");

			String nickname = bundle.getString("nickname");
			String avatar_url = bundle.getString("avatar_url");
			// String avatar_url =
			// "http://121.42.189.168/mouzhi/download/20160501/1462075394057.jpg";
			unLogin.setText(nickname);
			if ("".equals(avatar_url) || null == avatar_url) {
				widget_dface.setImageResource(R.drawable.widget_dface);
			} else {
				AsyncHttpClient client = new AsyncHttpClient();
				client.get(avatar_url, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
						if (statusCode == 200) {
							BitmapFactory bitmapFactory = new BitmapFactory();
							Bitmap image = bitmapFactory.decodeByteArray(responseBody, 0, responseBody.length);
							Bitmap bitmap = toRoundBitmap(image);
							widget_dface.setImageBitmap(bitmap);
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
						error.printStackTrace();
					}
				});
			}
		}
	}

	private void addListener() {
		// TODO Auto-generated method stub
		school.setOnClickListener(new MyClickListener());
		account_info.setOnClickListener(new MyClickListener());
		// message_warning.setOnClickListener(new MyClickListener());
		laboratory.setOnClickListener(new MyClickListener());
		helping.setOnClickListener(new MyClickListener());
		feedback.setOnClickListener(new MyClickListener());
		about.setOnClickListener(new MyClickListener());
		if ("WelcomeActivity".equals(activityName)) {
			widget_dface.setClickable(false);
		} else {
			widget_dface.setOnClickListener(new MyClickListener());
			account_info.setClickable(false);
			exit.setVisibility(0);
			exit.setOnClickListener(new MyClickListener());
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	class MyClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.widget_dface:
				Intent i1 = new Intent(Intent.ACTION_PICK, null);
				i1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				getActivity().startActivityForResult(i1, 1);
				break;
			case R.id.account_info:
				getActivity().startActivity(new Intent(getActivity(), AtyLogin.class),
						ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
				break;
			case R.id.laboratory:
				getActivity().startActivity(new Intent(getActivity(), Laboratory.class),
						ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
				break;
			case R.id.school:
				break;
			case R.id.helping:
				// getActivity().startActivity(new Intent(getActivity(),
				// Helping.class),ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
				Intent i3 = new Intent();
				i3.setClass(getActivity(), Search.class);
				i3.putExtra("ActivityName", "Helping");
				getActivity().startActivity(i3, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
				break;
			case R.id.feedback:
				getActivity().startActivity(new Intent(getActivity(), Feedback.class),
						ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
				break;
			case R.id.about:
				getActivity().startActivity(new Intent(getActivity(), About.class),
						ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
				break;
			case R.id.exit:
				startActivity(new Intent(getActivity(), AtyLogin.class));
				Config.cacheAccount(getActivity(), null);
				Config.cachePassword(getActivity(), null);
				break;
			}
		}

	}

	public void changeImage(String imageUrl) {
		// TODO Auto-generated method stub

		URL url = null;
		try {
			url = new URL("http://121.42.189.168/mouzhi/user/changeavatar");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// int uid = 0;
		// Intent intent = getActivity().getIntent();
		// String activityName = (String) intent.getStringExtra("intentName");
		// if ("AtyLogin".equals(activityName)) {
		// Bundle bundle = intent.getBundleExtra("bundle");
		// uid = bundle.getInt("uid");
		// }

		int uid = SlashActivity.userid;
		Map map = new HashMap();
		map.put("uid", uid);
		map.put("avatar_url", imageUrl);
		String str = NetUtil.submitPostData(url, map);

		JSONObject json;
		JSONObject data;
		int callBackCode = 1;
		try {
			json = new JSONObject(str);
			data = json.getJSONObject("data");
			callBackCode = json.getInt("error_code");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (0 == callBackCode) {
			// ((MainActivity) getActivity()).getUrl(new CallBackListener() {
			// @Override
			// public void getPicturePath(String picturePath, Drawable drawable)
			// {
			// // TODO Auto-generated method stub
			// Bitmap bitmap = drawableToBitmap(drawable);
			//
			// Bitmap bitmap1 = toRoundBitmap(bitmap);
			//
			// widget_dface.setImageBitmap(bitmap1);
			// }
			// });

			// Bitmap bitmap = drawableToBitmap(MainActivity.drawable);
			//
			// Bitmap bitmap1 = toRoundBitmap(bitmap);
			//
			// widget_dface.setImageBitmap(bitmap1);
			// Toast.makeText(getActivity(), "修改头像成功",
			// Toast.LENGTH_SHORT).show();
			// Config.cacheAvatart_url(getContext(), imageUrl);
		}

		// AsyncHttpClient client = new AsyncHttpClient();
		// client.get(imageUrl, new AsyncHttpResponseHandler() {
		// @Override
		// public void onSuccess(int statusCode, Header[] headers, byte[]
		// responseBody) {
		// if (statusCode == 200) {
		// BitmapFactory bitmapFactory = new BitmapFactory();
		// Bitmap image = bitmapFactory.decodeByteArray(responseBody, 0,
		// responseBody.length);
		// bitmap = SettingFragment.toRoundBitmap(image);
		// SettingFragment.FLAG = 1;
		//
		// // MainActivity mac = new MainActivity();
		// // Handler mHandler = mac.getHandler();
		// // Message message = Message.obtain();
		// // message.obj="";
		// //// message.sendToTarget();
		// // mHandler.sendMessage(message);
		//
		// // 要在主線程修改控件
		// // widget_dface.setImageBitmap(bitmap);
		// // SettingFragment.FLAG = 1;
		// }
		// }
		//
		// @Override
		// public void onFailure(int statusCode, Header[] headers, byte[]
		// responseBody, Throwable error) {
		// error.printStackTrace();
		// }
		// });
	}

	/**
	 * 转换图片成圆形
	 * 
	 * @param bitmap
	 *            传入Bitmap对象
	 * @return
	 */
	public static Bitmap toRoundBitmap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2 - 5;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2 - 5;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}

		Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst_left + 15, dst_top + 15, dst_right - 20, dst_bottom - 20);

		paint.setAntiAlias(true);

		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);

		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);
		return output;
	}

	public static Bitmap drawableToBitmap(Drawable drawable) {
		/*
		 * Drawable转化为Bitmap
		 */
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas);
		return bitmap;
	}

	public static void setImage() {
		// while(SettingFragment.FLAG != 1){
		//
		// }
		// widget_dface.setImageBitmap(MainActivity.bitmap);

		// Bitmap bitmap = drawableToBitmap(MainActivity.drawable);
		//
		// Bitmap bitmap1 = toRoundBitmap(bitmap);
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(new FileInputStream(MainActivity.picturePath));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		Bitmap bitmap1 = toRoundBitmap(bitmap2);
		widget_dface.setImageBitmap(bitmap);
	}
}
