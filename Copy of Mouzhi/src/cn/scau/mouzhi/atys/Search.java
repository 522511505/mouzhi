package cn.scau.mouzhi.atys;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.transition.Explode;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import cn.scau.mouzhi.adapter.SearchAdapter;
import cn.scau.mouzhi.aty.R;
import cn.scau.mouzhi.bean.Activities;
import cn.scau.mouzhi.bean.Bean;
import cn.scau.mouzhi.bean.HelpingInfo;
import cn.scau.mouzhi.bean.News;
import cn.scau.mouzhi.bean.Parttime;
import cn.scau.mouzhi.bean.Teacher;
import cn.scau.mouzhi.net.NetUtil;

public class Search extends Activity implements mySearchView.SearchViewListener, OnClickListener {

	private ListView lvResults;

	private mySearchView searchView;

	private ArrayAdapter<String> hintAdapter;

	private ArrayAdapter<String> autoCompleteAdapter;

	private SearchAdapter resultAdapter;

	private List<Bean> dbData;

	private EditText edt;

	private String newsSearchUrl = "http://121.42.189.168/mouzhi/news/search";

	private String parttimeSearchUrl = "http://121.42.189.168/mouzhi/recruitment/search";

	private String activitiesSearchUrl = "http://121.42.189.168/mouzhi/activity/search";

	private String teacherSearchUrl = "http://121.42.189.168/mouzhi/searchTeacher";

	private String helpingUrl = "http://121.42.189.168/mouzhi/setting/searchHelp";

	private int current = 0;

	private List<String> hintData;

	private List<String> autoCompleteData;

	private List<Bean> resultData;

	private static int DEFAULT_HINT_SIZE = 4;

	private static int hintSize = DEFAULT_HINT_SIZE;

	private Button search;

	private int[] newId = new int[10];

	private int[] parttimeId = new int[10];

	private int[] activitiesId = new int[10];

	private int[] teacherId = new int[10];

	private int[] helpingId = new int[10];

	private String activityName;

	private Drawable drawable = null;

	private static int empty = 1;

	private Bitmap bitmapImage;

	private ImageView image;

	// private Teacher teacher;

