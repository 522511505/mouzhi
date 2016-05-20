package cn.scau.mouzhi.adapter;

import java.util.List;

import android.content.Context;
import cn.scau.mouzhi.aty.R;
import cn.scau.mouzhi.bean.Bean;
import cn.scau.mouzhi.util.CommonAdapter;
import cn.scau.mouzhi.util.ViewHolder;

/**
 * Created by yetwish on 2015-05-11
 */

public class SearchAdapter extends CommonAdapter<Bean>{

    public SearchAdapter(Context context, List<Bean> data, int layoutId) {
        super(context, data, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, int position) {
        holder.setImageResource(R.id.item_search_iv_icon,mData.get(position).getDrawable())
                .setText(R.id.item_search_tv_title,mData.get(position).getTitle())
                .setText(R.id.item_search_tv_content,mData.get(position).getContent())
                .setText(R.id.item_search_tv_comments,mData.get(position).getComments());
    }
}
