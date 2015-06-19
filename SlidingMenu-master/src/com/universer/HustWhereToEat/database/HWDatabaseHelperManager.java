package com.universer.HustWhereToEat.database;

import java.sql.SQLException;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.universer.HustWhereToEat.application.HWApplication;

public class HWDatabaseHelperManager {


	private volatile HWDataBaseHelper helper;

	private static volatile HWDatabaseHelperManager instance;

	private static volatile int instanceCount;

	private HWDatabaseHelperManager() {

	}

	public static HWDatabaseHelperManager getInstance() {

		if (instance == null) {
			instance = new HWDatabaseHelperManager();
		}

		return instance;
	}

	public HWDataBaseHelper getHelper() throws SQLException {
		HWApplication app = HWApplication.getInstance();
		if (helper == null) {
			helper = OpenHelperManager.getHelper(app, HWDataBaseHelper.class);
		}
		instanceCount++;
		return helper;
	}

	public void releaseHelper(HWDataBaseHelper helper) {
		if (helper != null) {
			instanceCount--;
			if (instanceCount <= 0) {
				OpenHelperManager.releaseHelper();
				this.helper = null;
			}
		}
	}
	
	public void forceReleaseHelper(){
		if(this.helper != null){
			OpenHelperManager.releaseHelper();
		}
		instanceCount = 0;
	}


}
