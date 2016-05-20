package cn.scau.mouzhi.frag;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.scau.mouzhi.aty.R;
import cn.scau.mouzhi.atys.ActivitiesDetail;
import cn.scau.mouzhi.atys.ActivitiesPublish;
import cn.scau.mouzhi.atys.Search;
import cn.scau.mouzhi.bean.Activities;
import cn.scau.mouzhi.config.SlashActivity;
import cn.scau.mouzhi.net.NetUtil;

public class ActivitiesFragment extends Fragment {
	private View view;
	private ImageView activity_search, activity_publish, organization_logo, organization_image;
	private Button activities_previous, activities_detail, activities_next;
	private TextView activities_list_organization, activities_list_now_date, activities_list_title;
	private static int currentIndex = 0;
	private Activities ac;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.activities_list_cell, container, false);
		}

		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null) {
			parent.removeView(view);
		}

		// 安卓4.0以后不支持主线程请求HTTP
		if (Build.VERSION.SDK_INT >= 11) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites()
					.detectNetwork().penaltyLog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects()
					.detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
		}

		initView(view);

		addListener();

		setView();
		
		return view;
	}

	private void setView() {
		// TODO Auto-generated method stub
		Intent intent = getActivity().getIntent();
		String intentName = intent.getStringExtra("intentName");
		if (!"WelcomeActivity".equals(intentName)) {
			currentIndex = 0;
		}
		currentIndex++;
		ac = getActivityInfo(currentIndex, 1);
		if(ac == null){
//			Toast.makeText(getActivity(),"已没有旧活动",Toast.LENGTH_SHORT).show();
		}else{
			if("".equals(ac.getAvatar_url())){
				organization_logo.setImageResource(R.drawable.widget_dface);
			}else{
				changeNextImage(ac.getAvatar_url(), organization_logo);
			}
			changeNextImage(ac.getImagurl(), organization_image);
			changeNextView(ac);
		}
	}

	private void initView(View view) {
		// TODO Auto-generated method stub
		activity_search = (ImageView) view.findViewById(R.id.activity_search);
		activity_publish = (ImageView) view.findViewById(R.id.activity_publish);
		organization_logo = (ImageView) view.findViewById(R.id.organization_logo);
		organization_image = (ImageView) view.findViewById(R.id.organization_image);
		activities_previous = (Button) view.findViewById(R.id.activities_previous);
		activities_detail = (Button) view.findViewById(R.id.activities_detail);
		activities_next = (Button) view.findViewById(R.id.activities_next);
		activities_list_organization = (TextView) view.findViewById(R.id.activities_list_organization);
		activities_list_now_date = (TextView) view.findViewById(R.id.activities_list_now_date);
		activities_list_title = (TextView) view.findViewById(R.id.activities_list_title);
		Intent intent = getActivity().getIntent();
		String intentName = intent.getStringExtra("intentName");
		if ("WelcomeActivity".equals(intentName)) {
			activity_publish.setVisibility(View.GONE);
		}

	}

	private void addListener() {
		// TODO Auto-generated method stub
		activity_search.setOnClickListener(new MyClickListener());
		activity_publish.setOnClickListener(new MyClickListener());
		activities_previous.setOnClickListener(new MyClickListener());
		activities_detail.setOnClickListener(new MyClickListener());
		activities_next.setOnClickListener(new MyClickListener());
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
			case R.id.activity_search:
				Intent turnToSearch = new Intent();
				turnToSearch.setClass(getActivity(), Search.class);
				turnToSearch.putExtra("ActivityName", "ActivitiesFragment");
				getActivity().startActivity(turnToSearch,ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
				break;
			case R.id.activity_publish:
				Intent turnToPublic = new Intent();
				turnToPublic.setClass(getActivity(), ActivitiesPublish.class);
				Intent intent = getActivity().getIntent();
				Bundle bundle = intent.getBundleExtra("bundle");
				turnToPublic.putExtra("bundle", bundle);
				getActivity().startActivity(turnToPublic,ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
				break;
			case R.id.activities_previous:
				 activities_previous();
				break;
			case R.id.activities_detail:
				activities_detail();
				break;
			case R.id.activities_next:
				activities_next();
				break;
			default:
				break;
			}
		}

		private void activities_previous() {
			if (currentIndex < 2) {
				Toast.makeText(getActivity(),"当前已经是最新的活动",Toast.LENGTH_SHORT).show();
			} else {
				currentIndex--;
				ac = getActivityInfo(currentIndex, 1);
				if("".equals(ac.getAvatar_url())){
					organization_logo.setImageResource(R.drawable.widget_dface);
				}else{
					changeNextImage(ac.getAvatar_url(), organization_logo);
				}
				changeNextImage(ac.getImagurl(), organization_image);
				changeNextView(ac);
			}
		}

		private void activities_next() {
			currentIndex++;
			ac = getActivityInfo(currentIndex, 1);
			if(ac == null){
				currentIndex--;
				Toast.makeText(getActivity(),"已没有旧活动",Toast.LENGTH_SHORT).show();
			}else{
				changeNextImage(ac.getAvatar_url(), organization_logo);
				changeNextImage(ac.getImagurl(), organization_image);
				changeNextView(ac);
			}
		}
	}

	private void changeNextImage(String avatar_url, final ImageView image) {
//		if("".equals(ac.getAvatar_url())){
//			image.setImageResource(R.drawable.widget_dface);
//			return;
//		}
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(avatar_url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				if (statusCode == 200) {
					// 创建工厂对象
					BitmapFactory bitmapFactory = new BitmapFactory();
					// 工厂对象的decodeByteArray把字节转换成Bitmap对象
					Bitmap bitmap = bitmapFactory.decodeByteArray(responseBody, 0, responseBody.length);
					image.setImageBitmap(bitmap);
				} 
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
				error.printStackTrace();
			}
		});

	}

	private void changeNextView(Activities ac) {
		activities_list_organization.setText(ac.getNickname());
		String time = ac.getTime().substring(5, 10);
		activities_list_now_date.setText(time);
		
		activities_list_title.setText(ac.getTitle());
		// parttime_listItem_now_date.setText(new Date().getMonth() + "-" +
		// new Date().getDay());
	}

	private void activities_detail() {
		// TODO Auto-generated method stub
		Intent i = new Intent(getActivity(), ActivitiesDetail.class);
		i.putExtra("Activities", getActivityInfo(currentIndex, 1));
		getActivity().startActivity(i,ActivityOptions.makeSceneTransitionAnimation(getActivity(), organization_image, "organization_image").toBundle());
	}
	
	public Activities getActivityInfo(int pageNumber, int pageSize) {

		URL url = null;
		Map map;
		String str = null;
		try {
			url = new URL("http://121.42.189.168/mouzhi/activity/getActivity");
			map = new HashMap();
			map.put("pageNumber", pageNumber);
			map.put("pageSize", pageSize);
			int a = SlashActivity.userid;
			map.put("userid", 0);
			str = NetUtil.submitPostData(url, map);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		ac = new Activities();
		try {
			JSONObject json = new JSONObject(str);
			JSONArray dataArray = json.getJSONArray("data");
			int totalSize = json.getInt("totalPage");
			if(pageNumber >= totalSize + 1){
				return null;
			}
			for (int i = 0; i < dataArray.length(); i++) {
				JSONObject data = (JSONObject) dataArray.opt(i);
				ac.setAcid(Integer.parseInt(data.getString("acid")));
				ac.setTitle(data.getString("title"));
				ac.setContent(data.getString("content"));
				ac.setAuthorid(data.getInt("authorid"));
				ac.setTime(data.getString("time"));
				ac.setImagurl(data.getString("imageurl"));
//				ac.setImagurl("http://hiphotos.baidu.com/baidu/pic/item/7d8aebfebf3f9e125c6008d8.jpg");
				ac.setNickname(data.getString("nickname"));
				ac.setAvatar_url(data.getString("avatar_url"));
//				ac.setAvatar_url("http://hiphotos.baidu.com/baidu/pic/item/7d8aebfebf3f9e125c6008d8.jpg");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ac;
	}
}
