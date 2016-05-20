package cn.scau.mouzhi.bean;

import java.io.Serializable;

public class Driver  implements Serializable{

	private String name;
	private int id;
	private String avatar;
	
	public Driver() {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
}
