package cn.scau.mouzhi.bean;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * bean 锟�?
 * Created by yetwish on 2015-05-11
 */

public class Bean {

//    private int iconId;
    private Bitmap drawable;
    private String title;
    private String content;
    private String comments;

    public Bean(Bitmap drawable, String title, String content, String comments) {
        this.drawable = drawable;
        this.title = title;
        this.content = content;
        this.comments = comments;
    }

//    public int getIconId() {
//        return iconId;
//    }
//
//    public void setIconId(int iconId) {
//        this.iconId = iconId;
//    }

    public String getTitle() {
        return title;
    }

    public Bitmap getDrawable() {
		return drawable;
	}

	public void setDrawable(Bitmap drawable) {
		this.drawable = drawable;
	}

	public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
