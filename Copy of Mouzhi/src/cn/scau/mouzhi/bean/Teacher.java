package cn.scau.mouzhi.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.R.transition;

public class Teacher implements Serializable{
	private String name;
	private List<String> course = new ArrayList<String>();
	private String tel;
	private String department;
    private String tid;
    
    public Teacher() {
		// TODO Auto-generated constructor stub
	}
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getCourse() {
		return course;
	}

	public void setCourse(List<String> course) {
		this.course = course;
	}

	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
}
