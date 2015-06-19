package com.universer.HustWhereToEat.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {
	public static final String USER_ID="user_name";
	public static final String SETTING="setting";
	public static final String CURRENT_ACCOUNT="currenrAccount";
	
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
	
	public static SharedPreferences setSettingSharedPreferences(Context context){
		SharedPreferences settingSharedPreferences=context.getSharedPreferences(SETTING,Context.MODE_PRIVATE);
		return settingSharedPreferences;
	}
	
	public static SharedPreferences setCurrentAccountSharedPreferences(Context context){
		SharedPreferences setCurrentAccountSharedPreferences=context.getSharedPreferences(CURRENT_ACCOUNT,Context.MODE_PRIVATE);
		return setCurrentAccountSharedPreferences;
	}
	
	public static String getCurrentUserStringShare(Context context,String key,String defaultValue){
		SharedPreferences currentSharedPreferences=context.getSharedPreferences(CURRENT_ACCOUNT,Context.MODE_PRIVATE);
		return currentSharedPreferences.getString(key, defaultValue);
	}
}
