package cn.scau.mouzhi.config;

import android.content.Context;
import android.content.SharedPreferences.Editor;

public class Config {
	// public static final String SERVER_URL = "10.0.2.2";
//	public static final String SERVER_URL = "115.28.86.185/api/user/register";
	public static final String APP_ID = "cn.scau.mouzhi";
	public static final String KEY_ACCOUNT = "account";
	public static final String KEY_PASSWORD = "password";
	public static final String KEY_NICKNAME = "nickname";
	public static final String KEY_UID = "uid";
	public static final String KEY_AVATAR_URL = "avatar_url";
	public static final String KEY_IfFirstTime = "ifFirstTime";
	public static final String CHARSET = "utf-8";
	public static final String KEY_STATUS = "status";
	public static final int RESULT_STATUS_SUCCESS = 1;

	// 获得缓存的账号
	public static String getCachedAccount(Context context) {
		return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).getString(KEY_ACCOUNT, null);
	}

	public static void cacheAccount(Context context, String account) {
		Editor e = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).edit();// 获得编辑器
		e.putString(KEY_ACCOUNT, account);
		e.commit(); // 提交修改
	}

	// 获得缓存的密码
	public static String getCachedPassword(Context context) {
		return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).getString(KEY_PASSWORD, null);
	}

	public static void cachePassword(Context context, String password) {
		Editor e = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).edit();
		e.putString(KEY_PASSWORD, password);
		e.commit();
	}

	// 获得缓存名字
	public static String getCachedNickname(Context context) {
		return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).getString(KEY_NICKNAME, null);
	}

	public static void cacheNickname(Context context, String nickname) {
		Editor e = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).edit();
		e.putString(KEY_NICKNAME, nickname);
		e.commit();
	}

	// 获得缓存头像url
	public static String getCachedAvatart_url(Context context) {
		return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).getString(KEY_AVATAR_URL, null);
	}

	public static void cacheAvatart_url(Context context, String avatar_url) {
		Editor e = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).edit();
		e.putString(KEY_AVATAR_URL, avatar_url);
		e.commit();
	}

	// 获得缓存用户id
	public static String getCachedUid(Context context) {
		return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).getString(KEY_UID, null);
	}

	public static void cacheUid(Context context, String uid) {
		Editor e = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).edit();
		e.putString(KEY_UID, uid);
		e.commit();
	}

	// 获得缓存用户id
	public static String getCachedIfFirstTime(Context context) {
		return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).getString(KEY_IfFirstTime, null);
	}

	public static void cacheIfFirstTime(Context context, String ifFirstTime) {
		Editor e = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).edit();
		e.putString(KEY_IfFirstTime, ifFirstTime);
		e.commit();
	}
}
