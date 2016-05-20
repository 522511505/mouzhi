package cn.scau.mouzhi.adapter;


import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.scau.mouzhi.aty.R;

public class AtyParttimeLineAdapter extends BaseAdapter {

	private Context context = null;
	private List listItems;
	private LayoutInflater listContainer; // 视图容器
	private int itemViewResource;

	private static class ListItemView {
		public TextView title;
		public TextView salary;
		public TextView place;
		public TextView nowDate;
		public TextView workingDate;
		public TextView number;
	}

	public AtyParttimeLineAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public AtyParttimeLineAdapter(Context context, List<Map<String, Object>> listItems, int itemViewResource) {
		this.context = context;
		listContainer = LayoutInflater.from(context);
		this.listItems = listItems;
		this.itemViewResource = itemViewResource;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/**
	 * ListView Item设置
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// 自定义视图
		ListItemView listItemView = null;
		if (convertView == null) {
			listItemView = new ListItemView();
			
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.parttime_list_cell, null);
			
			listItemView.title = (TextView) convertView.findViewById(R.id.parttime_listItem_title);
			listItemView.salary = (TextView) convertView.findViewById(R.id.parttime_listItem_salary);
			listItemView.place = (TextView) convertView.findViewById(R.id.parttime_listItem_place);
			listItemView.number = (TextView) convertView.findViewById(R.id.parttime_listItem_people_number);
			listItemView.workingDate = (TextView) convertView.findViewById(R.id.parttime_listItem_workinking_date);
			
			listItemView.nowDate = (TextView) convertView.findViewById(R.id.parttime_listItem_now_date);
			
			convertView.setTag(listItemView);
		}
		else {
			listItemView = (ListItemView) convertView.getTag();
		}
		
		Map parttime =  (Map) listItems.get(position);
		
		listItemView.title.setText((String)parttime.get("title"));
		listItemView.salary.setText("工资待遇：" + (String)parttime.get("salary"));
		listItemView.place.setText("工作地点：" + (String)parttime.get("place"));
		listItemView.number.setText("招聘" + String.valueOf(parttime.get("number")) + "人");
		listItemView.workingDate.setText("工作时间：" + (String)parttime.get("date").toString().substring(0, 10) + "......");
//		listItemView.nowDate.setText((String)parttime.get("date"));
		
		// Message msg = getItem(position);// 适配器的位置
		// lc.getTvCellLabel().setText(msg.getMsg());// 获得文本并设置消息

		return convertView;
	}

	// 让它能被外界访问
	public Context getContext() {
		return context;
	}

	public void clear() {
		listItems.clear();
		notifyDataSetChanged();
	}
}
