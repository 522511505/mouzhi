package cn.scau.mouzhi.bean;

import java.io.Serializable;

import android.graphics.Bitmap;

public class News implements Serializable {
	private int newsid;
	private String imageurl;
	private String time;
	private String shortcontent;
	private int authorid;
    private String title;
    private String content;
    private int status;
    private String nickname;
    private String avatar_url;
    private int likenum;
    private int dislikenum;
    private int likeStatus;
    private int dislikeStatus;
    
    public int getLikeStatus() {
		return likeStatus;
	}

	public void setLikeStatus(int likeStatus) {
		this.likeStatus = likeStatus;
	}

	public int getDislikeStatus() {
		return dislikeStatus;
	}

	public void setDislikeStatus(int dislikeStatus) {
		this.dislikeStatus = dislikeStatus;
	}
	private static News single = null;

	// 静态工厂方法
	public static News getInstance(int newsid,String imageurl,String time,String shortcontent, int authorid,String title,String content,int status,String nickname,String avatar_url, int likenum, int dislikenum) {
		if (single == null) {
			single = new News(newsid,imageurl,time,shortcontent,authorid,title,content,status,nickname,avatar_url, likenum, dislikenum);
		}
		return single;
	}
    
	public News() {
		// TODO Auto-generated constructor stub
	}
	
    public News(int newsid,String imageurl,String time,String shortcontent, int authorid,String title,String content,int status,String nickname,String avatar_url, int likenum, int dislikenum) {
		// TODO Auto-generated constructor stub
    	this.newsid = newsid;
    	this.imageurl = imageurl;
    	this.time = time;
    	this.shortcontent = shortcontent;
    	this.authorid = authorid;
    	this.title = title;
    	this.content = content;
    	this.status = status;
    	this.nickname = nickname;
    	this.avatar_url = avatar_url;
    	this.likenum = likenum;
    	this.dislikenum = dislikenum;
	}
    
	public int getLikenum() {
		return likenum;
	}

	public void setLikenum(int likenum) {
		this.likenum = likenum;
	}

	public int getDislikenum() {
		return dislikenum;
	}

	public void setDislikenum(int dislikenum) {
		this.dislikenum = dislikenum;
	}

	public int getNewsid() {
		return newsid;
	}
	public void setNewsid(int newsid) {
		this.newsid = newsid;
	}
	public String getImageurl() {
		return imageurl;
	}
	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getShortcontent() {
		return shortcontent;
	}
	public void setShortcontent(String shortcontent) {
		this.shortcontent = shortcontent;
	}
	public int getAuthorid() {
		return authorid;
	}
	public void setAuthorid(int authorid) {
		this.authorid = authorid;
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
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getAvatar_url() {
		return avatar_url;
	}
	public void setAvatar_url(String avatar_url) {
		this.avatar_url = avatar_url;
	}
	
}