	public static void setHintSize(int hintSize) {
		Search.hintSize = hintSize;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		requestWindowFeature(Window.FEATURE_CONTENT_TRANSITIONS);
		getWindow().setEnterTransition(new Explode());
		setContentView(R.layout.activity_main);

		// initData();
		initViews();
		// 安卓4.0以后不支持主线程请求HTTP
		if (Build.VERSION.SDK_INT >= 11) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites()
					.detectNetwork().penaltyLog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects()
					.detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
		}
	}

	private void initViews() {
		Intent intent = getIntent();
		activityName = intent.getStringExtra("ActivityName");

		lvResults = (ListView) findViewById(R.id.main_lv_search_results);
		searchView = (mySearchView) findViewById(R.id.main_search_layout);
		searchView.setSearchViewListener(this);
		edt = (EditText) searchView.findViewById(R.id.search_et_input);
		image = (ImageView) findViewById(R.id.item_search_iv_icon);
		// searchView.setTipsHintAdapter(hintAdapter);
		// searchView.setAutoCompleteAdapter(autoCompleteAdapter);

		lvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int whichIsSelected, long l) {
				Intent intent = new Intent();
				if ("NewsFragment".equals(activityName)) {
					intent.setClass(Search.this, NewsDetail.class);
					News news = getNewsInfo(newId[whichIsSelected]);
					intent.putExtra("News", news);
				} else if ("ParttimeFragment".equals(activityName)) {
					intent.setClass(Search.this, ParttimeDetail.class);
					Parttime parttime = getParttimeInfo(parttimeId[whichIsSelected]);
					intent.putExtra("parttime", parttime);
				} else if ("ActivitiesFragment".equals(activityName)) {
					intent.setClass(Search.this, ActivitiesDetail.class);
					Activities activities = getActivitiesInfo(activitiesId[whichIsSelected]);
					intent.putExtra("Activities", activities);
				} else if ("SearchTeacher".equals(activityName)) {
					intent.setClass(Search.this, TeacherDetail.class);
					Teacher teacher = getTeacherInfo(whichIsSelected);
					intent.putExtra("Teacher", teacher);
					List courses = teacher.getCourse();
					if (courses != null) {
						int length = courses.size();
						String[] course = new String[length];
						for (int i = 0; i < length; i++) {
							course[i] = (String) courses.get(i);
						}
						intent.putExtra("course", course);
					}
				} else if ("Helping".equals(activityName)) {
					intent.setClass(Search.this, HelpingDetail.class);
					HelpingInfo helpingInfo = getHelpingInfo(whichIsSelected);
					intent.putExtra("Helping", helpingInfo); 
				}else {
					return;
				}
				startActivity(intent);
			}
		});

		search = (Button) findViewById(R.id.search_btn_back);
		search.setOnClickListener(this);
	}

	private void initData() {
		getNewsDbData(edt.getText().toString());
		getHintData();
		// getAutoCompleteData(null);
		getResultData(null);
	}

	private void getNewsDbData(String keyword) {
		dbData = new ArrayList();
		for (int i = 1; i < 10; i++) {
			News news = getNewsInfo(i, 1, newsSearchUrl);
			if (news != null) {
				if (news.getContent() != null) {
					empty = 0;
					newId[i - 1] = news.getNewsid();
					dbData.add(new Bean(bitmapImage, substring(news.getTitle()), substring(news.getContent()), ""));
				}
			} else {
				if (empty == 1) {
					Toast.makeText(getApplication(), "找不到新闻", Toast.LENGTH_SHORT).show();
				}
				break;
			}
		}
	}

	public News getNewsInfo(int pageNumber, int pageSize, String url2) {
		String str = getJSONString(pageNumber, pageSize, url2);
		News news = new News();
		try {
			JSONObject json = new JSONObject(str);
			JSONArray dataArray = json.getJSONArray("data");
			if (0 == dataArray.length()) {
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
					// news.setAvatar_url(data.getString("avatar_url"));
					news.setAvatar_url("http://121.42.189.168/mouzhi/download/20160418/1460964753363.png");
					// news.setImageurl(data.getString("imageurl"));
					news.setImageurl("http://121.42.189.168/mouzhi/download/20160418/1460964753363.png");
					news.setLikenum(data.getInt("likenum"));
					news.setDislikenum(data.getInt("dislikenum"));
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return news;
	}

	private void getParttimeDbData(String keyword) {
		dbData = new ArrayList();

		for (int i = 1; i < 10; i++) {
			empty = 1;
			Parttime parttime = getParttimeInfo(i, 1, parttimeSearchUrl);
			if (parttime != null) {
				if (parttime.getContent() != null) {
					empty = 0;
					parttimeId[i - 1] = parttime.getReid();
					// getImage("http://121.42.189.168/mouzhi/download/20160418/1460964753363.png");
					dbData.add(new Bean(bitmapImage, substring(parttime.getTitle()), substring(parttime.getContent()),
							""));
				}
			} else {
				if (empty == 1) {
					Toast.makeText(getApplication(), "找不到兼职", Toast.LENGTH_SHORT).show();
				}
				break;
			}
		}
	}

	public Parttime getParttimeInfo(int pageNumber, int pageSize, String url2) {

		String str = getJSONString(pageNumber, pageSize, url2);

		Parttime parttime = new Parttime();

		try {
			JSONObject json = new JSONObject(str);
			JSONArray dataArray = json.getJSONArray("data");

			for (int i = 0; i < dataArray.length(); i++) {
				JSONObject data = (JSONObject) dataArray.opt(i);
				parttime.setPublishtime(data.getString("publishtime"));
				parttime.setEmnumber(data.getInt("emnumber"));
				parttime.setReid(data.getInt("reid"));
				parttime.setAuthorid(data.getInt("authorid"));
				parttime.setTitle(data.getString("title"));
				parttime.setWorktime(data.getString("worktime"));
				parttime.setWorkplace(data.getString("workplace"));
				parttime.setContent(data.getString("content"));
				parttime.setWage(data.getString("wage"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return parttime;
	}

	private void getActivitiesDbData(String keyword) {
		dbData = new ArrayList();
		for (int i = 1; i < 10; i++) {
			empty = 1;
			Activities activities = getActivitiesInfo(i, 1, activitiesSearchUrl);
			if (activities != null) {
				if (activities.getContent() != null) {
					empty = 0;
					activitiesId[i - 1] = activities.getAcid();
					getImage("http://121.42.189.168/mouzhi/download/20160418/1460964753363.png");
					dbData.add(new Bean(bitmapImage, substring(activities.getTitle()),
							substring(activities.getContent()), ""));
				}
			} else {
				if (empty == 1) {
					Toast.makeText(getApplication(), "找不到活动", Toast.LENGTH_SHORT).show();
				}
				break;
			}
		}
	}

	public Activities getActivitiesInfo(int pageNumber, int pageSize, String url2) {
		String str = getJSONString(pageNumber, pageSize, url2);
		Activities ac = new Activities();
		try {
			JSONObject json = new JSONObject(str);
			JSONArray dataArray = json.getJSONArray("data");
			for (int i = 0; i < dataArray.length(); i++) {
				JSONObject data = (JSONObject) dataArray.opt(i);
				ac.setAcid(Integer.parseInt(data.getString("acid")));
				ac.setTitle(data.getString("title"));
				ac.setContent(data.getString("content"));
				ac.setAuthorid(data.getInt("authorid"));
				ac.setTime(data.getString("time"));
				// ac.setImagurl(data.getString("imageurl"));
				ac.setImagurl("http://hiphotos.baidu.com/baidu/pic/item/7d8aebfebf3f9e125c6008d8.jpg");
				ac.setNickname(data.getString("nickname"));
				// ac.setAvatar_url(data.getString("avatar_url"));
				ac.setAvatar_url("http://hiphotos.baidu.com/baidu/pic/item/7d8aebfebf3f9e125c6008d8.jpg");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ac;
	}

	private void getTeacherDbData() {
		dbData = new ArrayList();
		// for (int i = 1; i < 10; i++) {
		Teacher[] teachers = getTeacherInfo(teacherSearchUrl);
		for (Teacher teacher : teachers) {
			if (teacher != null) {
				if (teacher.getName() != null) {
					Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.actionbar_search_icon);
					dbData.add(new Bean(bitmap, teacher.getName(), teacher.getDepartment(), ""));
				} else {
					Toast.makeText(getApplication(), "找不到该老师", Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	public Teacher[] getTeacherInfo(String url2) {

		String str = getJSONString(1, 1, url2);
		int totalPage = 0;

		try {
			JSONObject json = new JSONObject(str);

			JSONArray dataArray = json.getJSONArray("information");
			JSONObject data = (JSONObject) dataArray.opt(0);

			totalPage = data.getInt("totalPage");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Teacher[] teachers = new Teacher[totalPage];

		for (int i = 1; i < totalPage; i++) {
			String str1 = getJSONString(i, 1, url2);

			List listOfCourse = null;
			teachers[i - 1] = new Teacher();
			try {
				JSONObject json = new JSONObject(str1);

				JSONArray dataArray = json.getJSONArray("information");
				JSONObject data = (JSONObject) dataArray.opt(0);

				String name = data.getString("name");
				String tel = data.getString("tel");
				String department = data.getString("department");
				String tid = data.getString("tid");

				JSONArray courseArray = data.getJSONArray("course");
				for (int j = 0; j < courseArray.length(); j++) {
					JSONObject courses = (JSONObject) courseArray.opt(j);
					listOfCourse = new ArrayList();
					String course = courses.getString("coursename");
					listOfCourse.add(course);
				}

				teachers[i - 1].setName(name);
				teachers[i - 1].setCourse(listOfCourse);
				teachers[i - 1].setDepartment(department);
				teachers[i - 1].setTel(tel);
				teachers[i - 1].setTid(tid);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return teachers;
	}

	private void getHelpingDbData(String keyword) {
		dbData = new ArrayList();
		for (int i = 1; i < 10; i++) {
			empty = 1;
			String helpingInfo = getHelpingInfo(i, 1, helpingUrl);
			if (helpingInfo != "") {
				empty = 0;
				Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.actionbar_search_icon);
				dbData.add(new Bean(bitmap, helpingInfo, "", ""));
			} else {
				if(empty == 1){
					Toast.makeText(getApplication(), "查询失败", Toast.LENGTH_SHORT).show();
				}
				break;
			}
		}
	}

	public String getHelpingInfo(int pageNumber, int pageSize, String url2) {

		String str = getJSONString(pageNumber, pageSize, url2);
		String helpingInfo2 = "";
		try {
			JSONObject json = new JSONObject(str);
			JSONArray dataArray = json.getJSONArray("data");
			if (0 == dataArray.length()) {
				return null;
			}
			for (int i = 0; i < dataArray.length(); i++) {
				JSONObject data = (JSONObject) dataArray.opt(i);
				for (int j = 0; j < data.length(); j++) {
					helpingInfo2 = data.getString("title");
				}
				helpingId[pageNumber - 1] = data.getInt("id");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return helpingInfo2;
	}

	private void getHintData() {
		hintData = new ArrayList(hintSize);
		hintData.add("");
		hintData.add("");
		hintData.add("");
		hintAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, hintData);
	}

	// private void getAutoCompleteData(String text) {
	// if (autoCompleteData == null) {
	// autoCompleteData = new ArrayList<>(hintSize);
	// } else {
	// autoCompleteData.clear();
	// for (int i = 0, count = 0; i < dbData.size()
	// && count < hintSize; i++) {
	// if (dbData.get(i).getTitle().contains(text.trim())) {
	// autoCompleteData.add(dbData.get(i).getTitle());
	// count++;
	// }
	// }
	// }
	// if (autoCompleteAdapter == null) {
	// autoCompleteAdapter = new ArrayAdapter<>(this,
	// android.R.layout.simple_list_item_1, autoCompleteData);
	// } else {
	// autoCompleteAdapter.notifyDataSetChanged();
	// }
	// }

	private void getResultData(String text) {
		if (resultData == null) {
			resultData = new ArrayList();
		} else {
			resultData.clear();
			for (int i = 0; i < dbData.size(); i++) {
				// if (dbData.get(i).getTitle().contains(text.trim())) {
				resultData.add(dbData.get(i));
				// }
			}
		}
		if (resultAdapter == null) {
			resultAdapter = new SearchAdapter(this, resultData, R.layout.item_bean_list);
		} else {
			resultAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onRefreshAutoComplete(String text) {
		// getAutoCompleteData(text);
	}

	@Override
	public void onSearch(String text) {
		// getResultData(text);
		// lvResults.setVisibility(View.VISIBLE);
		// if (lvResults.getAdapter() == null) {
		// lvResults.setAdapter(resultAdapter);
		// } else {
		// resultAdapter.notifyDataSetChanged();
		// }
		// Toast.makeText(this, "瀹屾垚鎼滅礌", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if ("NewsFragment".equals(activityName)) {
			getNewsDbData(edt.getText().toString().trim());
		} else if ("ParttimeFragment".equals(activityName)) {
			getParttimeDbData(edt.getText().toString().trim());
		} else if ("ActivitiesFragment".equals(activityName)) {
			getActivitiesDbData(edt.getText().toString().trim());
		} else if ("SearchTeacher".equals(activityName)) {
			getTeacherDbData();
		} else if ("Helping".equals(activityName)) {
			getHelpingDbData(edt.getText().toString().trim());
		}
		getResultData(null);
		lvResults.setVisibility(View.VISIBLE);
		if (lvResults.getAdapter() == null) {
			lvResults.setAdapter(resultAdapter);
			if ("NewsFragment".equals(activityName)) {
				getNewsDbData(edt.getText().toString().trim());
			} else if ("ParttimeFragment".equals(activityName)) {
				getParttimeDbData(edt.getText().toString().trim());
			} else if ("ActivitiesFragment".equals(activityName)) {
				getActivitiesDbData(edt.getText().toString().trim());
			} else if ("SearchTeacher".equals(activityName)) {
				getTeacherDbData();
			}
			getResultData(null);
			lvResults.setVisibility(View.VISIBLE);
			resultAdapter.notifyDataSetChanged();
		} else {
			resultAdapter.notifyDataSetChanged();
		}
	}

	public News getNewsInfo(int newId) {

		URL url = null;
		Map map;
		String str = null;
		try {
			url = new URL("http://121.42.189.168/mouzhi/news/checkdetail");
			map = new HashMap();
			map.put("newsid", newId);
			str = NetUtil.submitPostData(url, map);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		News news = new News();
		try {
			JSONObject json = new JSONObject(str);
			JSONObject data = (JSONObject) json.get("data");
			for (int j = 0; j < data.length(); j++) {
				news.setNewsid(data.getInt("newsid"));
				news.setTime(data.getString("time"));
				news.setShortcontent(data.getString("shortcontent"));
				news.setAuthorid(data.getInt("authorid"));
				news.setTitle(data.getString("title"));
				news.setContent(data.getString("content"));
				news.setStatus(data.getInt("status"));
				news.setNickname(data.getString("nickname"));
				// news.setAvatar_url(data.getString("avatar_url"));
				news.setAvatar_url("http://121.42.189.168/mouzhi/download/20160418/1460964753363.png");
				// news.setImageurl(data.getString("imageurl"));
				news.setImageurl("http://121.42.189.168/mouzhi/download/20160418/1460964753363.png");
				news.setLikenum(data.getInt("likenum"));
				news.setDislikenum(data.getInt("dislikenum"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return news;
	}

	public Parttime getParttimeInfo(int reid) {
		URL url = null;
		Map map;
		String str = null;
		try {
			url = new URL("http://121.42.189.168/mouzhi/recruitment/checkdetail");
			map = new HashMap();
			map.put("reid", reid);
			str = NetUtil.submitPostData(url, map);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Parttime parttime = new Parttime();

		try {
			JSONObject json = new JSONObject(str);
			JSONObject data = (JSONObject) json.get("data");
			parttime.setPublishtime(data.getString("publishtime"));
			parttime.setEmnumber(data.getInt("emnumber"));
			parttime.setAuthorid(data.getInt("authorid"));
			parttime.setTitle(data.getString("title"));
			parttime.setWorktime(data.getString("worktime"));
			parttime.setWorkplace(data.getString("workplace"));
			parttime.setContent(data.getString("content"));
			parttime.setWage(data.getString("wage"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return parttime;
	}

	public Activities getActivitiesInfo(int acid) {
		URL url = null;
		Map map;
		String str = null;
		try {
			url = new URL("http://121.42.189.168/mouzhi/activity/checkdetail");
			map = new HashMap();
			map.put("acid", acid);
			str = NetUtil.submitPostData(url, map);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Activities ac = new Activities();

		try {
			JSONObject json = new JSONObject(str);
			JSONObject data = (JSONObject) json.get("data");
			ac.setAcid(Integer.parseInt(data.getString("acid")));
			ac.setTitle(data.getString("title"));
			ac.setContent(data.getString("content"));
			ac.setAuthorid(data.getInt("authorid"));
			ac.setTime(data.getString("time"));
			// ac.setImagurl(data.getString("imageurl"));
			ac.setImagurl("http://121.42.189.168/mouzhi/download/20160418/1460964753363.png");
			ac.setNickname(data.getString("nickname"));
			// ac.setAvatar_url(data.getString("avatar_url"));
			ac.setAvatar_url("http://121.42.189.168/mouzhi/download/20160418/1460964753363.png");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ac;
	}

	public Teacher getTeacherInfo(int selected) {
		Teacher teacher = new Teacher();

		String str1 = getJSONString(selected + 1, 1, teacherSearchUrl);

		List listOfCourse = null;
		try {
			JSONObject json = new JSONObject(str1);

			JSONArray dataArray = json.getJSONArray("information");
			JSONObject data = (JSONObject) dataArray.opt(0);

			String name = data.getString("name");
			String tel = data.getString("tel");
			String department = data.getString("department");
			String tid = data.getString("tid");

			JSONArray courseArray = data.getJSONArray("course");
			for (int j = 0; j < courseArray.length(); j++) {
				JSONObject courses = (JSONObject) courseArray.opt(j);
				listOfCourse = new ArrayList();
				String course = courses.getString("coursename");
				listOfCourse.add(course);
			}

			teacher.setName(name);
			teacher.setCourse(listOfCourse);
			teacher.setDepartment(department);
			teacher.setTel(tel);
			teacher.setTid(tid);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return teacher;
	}

	public HelpingInfo getHelpingInfo(int selected) {
		HelpingInfo helpingInfo = new HelpingInfo();

		URL url = null;
		Map map;
		String str1 = null;
		try {
			url = new URL("http://121.42.189.168/mouzhi/setting/viewHelp");
			map = new HashMap();
			map.put("id", helpingId[selected]);
			str1 = NetUtil.submitPostData(url, map);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			JSONObject json = new JSONObject(str1);

			JSONObject data = (JSONObject) json.get("data");

			String title = data.getString("title");
			String content = data.getString("content");
			String time = data.getString("time");

			helpingInfo.setContent(content);
			helpingInfo.setTime(time);
			helpingInfo.setTitle(title);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return helpingInfo;
	}
	
	private void getImage(String imageurl) {
		// TODO Auto-generated method stub
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(imageurl, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				if (statusCode == 200) {
					// 创建工厂对象
					BitmapFactory bitmapFactory = new BitmapFactory();
					// 工厂对象的decodeByteArray把字节转换成Bitmap对象
					Bitmap bitmapImage = bitmapFactory.decodeByteArray(responseBody, 0, responseBody.length);

					// drawableImage = new BitmapDrawable(bitmap);
					// imageView.setImageBitmap(bitmap);
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
				Toast.makeText(Search.this, "加载图片失败", Toast.LENGTH_SHORT).show();
				error.printStackTrace();
			}
		});
	}

	private void setBitmap(Bitmap bitmap) {
		this.bitmapImage = bitmap;
	}

	private String substring(String str) {
		if (str.length() < 20) {
			return str;
		} else {
			return str.substring(0, 20) + "......";
		}
	}

	private String getJSONString(int pageNumber, int pageSize, String url2) {
		URL url = null;
		Map map;
		String str = null;
		try {
			url = new URL(url2);
			map = new HashMap();
			map.put("pageNumber", pageNumber);
			map.put("pageSize", pageSize);
			String keyword = null;
			try {
				keyword = URLEncoder.encode(edt.getText().toString().trim(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			map.put("keyword", keyword);
			str = NetUtil.submitPostData(url, map);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return str;
	}

}