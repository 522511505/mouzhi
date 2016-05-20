package cn.scau.mouzhi.bean;

import java.io.Serializable;

public class HelpingInfo implements Serializable{

	private String title;
	private String content;
	private String time;
	
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
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
}
