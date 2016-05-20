package cn.scau.mouzhi.bean;

import java.io.Serializable;
import java.util.Date;

import android.app.Activity;
import android.graphics.Bitmap;

public class Activities implements Serializable{
	private int acid;
	private String title;
	private String content;
	private int authorid;
	private String time;
	private String avatar_url;
	private String imagurl;
	private String nickname;

	private static Activities single = null;

	// 静态工厂方法
	public static Activities getInstance(int acid, String title, String content, int authorid, String time, String avatar_url,
			String imagurl, String nicknamel) {
		if (single == null) {
			single = new Activities(acid,title, content,authorid, time, avatar_url,imagurl,nicknamel);
		}
		return single;
	}

	public Activities(int acid, String title, String content, int authorid, String time, String avatar_url,
			String imagurl, String nickname) {
		// TODO Auto-generated constructor stub
		this.acid = acid;
		this.title = title;
		this.content = content;
		this.authorid = authorid;
		this.time = time;
		this.avatar_url = avatar_url;
		this.imagurl = imagurl;
		this.nickname = nickname;
	}

	public Activities() {
		// TODO Auto-generated constructor stub
	}

	public String getAvatar_url() {
		return avatar_url;
	}

	public void setAvatar_url(String avatar_url) {
		this.avatar_url = avatar_url;
	}

	public int getAcid() {
		return acid;
	}

	public void setAcid(int acid) {
		this.acid = acid;
	}

	public String getTitle() {
		return title;
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

	public int getAuthorid() {
		return authorid;
	}

	public void setAuthorid(int authorid) {
		this.authorid = authorid;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getImagurl() {
		return imagurl;
	}

	public void setImagurl(String imagurl) {
		this.imagurl = imagurl;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
}
