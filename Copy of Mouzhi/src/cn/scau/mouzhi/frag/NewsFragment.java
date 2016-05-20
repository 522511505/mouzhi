package cn.scau.mouzhi.frag;

import java.net.MalformedURLException;
import java.net.URL;
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
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.scau.mouzhi.aty.R;
import cn.scau.mouzhi.atys.NewsDetail;
import cn.scau.mouzhi.atys.NewsPublish;
import cn.scau.mouzhi.atys.Search;
import cn.scau.mouzhi.bean.News;
import cn.scau.mouzhi.config.SlashActivity;
import cn.scau.mouzhi.net.NetUtil;

public class NewsFragment extends Fragment implements OnTouchListener {
	// private AtyNewslineAdapter adapter = null;
	// private List<Map<String, Object>> listItems;

	private ImageView news_listItem_image, news_listItem_support, news_listItem_dissupport, news_search, news_publish;
	private TextView news_listItem_title, news_listItem_outline, news_listItem_date_day,
			news_listItem_date_month_and_year, news_listItem_supportCount, news_listItem_dissupportCount,
			news_listItem_moreInfo;
	private static int support = 0;
	private static int dissupport = 0;
	private GestureDetector mGestureDetector;
	private News news;
	private static int currentIndex = 0;
	private View view;// 缓存Fragment view
	public static Bitmap bitmap;
	private String intentName;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// 手势监听
		mGestureDetector = new GestureDetector(getActivity(), new LearnGestureListener());

