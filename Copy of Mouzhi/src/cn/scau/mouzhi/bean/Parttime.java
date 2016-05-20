package cn.scau.mouzhi.bean;

import java.io.Serializable;

public class Parttime  implements Serializable{
    private String publishtime;
    private int emnumber;  //招聘人数
    private int reid;  //兼职的id
    private int authorid;
    private String title;
    private String worktime;
    private String workplace;
    private String content;
    private String wage;

    private static Parttime single = null;

	// 静态工厂方法
	public static Parttime getInstance(String publishtime,int emnumber, int reid, int authorid,String title,String worktime,String workplace,String content,String wage) {
		if (single == null) {
			single = new Parttime(publishtime,emnumber,reid,authorid,title,worktime,workplace,content,wage);
		}
		return single;
	}
    
    public Parttime(String publishtime,int emnumber, int reid, int authorid,String title,String worktime,String workplace,String content,String wage) {
		// TODO Auto-generated constructor stub
    	this.publishtime = publishtime;
    	this.emnumber = emnumber;
    	this.reid = reid;
    	this.authorid = authorid;
    	this.title = title;
    	this.worktime = worktime;
    	this.workplace = workplace;
    	this.content = content;
    	this.wage = wage;
	}

    public Parttime() {
		// TODO Auto-generated constructor stub
	}
    
	public String getPublishtime() {
		return publishtime;
	}

	public void setPublishtime(String publishtime) {
		this.publishtime = publishtime;
	}

	public int getEmnumber() {
		return emnumber;
	}

	public void setEmnumber(int emnumber) {
		this.emnumber = emnumber;
	}

	public int getReid() {
		return reid;
	}

	public void setReid(int reid) {
		this.reid = reid;
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

	public String getWorktime() {
		return worktime;
	}

	public void setWorktime(String worktime) {
		this.worktime = worktime;
	}

	public String getWorkplace() {
		return workplace;
	}

	public void setWorkplace(String workplace) {
		this.workplace = workplace;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getWage() {
		return wage;
	}

	public void setWage(String wage) {
		this.wage = wage;
	}

}
