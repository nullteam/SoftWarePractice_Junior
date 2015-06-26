package com.universer.HustWhereToEat.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesUtil {
	public static final String USER_ID="userName";
	public static final String SETTING="setting";
	public static final String CURRENT_ACCOUNT="currenrAccount";
	private static SharedPreferences setCurrentAccountSharedPreferences = null;
	
	public static SharedPreferences setUserSharedPreference(Context context,String userName){
		SharedPreferences userSharedPreferences=context.getSharedPreferences(userName,Context.MODE_PRIVATE);
		return userSharedPreferences;
	}
	
	public static int getUserIntShare(Context context,String key,int defaultValue,String userName){
		SharedPreferences userSharedPreferences=context.getSharedPreferences(userName,Context.MODE_PRIVATE);
		return userSharedPreferences.getInt(key, defaultValue);
	}
	
	public static String getUserStringShare(Context context,String key,String defaultValue,String userName){
		SharedPreferences userSharedPreferences=context.getSharedPreferences(userName,Context.MODE_PRIVATE);
		return userSharedPreferences.getString(key, defaultValue);
	}
	
	public static void setUserStringShare(Context context,String userName,String password){
		SharedPreferences userSharedPreferences=context.getSharedPreferences(userName,Context.MODE_PRIVATE);
		Editor editor=userSharedPreferences.edit();
		editor.putString("userName",userName);
		editor.putString("password",password);
		editor.commit();
	}
	
	public static SharedPreferences setSettingSharedPreferences(Context context){
		SharedPreferences settingSharedPreferences=context.getSharedPreferences(SETTING,Context.MODE_PRIVATE);
		return settingSharedPreferences;
	}
	
	public static void setCurrentAccountSharedPreferences(Context context){
		setCurrentAccountSharedPreferences = context.getSharedPreferences(CURRENT_ACCOUNT,Context.MODE_PRIVATE);
		return;
	}
	
	public static String getCurrentUserStringShare(Context context,String key,String defaultValue){
		SharedPreferences currentSharedPreferences=context.getSharedPreferences(CURRENT_ACCOUNT,Context.MODE_PRIVATE);
		return currentSharedPreferences.getString(key, defaultValue);
	}
	
	public static void setCurrentUserStringShare(Context context,String userName,String password){
		SharedPreferences userSharedPreferences=context.getSharedPreferences(CURRENT_ACCOUNT,Context.MODE_PRIVATE);
		Editor editor=userSharedPreferences.edit();
		editor.putString("userName",userName);
		editor.putString("password",password);
		editor.commit();
	}
}
