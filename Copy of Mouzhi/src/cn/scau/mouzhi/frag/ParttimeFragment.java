package cn.scau.mouzhi.frag;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import cn.scau.mouzhi.adapter.AtyParttimeLineAdapter;
import cn.scau.mouzhi.aty.R;
import cn.scau.mouzhi.atys.ParttimeDetail;
import cn.scau.mouzhi.atys.ParttimePublish;
import cn.scau.mouzhi.atys.Search;
import cn.scau.mouzhi.bean.Parttime;
import cn.scau.mouzhi.net.NetUtil;

public class ParttimeFragment extends Fragment implements OnRefreshListener {
	private AtyParttimeLineAdapter adapter = null;
	private List<Map<String, Object>> listItems;
	private ListView listView;
	private View view;
	private ImageView parttime_search, parttime_publish;
	private Parttime parttime;
	private SwipeRefreshLayout mRefreshLayout;
	private int current = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		// 安卓4.0以后不支持主线程请求HTTP
		if (Build.VERSION.SDK_INT >= 11) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites()
					.detectNetwork().penaltyLog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects()
					.detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
		}
		
		if (view == null) {
			view = inflater.inflate(R.layout.main_parttime, container, false);
		}

		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null) {
			parent.removeView(view);
		}

		// 列表的ListView
		listView = (ListView) view.findViewById(R.id.parttime_list);

		// ListView每一行的布局
		listItems = getListItems(1);

		// 适配器
		adapter = new AtyParttimeLineAdapter(getContext(), listItems, R.layout.parttime_list_cell);

		// 绑定适配器
		listView.setAdapter(adapter);

		initView(view);

		addListener();

		return view;
	}

	private void initView(View view) {
		// TODO Auto-generated method stub
		parttime_search = (ImageView) view.findViewById(R.id.parttime_search);
		parttime_publish = (ImageView) view.findViewById(R.id.parttime_publish);

		mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.srl_drop_down_refresh);
		mRefreshLayout.setEnabled(false);
		mRefreshLayout.setOnRefreshListener(this);

		mRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
				android.R.color.holo_orange_light, android.R.color.holo_red_light);

		listView.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView absListView, int i) {
			}

			@Override
			public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount,
					int totalItemCount) {
				if (firstVisibleItem == 0)
					mRefreshLayout.setEnabled(true);
				else
					mRefreshLayout.setEnabled(false);
			}
		});

		Intent intent = getActivity().getIntent();
		String intentName = intent.getStringExtra("intentName");
		if ("WelcomeActivity".equals(intentName)) {
			parttime_publish.setVisibility(View.GONE);
		}
	}

	private void addListener() {
		// TODO Auto-generated method stub
		parttime_search.setOnClickListener(new MyClickListener());
		parttime_publish.setOnClickListener(new MyClickListener());
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int whichIsSelected, long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getActivity(), ParttimeDetail.class);
//				Parttime pt = getParttimeInfo(5 * (current - 1) + whichIsSelected + 1,1); 
				Parttime pt = getParttimeInfo(current + whichIsSelected,1);
				intent.putExtra("parttime", pt);
				Intent it = getActivity().getIntent();
				Bundle bundle = it.getBundleExtra("bundle");
				intent.putExtra("bundle", bundle);
				getActivity().startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
			}

		});
	}

	class MyClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.parttime_search:
				Intent i = new Intent();
				i.setClass(getActivity(), Search.class);
				i.putExtra("ActivityName", "ParttimeFragment");
				getActivity().startActivity(i,ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
				break;
			case R.id.parttime_publish:
				Intent intent = new Intent();
				intent.setClass(getActivity(), ParttimePublish.class);
				Intent it = getActivity().getIntent();
				Bundle bundle = it.getBundleExtra("bundle");
				intent.putExtra("bundle", bundle);
				startActivity(intent);
				break;
			}
		}
	}

	// 每一行ListView的内容
	private List<Map<String, Object>> getListItems(int page) {
		listItems = new ArrayList<Map<String, Object>>();

		for (int i = page; i < page + 5; i++) {
			parttime = getParttimeInfo(i, 1);
			if(parttime == null){
				if(current > page){
					Toast.makeText(getActivity(),"没有最新的兼职",Toast.LENGTH_SHORT).show();
				}
			}else{
				Map<String, Object> mapTest = new HashMap<String, Object>();
				mapTest.put("title", parttime.getTitle());
				mapTest.put("salary", parttime.getWage());
				mapTest.put("place", parttime.getWorkplace());
				mapTest.put("date", parttime.getWorktime());
				mapTest.put("number", parttime.getEmnumber());
				listItems.add(mapTest);
			}
		}
		return listItems;
	}

	public Parttime getParttimeInfo(int pageNumber, int pageSize) {

		URL url = null;
		Map map;
		String str = null;
		try {
			url = new URL("http://121.42.189.168/mouzhi/recruitment/reclist");
			map = new HashMap();
			map.put("pageNumber", pageNumber);
			map.put("pageSize", pageSize);
			str = NetUtil.submitPostData(url, map);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		parttime = new Parttime();
		
		try {
			JSONObject json = new JSONObject(str);
			JSONArray dataArray = json.getJSONArray("data");
			int totalSize = json.getInt("totalPage");
			if(pageNumber >= totalSize + 1){
				return null;
			}

			for (int i = 0; i < dataArray.length(); i++) {
				JSONObject data = (JSONObject) dataArray.opt(i);

				// mapTest.put("title", data.getString("title"));
				// mapTest.put("salary", data.getString("wage"));
				// mapTest.put("place", data.getString("workplace"));
				// mapTest.put("date",data.getString("worktime"));
				// mapTest.put("howlong", "2天");
				// mapTest.put("number", data.getInt("emnumber"));

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

		// news = new News(newsid, imageurl, time, shortcontent, authorid,
		// title, content, status, nickname, avatar_url,likenum,dislikenum);

		return parttime;
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		mRefreshLayout.setRefreshing(true);
		(new Handler()).postDelayed(new Runnable() {
			@Override
			public void run() {
				current+=5;
				listItems = getListItems(current);
				if(listItems.isEmpty()){
					Toast.makeText(getActivity(),"没有最新的兼职",Toast.LENGTH_SHORT).show();
					current-=5;
				}else{
//					listItems.clear();
					adapter = new AtyParttimeLineAdapter(getContext(), listItems, R.layout.parttime_list_cell);
					listView.setAdapter(adapter);
				}
				mRefreshLayout.setRefreshing(false);
			}
		}, 3000);
	}
}
