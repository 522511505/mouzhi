package cn.scau.mouzhi.bean;

import java.io.Serializable;

import cn.scau.mouzhi.frag.SettingFragment;

public class User implements Serializable {
	private String nickname; // 昵称，用户名
	private int uid;
	private int groupid;
	private String avatar_url;

	private static User single=null;  
    //静态工厂方法   
    public static User getInstance(int uid,String avatar_url,int groupid,String nickname) {  
         if (single == null) {    
             single = new User(uid,avatar_url,groupid,nickname);  
         }    
        return single;  
    }
    
    public static User getInstance() {  
        if (single == null) {    
            single = new User();  
        }    
       return single;  
   }
    
    public User() {
		// TODO Auto-generated constructor stub
	}
    
	public User(String nickname,int uid) {
		// TODO Auto-generated constructor stub
		setNickname(nickname);
		setUid(uid);
	}
	
	public int getGroupid() {
		return groupid;
	}

	public void setGroupid(int groupid) {
		this.groupid = groupid;
	}

	public String getAvatar_url() {
		return avatar_url;
	}

	public void setAvatar_url(String avatar_url) {
		this.avatar_url = avatar_url;
	}

	public User(int uid,String avatar_url,int groupid,String nickname) {
		// TODO Auto-generated constructor stub
		this.uid = uid;
		this.nickname = nickname;
		this.groupid = groupid;
		this.avatar_url = avatar_url;
	}
	

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}
	
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

}