		// 安卓4.0以后不支持主线程请求HTTP
		if (Build.VERSION.SDK_INT >= 11) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites()
					.detectNetwork().penaltyLog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects()
					.detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
		}

		// 新闻列表的布局
		if (view == null) {
			view = inflater.inflate(R.layout.news_list_cell, container, false);
		}

		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null) {
			parent.removeView(view);
		}

		view.setOnTouchListener(this);

		view.setLongClickable(true);

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
			news = getNewsInfo(1, 1,SlashActivity.userid);
		}else{
			news = getNewsInfo(1, 1,0);
		}
		currentIndex++;
		if (news == null) {
			currentIndex--;
			// Toast.makeText(getActivity(),"已经没有旧新闻",Toast.LENGTH_SHORT).show();
		} else {
			changeNextImage(news.getImageurl(), news_listItem_image);
			changeNextView(news);
		}
	}

	private void initView(View view) {
		// TODO Auto-generated method stub
		news_listItem_image = (ImageView) view.findViewById(R.id.news_listItem_image);
		news_listItem_support = (ImageView) view.findViewById(R.id.news_listItem_support);
		news_listItem_dissupport = (ImageView) view.findViewById(R.id.news_listItem_dissupport);
		news_search = (ImageView) view.findViewById(R.id.news_search);
		news_publish = (ImageView) view.findViewById(R.id.news_publish);
		news_listItem_title = (TextView) view.findViewById(R.id.news_listItem_title);
		news_listItem_outline = (TextView) view.findViewById(R.id.news_listItem_outline);
		news_listItem_date_day = (TextView) view.findViewById(R.id.news_listItem_date_day);
		news_listItem_date_month_and_year = (TextView) view.findViewById(R.id.news_listItem_date_month_and_year);
		news_listItem_supportCount = (TextView) view.findViewById(R.id.news_listItem_supportCount);
		news_listItem_dissupportCount = (TextView) view.findViewById(R.id.news_listItem_dissupportCount);
		news_listItem_moreInfo = (TextView) view.findViewById(R.id.news_listItem_moreInfo);
		Intent intent = getActivity().getIntent();
		intentName = intent.getStringExtra("intentName");
		if ("WelcomeActivity".equals(intentName)) {
			news_publish.setVisibility(View.GONE);
		}
	}

	private void addListener() {
		// TODO Auto-generated method stub
		news_listItem_moreInfo.setOnClickListener(new MyClickListener());
		news_search.setOnClickListener(new MyClickListener());
		news_publish.setOnClickListener(new MyClickListener());

		if ("WelcomeActivity".equals(intentName)) {
			news_listItem_support.setVisibility(0);
			news_listItem_dissupport.setVisibility(0);
		} else {
			news_listItem_support.setOnClickListener(new MyClickListener());
			news_listItem_dissupport.setOnClickListener(new MyClickListener());
		}
	}

	public class LearnGestureListener extends SimpleOnGestureListener {

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

			// 大于设定的最小滑动距离并且在水平/竖直方向速度绝对值大于设定的最小速度，则执行相应方法
			if (Math.abs(e1.getX() - e2.getX()) > 20 && Math.abs(velocityX) > 5) {
				if (e1.getY() - e2.getY() > 20 && Math.abs(velocityY) > 5) {
					if (currentIndex < 2) {
						Toast.makeText(getActivity(), "当前已经是最新的新闻", Toast.LENGTH_SHORT).show();
					} else {
						currentIndex--;
						news = getNewsInfo(currentIndex, 1,SlashActivity.userid);
						changeNextImage(news.getImageurl(), news_listItem_image);
						changeNextView(news);
						if (news.getLikeStatus() == 1) {
							news_listItem_support.setImageResource(R.drawable.after_support);
							support = 1;
						} else {
							news_listItem_support.setImageResource(R.drawable.before_support);
							support = 0;
						}
						if (news.getDislikeStatus() == 1) {
							news_listItem_dissupport.setImageResource(R.drawable.after_dissupport);
							dissupport = 1;
						} else {
							news_listItem_dissupport.setImageResource(R.drawable.before_dissupport);
							dissupport = 0;
						}
					}
				}
				if (e2.getY() - e1.getY() > 20 && Math.abs(velocityY) > 5) {
					currentIndex++;
					news = getNewsInfo(currentIndex, 1,SlashActivity.userid);
					if (news == null) {
						currentIndex--;
						Toast.makeText(getActivity(), "没有旧新闻", Toast.LENGTH_SHORT).show();
					} else {
						changeNextImage(news.getImageurl(), news_listItem_image);
						changeNextView(news);
						if (news.getLikeStatus() == 1) {
							news_listItem_support.setImageResource(R.drawable.after_support);
							support = 1;
						} else {
							news_listItem_support.setImageResource(R.drawable.before_support);
							support = 0;
						}
						if (news.getDislikeStatus() == 1) {
							news_listItem_dissupport.setImageResource(R.drawable.after_dissupport);
							dissupport = 1;
						} else {
							news_listItem_dissupport.setImageResource(R.drawable.before_dissupport);
							dissupport = 0;
						}
					}
				}
			}
			return false;
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
			case R.id.news_listItem_support:
				supportCountAdd();
				break;
			case R.id.news_listItem_dissupport:
				dissupportCountAdd();
				break;
			case R.id.news_listItem_moreInfo:
				showMoreInfo();
				break;
			case R.id.news_search:
				Intent intent = new Intent();
				intent.setClass(getActivity(), Search.class);
				intent.putExtra("ActivityName", "NewsFragment");
				getActivity().startActivity(intent,
						ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
				break;
			case R.id.news_publish:
				publish();
				break;
			}
		}

	}

	private void changeNextImage(String imageurl, final ImageView imageView) {
		// TODO Auto-generated method stub
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(imageurl, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				if (statusCode == 200) {
					// 创建工厂对象
					BitmapFactory bitmapFactory = new BitmapFactory();
					// 工厂对象的decodeByteArray把字节转换成Bitmap对象
					bitmap = bitmapFactory.decodeByteArray(responseBody, 0, responseBody.length);

					imageView.setImageBitmap(bitmap);
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
				error.printStackTrace();
			}
		});
	}

	private void changeNextView(News news) {
		// TODO Auto-generated method stub
		news_listItem_outline.setText(news.getShortcontent());
		news_listItem_title.setText(news.getTitle());
		news_listItem_date_day.setText(news.getTime().substring(8, 10));
		news_listItem_date_month_and_year.setText(news.getTime().substring(0, 7));
		news_listItem_supportCount.setText(String.valueOf(news.getLikenum()));
		news_listItem_dissupportCount.setText(String.valueOf(news.getDislikenum()));
		if (news.getLikeStatus() == 1) {
			news_listItem_support.setImageResource(R.drawable.after_support);
			support = 1;
		}
		if (news.getDislikeStatus() == 1) {
			news_listItem_dissupport.setImageResource(R.drawable.after_dissupport);
			dissupport = 1;
		}
	}

	private void dissupportCountAdd() {
		// TODO Auto-generated method stub
		URL url = null;
		Map map;
		String str = null;
		// Intent i = getActivity().getIntent();
		// Bundle bundle = i.getBundleExtra("bundle");
		// int uid = bundle.getInt("uid");
		if (0 == support) {
			if (1 == dissupport) {
				news_listItem_dissupport.setImageResource(R.drawable.before_dissupport);
				news_listItem_dissupportCount.setText(
						String.valueOf((Integer.parseInt(news_listItem_dissupportCount.getText().toString()) - 1)));
				try {
					url = new URL("http://121.42.189.168/mouzhi/news/dislikeAction");
					map = new HashMap();
					map.put("newsid", news.getNewsid());
					map.put("userid", SlashActivity.userid);
					map.put("type", 1);
					str = NetUtil.submitPostData(url, map);
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				dissupport = 0;
			} else if (0 == dissupport) {
				news_listItem_dissupport.setImageResource(R.drawable.after_dissupport);
				news_listItem_dissupportCount.setText(
						String.valueOf((Integer.parseInt(news_listItem_dissupportCount.getText().toString()) + 1)));
				try {
					url = new URL("http://121.42.189.168/mouzhi/news/dislikeAction");
					map = new HashMap();
					map.put("newsid", news.getNewsid());
					map.put("userid", SlashActivity.userid);
					map.put("type", 0);
					str = NetUtil.submitPostData(url, map);
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				dissupport = 1;
			}
		}
	}

	private void supportCountAdd() {
		// TODO Auto-generated method stub
		URL url = null;
		Map map;
		String str = null;
		// Intent i = getActivity().getIntent();
		// Bundle bundle = i.getBundleExtra("bundle");
		// int uid = bundle.getInt("uid");
		if (0 == dissupport) {
			if (1 == support) {
				news_listItem_support.setImageResource(R.drawable.before_support);
				news_listItem_supportCount.setText(
						String.valueOf((Integer.parseInt(news_listItem_supportCount.getText().toString()) - 1)));
				try {
					url = new URL("http://121.42.189.168/mouzhi/news/likeAction");
					map = new HashMap();
					map.put("newsid", news.getNewsid());
					map.put("userid", SlashActivity.userid);
					map.put("type", 1);
					str = NetUtil.submitPostData(url, map);
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				support = 0;
			} else if (0 == support) {
				news_listItem_support.setImageResource(R.drawable.after_support);
				news_listItem_supportCount.setText(
						String.valueOf((Integer.parseInt(news_listItem_supportCount.getText().toString()) + 1)));
				try {
					url = new URL("http://121.42.189.168/mouzhi/news/likeAction");
					map = new HashMap();
					map.put("newsid", news.getNewsid());
					map.put("userid", SlashActivity.userid);
					map.put("type", 0);
					str = NetUtil.submitPostData(url, map);
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				support = 1;
			}
		}
	}

	private void publish() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setClass(getActivity(), NewsPublish.class);
		Intent i = getActivity().getIntent();
		Bundle bundle = i.getBundleExtra("bundle");
		intent.putExtra("bundle", bundle);
		getActivity().startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
	}

	private void showMoreInfo() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setClass(getActivity(), NewsDetail.class);
		intent.putExtra("News", getNewsInfo(currentIndex, 1,SlashActivity.userid));

		// Bundle bundle = new Bundle();
		// bundle.putParcelable("image", bitmap);
		// intent.putExtra("bundle", bundle);

		// ByteArrayOutputStream baos=new ByteArrayOutputStream();
		// bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		// byte [] bitmapByte =baos.toByteArray();
		// intent.putExtra("bitmap", bitmapByte);

		getActivity().startActivity(intent,
				ActivityOptions.makeSceneTransitionAnimation(getActivity(), news_listItem_image, "image").toBundle());
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return mGestureDetector.onTouchEvent(event);
	}

	public News getNewsInfo(int pageNumber, int pageSize,int uid) {

		URL url = null;
		Map map;
		String str = null;
		try {
			url = new URL("http://121.42.189.168/mouzhi/news/todaynews");
			map = new HashMap();
			map.put("pageNumber", pageNumber);
			map.put("pageSize", pageSize);
			map.put("userid", uid);
			str = NetUtil.submitPostData(url, map);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		news = new News();
		try {
			JSONObject json = new JSONObject(str);
			JSONArray dataArray = json.getJSONArray("data");
			int totalSize = json.getInt("totalPage");
			if (pageNumber >= totalSize + 1) {
				return null;
			}
			for (int i = 0; i < dataArray.length(); i++) {
				JSONObject data = (JSONObject) dataArray.opt(i);

				for (int j = 0; j < data.length(); j++) {
					news.setNewsid(data.getInt("newsid"));
					news.setTime(data.getString("time"));
					news.setShortcontent(data.getString("shortcontent"));
					news.setAuthorid(data.getInt("authorid"));
					news.setTitle(data.getString("title"));
					news.setContent(data.getString("content"));
					news.setStatus(data.getInt("status"));
					news.setNickname(data.getString("nickname"));
					news.setAvatar_url(data.getString("avatar_url"));
					news.setImageurl(data.getString("imageurl"));
					news.setLikenum(data.getInt("likenum"));
					news.setDislikenum(data.getInt("dislikenum"));
					news.setLikeStatus(data.getInt("likeStatus"));
					news.setDislikeStatus(data.getInt("dislikeStatus"));
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return news;
	}
}
